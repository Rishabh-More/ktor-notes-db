package com.invizio.mongo.repository

import com.invizio.mongo.common.Constants
import com.invizio.mongo.database.mysql.dao.UsersDao
import com.invizio.mongo.database.mysql.dao.toUser
import com.invizio.mongo.model.Login
import com.invizio.mongo.model.User
import java.util.regex.Pattern

class MySqlUserRepositoryImpl(val dao: UsersDao) : AuthRepository {

    override fun createUser(login: Login): Pair<Boolean, String?> {
        return when {
            //username cannot be blank value
            login.username.isBlank() -> Pair(false, "Username cannot be blank!")
            //Email cannot be blank, return false
            login.email.isBlank() -> Pair(false, "Email cannot be blank")
            //Email must be a valid email Pattern
            !Pattern.matches(Constants.EMAIL_PATTERN, login.email) ->
                Pair(false, "Please Enter a Valid email address")
            //Password can't be less than 6 characters
            login.password.length < 6 -> Pair(false, "Password must be Greater than 6 characters")
            //otherwise, all fields have been validated successfully, perform DB Insert operation
            else -> dao.insertUser(login)
        }
    }

    override fun validateUser(login: Login): Pair<Boolean, String> {
        return when {
            //username cannot be blank value
            login.username.isBlank() -> Pair(false, "Username cannot be blank!")
            //Email cannot be blank, return false
            login.email.isBlank() -> Pair(false, "Email cannot be blank")
            //Email must be a valid email Pattern
            !Pattern.matches(Constants.EMAIL_PATTERN, login.email) ->
                Pair(false, "Please Enter a Valid email address")
            //Password can't be less than 6 characters
            login.password.length < 6 -> Pair(false, "Password must be Greater than 6 characters")
            //Either Success with JWT token, or failure with message
            else -> dao.validateCredentials(login)
        }
    }

    override fun getUser(id: Int): User? {
        //Return a user object for the given user id
        // or null if user does not exist
        return dao.getUser(id).toUser()
    }
}