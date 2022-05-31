package com.invizio.mongo.database.mysql.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.Instant

/**
 * Provide the actual table name as that in MySql database/schema
 */
object UsersTable : Table<UserEntity>("users") {

    //Define the MySql table columns here, along with their type
    // (as type defined in the MySql table)
    val userId = int("userId").primaryKey().bindTo { it.userId }
    val name = varchar("name").bindTo { it.name }
    val email = varchar("email").bindTo { it.email }
    val password = text("password").bindTo { it.password }
    val createdAt = timestamp("createdAt").bindTo { it.createdAt }
    val updatedAt = timestamp("updatedAt").bindTo { it.updatedAt }
    val deletedAt = timestamp("deletedAt").bindTo { it.deletedAt }
}

interface UserEntity: Entity<UserEntity> {

    companion object : Entity.Factory<UserEntity>()

    val userId: Int
    var name: String
    var email: String
    var password: String
    var createdAt: Instant?
    var updatedAt: Instant?
    var deletedAt: Instant?
}