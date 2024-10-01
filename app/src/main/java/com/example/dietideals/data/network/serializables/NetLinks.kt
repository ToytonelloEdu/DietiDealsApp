package com.example.dietideals.data.network.serializables

import com.example.dietideals.domain.models.Links
import kotlinx.serialization.Serializable

@Serializable
data class NetLinks(
    val website: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val facebook: String? = null
) {
    fun toLinks() = Links(
        website,
        instagram,
        twitter,
        facebook
    )
}
