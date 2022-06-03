package com.github.brunomartinscorrea.config.utils

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

open class AuditableTable(name: String) : Table(name) {
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val deletedAt = datetime("deleted_at").nullable()
}
