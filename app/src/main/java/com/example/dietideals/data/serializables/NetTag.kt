package com.example.dietideals.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class NetTag (
    val id: Int? = null,
    val tagName: String,
)