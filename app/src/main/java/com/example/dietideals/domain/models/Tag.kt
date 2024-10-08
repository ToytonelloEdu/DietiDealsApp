package com.example.dietideals.domain.models

import com.example.dietideals.data.network.serializables.NetTag

data class Tag (
    val tagName: String,
) {
    constructor(netTag: NetTag) : this(
        netTag.tagName,
    )

    fun toNetTag() : NetTag {
        return NetTag(
            tagName = tagName,
        )
    }

}