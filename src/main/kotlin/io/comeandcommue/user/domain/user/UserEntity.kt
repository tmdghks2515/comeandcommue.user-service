package io.comeandcommue.user.domain.user

import io.comeandcommue.user.common.ShortId
import jakarta.persistence.*
import lombok.EqualsAndHashCode
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime =  LocalDateTime.now(),
) {
    protected constructor() : this(id = null)
}
