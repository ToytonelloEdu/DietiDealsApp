package com.example.dietideals.data.network

import com.example.dietideals.data.entities.NetAuction
import com.example.dietideals.data.entities.NetTag
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * **NetworkApiService** - Interface for Network API calls
 *
 * (Annotations are used by Retrofit to generate the necessary code)
 */
interface NetworkApiService {
    @GET("myresource")
    suspend fun getString() : String

    @GET("auctions")
    suspend fun getAuctions() : List<NetAuction>

    @GET("auctions/{id}")
    suspend fun getAuctionById(@Path("id") id: Int) : NetAuction

    @GET("tags")
    suspend fun getTags() : List<NetTag>

}