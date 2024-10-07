package com.example.dietideals.domain.models

import com.example.dietideals.data.network.serializables.NetLinks

data class Links(
    val website: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val facebook: String? = null
) {
    fun toNetLinks() = NetLinks(
        //null,
        website,
        instagram,
        twitter,
        facebook
    )

    fun toLinksMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        if(website != null) map["website"] = website
        if(instagram != null) map["instagram"] = instagram
        if(twitter != null) map["twitter"] = twitter
        if(facebook != null) map["facebook"] = facebook

        return map
    }
}
