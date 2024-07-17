package com.example.dietideals.ui.models

import com.example.dietideals.data.entities.NetTag

data class Tag (
    val tagName: String,
) {
    constructor(netTag: NetTag) : this(
        netTag.tagName,
    )

}