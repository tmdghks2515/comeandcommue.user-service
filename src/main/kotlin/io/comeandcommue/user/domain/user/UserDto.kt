package io.comeandcommue.user.domain.user

import java.time.Instant
import java.time.LocalDateTime

data class UserDto(
    val id:  String,
    val nickname: String,
    val dailyNicknameChangeRemain: Int,
    val createdAt: Instant,
)
