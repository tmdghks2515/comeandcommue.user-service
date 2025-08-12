package io.comeandcommue.user.infrastructure.jpa

import io.comeandcommue.user.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, String> {
}
