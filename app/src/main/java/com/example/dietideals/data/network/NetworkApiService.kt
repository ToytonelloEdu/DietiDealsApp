package com.example.dietideals.data.network

import com.example.dietideals.data.serializables.NetAuction
import com.example.dietideals.data.serializables.NetAuth
import com.example.dietideals.data.serializables.NetTag
import com.example.dietideals.data.serializables.NetUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * **NetworkApiService** - Interface for Network API calls
 *
 * (Annotations are used by Retrofit to generate the necessary code)
 */
interface NetworkApiService {
    @GET("myresource")
    suspend fun getString() : String

    //auctions
    @GET("auctions")
    suspend fun getAuctions() : List<NetAuction>

    @GET("auctions/{id}")
    suspend fun getAuctionById(@Path("id") id: Int) : NetAuction

    @POST("auctions")
    @Headers("Bearer : \$token")
    suspend fun postAuction(@Body auction: NetAuction) : NetAuction

    //users
    @GET("users/{handle}")
    suspend fun getUserByHandle(@Path("handle") handle: String) : NetUser

    //auth
    @POST("auth")
    suspend fun restLogin(@Body auth: NetAuth) : String //JWT Token

    @POST("signup")
    suspend fun restSignup(@Body user: NetUser) : NetUser

    //tags
    @GET("tags")
    suspend fun getTags() : List<NetTag>

}