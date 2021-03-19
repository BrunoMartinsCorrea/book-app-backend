package com.github.brunomartinscorrea.user

import com.github.brunomartinscorrea.config.serializer.LocalDateTimeSerializer
import com.github.brunomartinscorrea.utils.DtoConverter
import com.github.brunomartinscorrea.utils.EntityConverter
import java.time.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    @Serializable(LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = null,
    @Serializable(LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime? = null,
    @Serializable(LocalDateTimeSerializer::class)
    val deletedAt: LocalDateTime? = null
) : EntityConverter<User> {

    override fun toEntity() = User(
        id = this.id,
        name = this.name,
        email = this.email,
        password = this.password,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )

    companion object : DtoConverter<UserDto, User> {
        override fun toDto(entity: User) = UserDto(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt
        )
    }
}
