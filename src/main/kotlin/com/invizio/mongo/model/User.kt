package com.invizio.mongo.model

import kotlinx.serialization.Serializable

/**
 * This User Model will be later exposed to the client.
 *
 * An exposed model will expose it's fields so that's why we don't
 * save password here (even if we receive it encrypted from the DB)
 */
@Serializable
data class User(
    val id: Int,
    val username: String?,
    val email: String?,
    val createdAt: String?,
    val updatedAt: String?
)
