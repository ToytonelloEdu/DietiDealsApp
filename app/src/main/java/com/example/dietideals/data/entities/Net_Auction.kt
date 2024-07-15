package com.example.dietideals.data.entities

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Net_Auction(
    val id: Int,
    val objectName: String,
    val date: String,
    val bids: List<Net_Bid>,
    val tags: List<Net_Tag>
)