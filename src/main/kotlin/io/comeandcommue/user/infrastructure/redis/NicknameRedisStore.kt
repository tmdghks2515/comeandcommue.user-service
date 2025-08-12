package io.comeandcommue.user.infrastructure.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class NicknameRedisStore(
    private val redisTemplate: StringRedisTemplate
) {
    fun  createNickname(): String {
        val nickname1 = redisTemplate.opsForSet().randomMember("nickname-set-1")
        val nickname2 = redisTemplate.opsForSet().randomMember("nickname-set-2")

        require(nickname1.isNotBlank() && nickname2.isNotBlank()) {
            "nickname-set-1 또는 nickname-set-2에서 값을 가져오지 못했습니다."
        }

        val combinedKey = "$nickname1$nickname2"
        val seqKey = "nickname-seq:$combinedKey"

        // 시퀀스 조회
        val currentSeq = redisTemplate.opsForValue().get(seqKey)

        return if (currentSeq == null) {
            // 없으면 1로 세팅하고 원본 닉네임 리턴
            redisTemplate.opsForValue().set(seqKey, "1")
            combinedKey
        } else {
            // 이미 있으면 seq 붙여서 리턴 (그리고 seq 증가)
            val nextSeq = currentSeq.toInt() + 1
            redisTemplate.opsForValue().set(seqKey, nextSeq.toString())
            "$combinedKey$nextSeq"
        }
    }
}
