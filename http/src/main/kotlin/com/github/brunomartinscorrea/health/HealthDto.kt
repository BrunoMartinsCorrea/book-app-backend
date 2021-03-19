package com.github.brunomartinscorrea.health

import kotlinx.serialization.Serializable

@Serializable
data class HealthDto(
    val status: Boolean
)
