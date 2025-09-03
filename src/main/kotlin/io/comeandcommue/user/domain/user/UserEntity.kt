package io.comeandcommue.user.domain.user

import io.comeandcommue.user.common.ShortId
import jakarta.persistence.*
import lombok.EqualsAndHashCode
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@EqualsAndHashCode(of = ["id"], callSuper = false)
@EntityListeners(AuditingEntityListener::class)
class UserEntity(
    @Id
    @ShortId
    var id:  String? = null,

    @Column
    var nickname: String = "",

    @Column
    var password: String = "",

    @Column(name = "daily_nickname_change_remain")
    var dailyNicknameChangeRemain: Int = 3,

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    val createdAt: Instant =  Instant.now(),
) {
    protected constructor() : this(id = null)

    fun changeNickname(newNickname: String) {
        if (dailyNicknameChangeRemain == 0) {
            // 한국어로 에러 메시지 발생
            throw IllegalStateException("오늘 닉네임 변경 가능 횟수를 모두 사용했습니다. 내일 다시 시도해주세요.")
        }
        nickname =  newNickname
        dailyNicknameChangeRemain--
    }
}
