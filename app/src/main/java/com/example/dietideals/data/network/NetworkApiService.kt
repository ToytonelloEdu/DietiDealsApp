package com.example.dietideals.data.network

import com.example.dietideals.data.entities.Net_Auction
import com.example.dietideals.data.entities.Net_Tag
import com.example.dietideals.ui.models.Auction
import retrofit2.http.GET

/**
 * **NetworkApiService** - Interface for Network API calls
 *
 * (Annotations are used by Retrofit to generate the necessary code)
 */
interface NetworkApiService {
    @GET("myresource")
    suspend fun getString() : String

    @GET("auctions")
    suspend fun getAuctions() : List<Net_Auction>

    @GET("tags")
    suspend fun getTags() : List<Net_Tag>

}