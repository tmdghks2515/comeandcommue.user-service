package io.comeandcommue.user.domain.user


interface UserRepository {
    fun save(user: UserEntity): UserEntity
    fun findById(id: String): UserEntity
}
