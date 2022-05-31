package com.invizio.mongo.model

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val username: String,
    val email: String,
    val password: String
)
