package com.github.brunomartinscorrea.user

import com.github.brunomartinscorrea.adapter.database.UserRepository
import com.github.brunomartinscorrea.adapter.service.UserService

class UserServiceImpl(
    private val repository: UserRepository
) : UserService {

    override suspend fun findById(id: Long) = repository.findById(id)

    override suspend fun findAll() = repository.findAll()

    override suspend fun save(entity: User) = repository.save(entity)

    override suspend fun update(entity: User) {
        repository.update(entity)
    }

    override suspend fun delete(id: Long) {
        repository.delete(id)
    }
}
