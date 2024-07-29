package com.example.dietideals.domain.models

import com.example.dietideals.data.serializables.NetTag

data class Tag (
    val tagName: String,
) {
    constructor(netTag: NetTag) : this(
        netTag.tagName,
    )

}