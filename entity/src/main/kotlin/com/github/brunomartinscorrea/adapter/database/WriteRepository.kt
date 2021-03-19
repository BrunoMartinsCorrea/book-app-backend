package com.github.brunomartinscorrea.adapter.database

import com.github.brunomartinscorrea.user.User

interface WriteRepository<T, R> {
    suspend fun save(entity: T): User
    suspend fun update(entity: T)
    suspend fun delete(id: R)
}
