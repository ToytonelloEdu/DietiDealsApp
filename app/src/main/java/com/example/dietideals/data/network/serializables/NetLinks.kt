package com.example.dietideals.data.network.serializables

import android.util.Log
import com.example.dietideals.domain.models.Links
import kotlinx.serialization.Serializable

@Serializable
data class NetLinks(
    //val user: NetUser? = null,
    val website: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val facebook: String? = null
) {
    fun toLinks(): Links {
        Log.d("NetLinks", this.toString())
        return Links(
            website,
            instagram,
            twitter,
            facebook
        )
    }
}
