package com.invizio.mongo.repository

import com.invizio.mongo.model.Login
import com.invizio.mongo.model.User

interface AuthRepository {

    fun createUser(login: Login) : Pair<Boolean, String?>

    fun validateUser(login: Login) : Pair<Boolean, String>

    fun getUser(id: Int) : User?
}