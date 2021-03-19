package com.github.brunomartinscorrea.adapter.database

import com.github.brunomartinscorrea.user.User

interface ReadRepository<T, R> {
    suspend fun findById(id: R): User
    suspend fun findAll(): List<T>
}
