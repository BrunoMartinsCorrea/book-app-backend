package com.github.brunomartinscorrea.user

import com.github.brunomartinscorrea.adapter.database.DatabaseApi
import com.github.brunomartinscorrea.adapter.database.UserRepository
import com.github.brunomartinscorrea.exception.database.RegisterNotFound
import com.github.brunomartinscorrea.exception.database.RegisterNotSaved
import com.github.brunomartinscorrea.user.UserDao.toEntity
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class UserRepositoryImpl(
    private val databaseApi: DatabaseApi
) : UserRepository {

    private val dao = UserDao

    override suspend fun save(entity: User) = databaseApi.transaction {
        val now = LocalDateTime.now()

        dao.insert {
            it[name] = entity.name
            it[email] = entity.email
            it[password] = entity.password
            it[createdAt] = now
            it[updatedAt] = now
        }.resultedValues
            ?.firstOrNull()
            ?.toEntity() ?: throw RegisterNotSaved("The user could not be saved!")
    }

    override suspend fun update(entity: User): Unit = databaseApi.transaction {
        dao.update({
            (dao.id eq entity.id!!) and (dao.deletedAt eq null)
        }) {
            it[name] = entity.name
            it[email] = entity.email
            it[password] = entity.password
            it[updatedAt] = LocalDateTime.now()
        }
    }

    override suspend fun findById(id: Long) = databaseApi.transaction {
        dao.select {
            (dao.id eq id) and (dao.deletedAt eq null)
        }.mapNotNull {
            it.toEntity()
        }.singleOrNull() ?: throw RegisterNotFound("User with id $id not found!")
    }

    override suspend fun findAll() = databaseApi.transaction {
        dao.select {
            dao.deletedAt eq null
        }.mapNotNull {
            it.toEntity()
        }
    }

    override suspend fun delete(id: Long): Unit = databaseApi.transaction {
        dao.update({
            (dao.id eq id) and (dao.deletedAt eq null)
        }) {
            it[name] = null
            it[email] = null
            it[password] = null
            it[deletedAt] = LocalDateTime.now()
        }
    }
}
