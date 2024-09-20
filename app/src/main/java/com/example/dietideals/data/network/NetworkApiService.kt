package com.example.dietideals.data.network

import com.example.dietideals.data.network.serializables.NetAuction
import com.example.dietideals.data.network.serializables.NetAuth
import com.example.dietideals.data.network.serializables.NetBid
import com.example.dietideals.data.network.serializables.NetTag
import com.example.dietideals.data.network.serializables.NetUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * **NetworkApiService** - Interface for Network API calls
 *
 * (Annotations are used by Retrofit to generate the necessary code)
 */
interface NetworkApiService {
    @GET("myresource")
    suspend fun getString() : String

    //AUCTIONS
    @GET("auctions")
    suspend fun getAuctions() : List<NetAuction>

    @GET("auctions")
    suspend fun getAuctionsQueried(
        @Query("object") objectName: String? = null,
        @Query("vendor") vendor: String? = null,
        @Query("tag1") tag1: String? = null,
        @Query("tag2") tag2: String? = null,
        @Query("tag3") tag3: String? = null,
    ) : List<NetAuction>

    @GET("auctions/{id}")
    suspend fun getAuctionById(@Path("id") id: Int) : NetAuction

    @POST("auctions")
    suspend fun postAuction(@Header("Authorization") token: String, @Body auction: NetAuction) : NetAuction

    @GET("users/{handle}/auctions")
    suspend fun getAuctionsByUser(@Path("handle") handle: String) : List<NetAuction>

    //USERS
    @GET("users/{handle}")
    suspend fun getUserByHandle(@Path("handle") handle: String) : NetUser

    //AUTH
    @POST("auth")
    suspend fun restLogin(@Body auth: NetAuth) : String //JWT Token

    @POST("signup")
    suspend fun restSignup(@Body user: NetUser) : NetUser

    //TAGS
    @GET("tags")
    suspend fun getTags() : List<NetTag>

    //BIDS
    @GET("users/{handle}/bids")
    suspend fun getBidsByUser(@Path("handle") handle: String) : List<NetBid>

    @POST("bids")
    suspend fun postBid(@Header("Authorization") token: String, @Body bid: NetBid) : NetBid


    //IMAGES
    @Multipart
    @POST("photos/{name}/profile")
    suspend fun uploadProfileImage(
        @Path("name") name: String,
        @Part image: MultipartBody.Part,
        @Part("details") details: RequestBody
    ) : ResponseBody

    @Multipart
    @POST("photos/auctions/{id}/photo/{index}")
    suspend fun uploadAuctionImage(
        @Path("id") id: Int,
        @Path("index") index: Int,
        @Part image: MultipartBody.Part,
        @Part("details") details: RequestBody
    ) : ResponseBody

}