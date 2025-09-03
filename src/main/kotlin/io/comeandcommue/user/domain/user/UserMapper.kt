package io.comeandcommue.user.domain.user

fun UserEntity.toDto() = UserDto(
    id = id ?: "",
    nickname = nickname,
    dailyNicknameChangeRemain =  dailyNicknameChangeRemain,
    createdAt = createdAt,
)
