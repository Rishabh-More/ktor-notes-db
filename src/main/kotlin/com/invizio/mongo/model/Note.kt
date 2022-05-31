package com.invizio.mongo.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Long,
    var title: String?,
    var done: Boolean?
)
