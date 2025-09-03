package io.comeandcommue.user.application

import io.comeandcommue.user.domain.user.UserDto
import io.comeandcommue.user.domain.user.UserRepository
import io.comeandcommue.user.domain.user.toDto
import io.comeandcommue.user.infrastructure.redis.NicknameRedisStore
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ChangeNicknameUseCase(
    private val userRepository: UserRepository,
    private val nicknameRedisStore: NicknameRedisStore
) {
    fun changeNickname(userId: String): UserDto {
        val  user = userRepository.findById(userId)
        user.changeNickname(nicknameRedisStore.createNickname())

        return userRepository.save(user)
            .toDto()
    }
}
