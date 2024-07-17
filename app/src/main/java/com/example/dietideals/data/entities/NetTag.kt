package com.example.dietideals.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetTag (
    val id: Int? = null,
    val tagName: String,
)