package com.github.brunomartinscorrea.adapter.database

interface DatabaseApi {
    suspend fun <T> transaction(function: () -> T): T
}
