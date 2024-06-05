package com.example.dietideals.data.network

import com.example.dietideals.data.model.Auction

interface DataSourceAPI {
    suspend fun getString() : String
    suspend fun getAuctions() : List<Auction>
}