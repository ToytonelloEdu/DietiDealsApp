package com.example.dietideals.ui.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag (
    val id: String,
    @SerialName(value = "tagname")
    val tagName: String,
)