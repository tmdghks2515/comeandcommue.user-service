package io.comeandcommue.user.infrastructure.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Component
class NicknameRedisStore(
    private val redisTemplate: StringRedisTemplate
) {
    /**
     * 랜덤 닉네임을 생성합니다.
     *
     * <p>로직 흐름</p>:
     * 1. Redis Set `nickname-set-1`, `nickname-set-2` 에서 각각 무작위 요소를 하나씩 조회합니다.
     * 2. 두 값을 결합하여 기본 닉네임 문자열(`combinedKey`)을 만듭니다.
     * 3. 닉네임 시퀀스 키(`nickname-seq:<combinedKey>`)를 `INCR`로 원자적으로 증가시킵니다.
     *    - 증가 결과가 1이면 처음 생성된 닉네임 → 그대로 반환
     *    - 2 이상이면 접미 숫자를 붙여 유니크한 닉네임을 반환
     *
     * <p>제약 조건</p>:
     * - 두 Set 중 하나라도 비어 있으면 `IllegalArgumentException` 발생
     * - Redis INCR 연산 실패 시 기본값 1L 로 처리
     *
     * @return 유니크한 닉네임 문자열
     * @throws IllegalArgumentException `nickname-set-1` 또는 `nickname-set-2`가 비어 있는 경우
     */
    fun  createNickname(): String {
        val nickname1 = redisTemplate.opsForSet().randomMember("nickname-set-1")
        val nickname2 = redisTemplate.opsForSet().randomMember("nickname-set-2")

        require(nickname1.isNotBlank() && nickname2.isNotBlank()) {
            "nickname-set-1 또는 nickname-set-2에서 값을 가져오지 못했습니다."
        }

        val combinedKey = "$nickname1$nickname2"
        val seqKey = "nickname-seq:$combinedKey"

        // 1,2,3... 원자 증가
        val currentSeq = redisTemplate.opsForValue().increment(seqKey) ?: 1L

        return if (currentSeq == 1L) combinedKey else "$combinedKey$currentSeq"
    }

    /**
     * 사용자 닉네임을 새로 생성하여 반환합니다.
     *
     * <p>로직 흐름:</p>
     * 1. 오늘 날짜(KST)를 기준으로 사용자별 닉네임 변경 횟수를 카운팅하기 위한 Redis 키를 생성합니다.
     * 2. `INCR` 연산으로 변경 횟수를 1 증가시키고, 첫 증가 시 자정까지 만료 시간을 설정합니다.
     * 3. 변경 횟수가 3회를 초과하면 카운터를 롤백(`DECR`)하고 예외를 발생시킵니다.
     * 4. 횟수가 유효하면 `createNickname()`을 호출해 새로운 닉네임을 생성하고 반환합니다.
     *
     * <p>제약 조건:</p>
     * - 하루 최대 3회까지 변경 가능 (자정(KST) 기준으로 리셋)
     * - 3회를 초과하면 `IllegalStateException` 발생
     *
     * @param userId 닉네임을 변경할 사용자 ID
     * @return 새로 생성된 닉네임 문자열
     * @throws IllegalStateException 하루 변경 가능 횟수를 초과한 경우
     */
    fun changeNickname(userId: String): String {
        // 1. 기준 키 생성
        val dailyNicknameChangeCountKey = getDailyNicknameChangeCountKey(userId)

        // 2. 카운터 증가 (원자적 INCR)
        val newCount = redisTemplate.opsForValue().increment(dailyNicknameChangeCountKey) ?: 1L

        // 3. 첫 증가 시 자정까지 만료 설정
        if (newCount == 1L) {
            val today = LocalDate.now(ZoneId.of("Asia/Seoul"))
            val midnight = today.plusDays(1).atStartOfDay(ZoneId.of("Asia/Seoul"))
            val expireAt = Date.from(midnight.toInstant())
            redisTemplate.expireAt(dailyNicknameChangeCountKey, expireAt)
        }

        // 4. 제한 횟수 검사
        if (newCount > 3) {
            redisTemplate.opsForValue().decrement(dailyNicknameChangeCountKey)
            throw IllegalStateException("오늘의 닉네임 변경 가능 횟수를 초과했습니다.")
        }

        return createNickname()
    }

    /**
     * 오늘 사용자의 닉네임 변경 횟수를 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 오늘 닉네임 변경 횟수 (기본값 0)
     */
    fun getNicknameChangedCount(userId: String): Int {
        // 1. 키 생성
        val dailyNicknameChangeCountKey = getDailyNicknameChangeCountKey(userId)
        return redisTemplate.opsForValue()
            .get(dailyNicknameChangeCountKey)
            ?.toIntOrNull()
            ?: 0
    }

    private fun getDailyNicknameChangeCountKey(userId: String): String = "user:{$userId}:nickname:change"
}
