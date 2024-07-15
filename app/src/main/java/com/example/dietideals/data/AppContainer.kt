package com.example.dietideals.data

import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.data.repos.AuctionsRepository
import com.example.dietideals.data.repos.NetworkAuctionsRepository
import com.example.dietideals.data.repos.NetworkStringsRepository
import com.example.dietideals.data.repos.NetworkTagsRepository
import com.example.dietideals.data.repos.StringsRepository
import com.example.dietideals.data.repos.TagsRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val stringsRepository: StringsRepository
    val auctionsRepository: AuctionsRepository
    val tagsRepository: TagsRepository
}

class RetrofitAppContainer : AppContainer {

    private val baseUrl =
    //"http://192.168.43.8:8080/api/1.0/"
    //"http://100.102.11.112:8080/api/1.0/"
    //"http://192.168.0.109:8080/api/1.0/"
    "http://192.168.0.105:8080/api/1.0/"
    //"http://192.168.0.101:8080/api/1.0/"
    //"http://192.168.42.82:8080/api/1.0/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            Json.asConverterFactory("application/json; charset=UTF8".toMediaType())
        )
        .baseUrl(baseUrl)
        .build()

    private val retrofitService : NetworkApiService by lazy {
        retrofit.create(NetworkApiService::class.java)
    }

    //Repositories

    override val stringsRepository: StringsRepository by lazy {
        NetworkStringsRepository(retrofitService)
    }

    override val auctionsRepository: AuctionsRepository by lazy {
        NetworkAuctionsRepository(retrofitService)
    }

    override val tagsRepository: TagsRepository by lazy {
        NetworkTagsRepository(retrofitService)
    }


}