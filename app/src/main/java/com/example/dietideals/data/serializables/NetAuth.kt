package com.example.dietideals.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class NetAuth(
    val handle: String,
    val password: String
)
