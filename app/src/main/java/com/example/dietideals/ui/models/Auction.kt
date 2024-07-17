package com.example.dietideals.ui.models

import java.sql.Timestamp

abstract class Auction(
    open val id: Int? = null,
    open val picturePath: String? = null,
    open val objectName: String,
    open val description: String,
    open val auctioneer: Auctioneer? = null,
    open val auctioneerUsername: String? = null,
    open val date: Timestamp,
    open val bids: MutableList<Bid>,
    open val tags: List<Tag>
)

