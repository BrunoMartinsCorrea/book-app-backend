package com.github.brunomartinscorrea.utils

import java.time.LocalDateTime

interface Auditable {
    val createdAt: LocalDateTime?
    val updatedAt: LocalDateTime?
    val deletedAt: LocalDateTime?
}
