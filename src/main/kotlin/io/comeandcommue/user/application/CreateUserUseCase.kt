package io.comeandcommue.user.application

import io.comeandcommue.user.domain.user.UserDto
import io.comeandcommue.user.domain.user.UserEntity
import io.comeandcommue.user.domain.user.UserRepository
import io.comeandcommue.user.domain.user.toDto
import io.comeandcommue.user.infrastructure.redis.NicknameRedisStore
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val nicknameRedisStore: NicknameRedisStore
) {
    fun createUser(): UserDto {
        val newUser = UserEntity(
            nickname = nicknameRedisStore.createNickname(),
        )
        return userRepository.save(newUser)
            .toDto()
    }
}
