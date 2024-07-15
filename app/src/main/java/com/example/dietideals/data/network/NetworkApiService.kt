package com.example.dietideals.data.network

import com.example.dietideals.ui.models.Auction
import com.example.dietideals.ui.models.Tag
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
    suspend fun getAuctions() : List<Auction>

    @GET("auctions/{id}")
    suspend fun getAuctionById(@Path("id") id: Int) : Auction

    @GET("tags")
    suspend fun getTags() : List<Tag>

}