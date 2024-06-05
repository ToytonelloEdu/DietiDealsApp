package com.example.dietideals.data.network

import com.example.dietideals.data.model.Auction
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL =
    //"http://192.168.43.8:8080/api/1.0/"
    //"http://100.102.11.112:8080/api/1.0/"
    //"http://192.168.0.109:8080/api/1.0/"
    "http://192.168.0.105:8080/api/1.0/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        Json.asConverterFactory("application/json".toMediaType())
    )
    .baseUrl(BASE_URL)
    .build()

interface NetworkApiService {
    @GET("myresource")
    suspend fun getIndexString() : String

    @GET("auctions")
    suspend fun getAuctions() : List<Auction>
}

object ServerAPI : DataSourceAPI {
    private val retrofitService : NetworkApiService by lazy {
        retrofit.create(NetworkApiService::class.java)
    }

    override suspend fun getString(): String {
        return retrofitService.getIndexString()
    }

    override suspend fun getAuctions(): List<Auction> {
        return retrofitService.getAuctions()
    }


}