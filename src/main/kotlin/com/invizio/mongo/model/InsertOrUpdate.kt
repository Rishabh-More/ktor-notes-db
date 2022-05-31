package com.invizio.mongo.model

import kotlinx.serialization.Serializable

@Serializable
data class InsertOrUpdate(
    val title: String,
    val isDone: Boolean
)
