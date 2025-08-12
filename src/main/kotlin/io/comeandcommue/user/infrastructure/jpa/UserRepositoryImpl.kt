package io.comeandcommue.user.infrastructure.jpa

import io.comeandcommue.user.domain.user.UserEntity
import io.comeandcommue.user.domain.user.UserRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    @PersistenceContext private val em: EntityManager,
    private val userJpaRepository: UserJpaRepository
) : UserRepository {
    override fun save(user: UserEntity): UserEntity =
        userJpaRepository.save(user)

    override fun findById(id: String): UserEntity =
        userJpaRepository.findById(id).orElseThrow {  IllegalArgumentException("User with id $id not found") }
}
