package com.invizio.mongo.model

import kotlinx.serialization.Serializable

@Serializable
class PackagedResponse<Type>(
    var status: Int,
    var message: String,
    var data: Type? = null
)