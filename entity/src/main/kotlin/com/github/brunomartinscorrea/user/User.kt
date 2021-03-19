package com.github.brunomartinscorrea.user

import com.github.brunomartinscorrea.utils.Auditable
import java.time.LocalDateTime

data class User(
    val id: Long?,
    val name: String?,
    val email: String?,
    val password: String?,
    override val createdAt: LocalDateTime?,
    override val updatedAt: LocalDateTime?,
    override val deletedAt: LocalDateTime?
) : Auditable
