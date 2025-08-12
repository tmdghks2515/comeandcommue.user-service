package io.comeandcommue.user.domain.user

import java.time.LocalDateTime

data class UserDto(
    val id:  String,
    val nickname: String,
    val createdAt: LocalDateTime,
)
