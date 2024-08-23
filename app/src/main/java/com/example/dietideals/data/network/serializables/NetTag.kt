package com.example.dietideals.data.network.serializables

import kotlinx.serialization.Serializable

@Serializable
data class NetTag (
    val id: Int? = null,
    val tagName: String,
)