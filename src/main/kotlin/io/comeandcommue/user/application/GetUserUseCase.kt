package io.comeandcommue.user.application

import io.comeandcommue.user.domain.user.UserDto
import io.comeandcommue.user.domain.user.UserRepository
import io.comeandcommue.user.domain.user.toDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    fun getUser(userId: String): UserDto =
        userRepository.findById(userId).toDto()
}
