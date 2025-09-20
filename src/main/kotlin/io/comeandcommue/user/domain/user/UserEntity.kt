package io.comeandcommue.user.domain.user

import io.comeandcommue.lib.data.baseEntity.BaseEntity
import io.comeandcommue.lib.data.shortId.ShortId
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
) : BaseEntity() {
    protected constructor() : this(id = null)

    fun changeNickname(newNickname: String) {
        require(newNickname.isNotBlank()) { "닉네임은 비어있을 수 없습니다." }
        nickname = newNickname
    }
}
