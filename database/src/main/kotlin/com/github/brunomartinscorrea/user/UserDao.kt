package com.github.brunomartinscorrea.user

import com.github.brunomartinscorrea.config.utils.AuditableTable
import org.jetbrains.exposed.sql.ResultRow

object UserDao : AuditableTable("user") {
    val id = long("id").autoIncrement()
    val name = varchar("name", 100).nullable()
    val email = varchar("email", 100).nullable().index("user_email_idx")
    val password = varchar("password", 100).nullable()

    override val primaryKey = PrimaryKey(id, name = "user_id_pkey")

    fun ResultRow.toEntity() = User(
        this[id],
        this[name],
        this[email],
        this[password],
        this[createdAt],
        this[updatedAt],
        this[deletedAt]
    )
}
