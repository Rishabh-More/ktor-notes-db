package com.invizio.mongo.database.mysql.dao

import com.invizio.mongo.authorization.BCryptHasher
import com.invizio.mongo.authorization.JwtConfig
import com.invizio.mongo.authorization.JwtConfig.JwtUser
import com.invizio.mongo.common.getSystemTimeInstant
import com.invizio.mongo.database.mysql.DatabaseManager
import com.invizio.mongo.database.mysql.entities.UserEntity
import com.invizio.mongo.database.mysql.entities.UsersTable
import com.invizio.mongo.model.Login
import com.invizio.mongo.model.User
import org.ktorm.dsl.*
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf

class UsersDao(val jwtConfig: JwtConfig, val db: DatabaseManager) {

    fun insertUser(login: Login) : Pair<Boolean, String?> {
        //If Number of records for a username is more than 0, that means, a user with that name already exists
        val alreadyExists = db.ktormDb.from(UsersTable).select().where {
            UsersTable.name eq login.username
        }.totalRecords > 0
        //If User does not exist, then we create a new user
        return if (!alreadyExists) {
            val userId = db.ktormDb.insertAndGenerateKey(UsersTable) {
                set(UsersTable.name, login.username) //set the username
                set(UsersTable.email, login.email) //set the email
                //Insert a Hashed Password String instead of the actual password string in DB
                set(UsersTable.password, BCryptHasher.hashPassword(login.password))
                set(UsersTable.createdAt, getSystemTimeInstant()) //set the created at UTC date time
                set(UsersTable.updatedAt, getSystemTimeInstant()) //updated UTC date time is same as created
                set(UsersTable.deletedAt, null) //null for deleted at
            } as Int
            //We have successfully inserted a new user to our db
            Pair(true, "User was created successfully with user Id: $userId")
        } else Pair(false, "A user already exists with that User Name")
    }

    fun validateCredentials(login: Login) : Pair<Boolean, String> {
        val existingUser = db.ktormDb.sequenceOf(UsersTable).find {
            (it.name eq login.username) and (it.email eq login.email)
        }
        return if (existingUser != null) {
            //existing user was indeed found.
            // Time to validate the request's password with encrypted hash from Database

            when(BCryptHasher.validatePassword(login.password, existingUser.password)) {
                //If password is valid, then return success with JWT Token
                true -> Pair(true, jwtConfig.generateToken(JwtUser(existingUser.userId, existingUser.name)))
                //else passwords don't match. Return Invalid password error
                false -> Pair(false, "Incorrect Password. Please enter a valid Password")
            }
        } else Pair(false, "Invalid username or email! Please check again")
    }

    fun getUser(id: Int): UserEntity? {
        //Get the current user from db using the user id
        return db.ktormDb.sequenceOf(UsersTable).find { it.userId eq id }
    }
}

/**
 * Mapper function to return User from Entity object
 */
fun UserEntity?.toUser() : User? {
    return this?.let {
        User(
            id = userId,
            username = name,
            email = email,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString()
        )
    }
}

/**
 * Mapper function to return list of users from list of entity objects
 */
fun List<UserEntity>?.toUsers() : List<User> {
    return this?.map { it.toUser()!! } ?: emptyList()
}