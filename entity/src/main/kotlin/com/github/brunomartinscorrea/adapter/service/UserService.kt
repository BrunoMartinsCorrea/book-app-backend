package com.github.brunomartinscorrea.adapter.service

import com.github.brunomartinscorrea.user.User

interface UserService {
    suspend fun findById(id: Long): User
    suspend fun findAll(): List<User>
    suspend fun save(entity: User): User
    suspend fun update(entity: User)
    suspend fun delete(id: Long)
}
