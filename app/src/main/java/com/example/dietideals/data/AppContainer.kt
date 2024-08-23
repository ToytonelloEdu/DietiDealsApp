package com.example.dietideals.data

import android.content.Context
import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.data.persistence.AppDatabase
import com.example.dietideals.data.repos.AuctionsRepository
import com.example.dietideals.data.repos.AuthRepository
import com.example.dietideals.data.repos.BidsRepository
import com.example.dietideals.data.repos.ImagesRepository
import com.example.dietideals.data.repos.NetworkAuctionsRepository
import com.example.dietideals.data.repos.NetworkAuthRepository
import com.example.dietideals.data.repos.NetworkBidsRepository
import com.example.dietideals.data.repos.NetworkImagesRepository
import com.example.dietideals.data.repos.NetworkStringsRepository
import com.example.dietideals.data.repos.NetworkTagsRepository
import com.example.dietideals.data.repos.NetworkUsersRepository
import com.example.dietideals.data.repos.OfflineAuctionsRepository
import com.example.dietideals.data.repos.OfflineUsersRepository
import com.example.dietideals.data.repos.StringsRepository
import com.example.dietideals.data.repos.TagsRepository
import com.example.dietideals.data.repos.UsersRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val auctionsRepository: AuctionsRepository
    val usersRepository: UsersRepository
    val authRepository: AuthRepository
    val tagsRepository: TagsRepository
    val imagesRepository: ImagesRepository
    val bidsRepository: BidsRepository
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

    override val auctionsRepository: AuctionsRepository by lazy {
        NetworkAuctionsRepository(retrofitService)
    }

    override val usersRepository: UsersRepository by lazy {
        NetworkUsersRepository(retrofitService)
    }

    override val authRepository: AuthRepository by lazy {
        NetworkAuthRepository(retrofitService)
    }

    override val tagsRepository: TagsRepository by lazy {
        NetworkTagsRepository(retrofitService)
    }
    override val imagesRepository: ImagesRepository by lazy {
        NetworkImagesRepository(retrofitService)
    }
    override val bidsRepository: BidsRepository by lazy {
        NetworkBidsRepository(retrofitService)
    }
}

class RoomAppContainer(
    context: Context
): AppContainer{

    private val database = AppDatabase.getDatabase(context)

    override val auctionsRepository: AuctionsRepository by lazy{
        OfflineAuctionsRepository(database.auctionDao())
    }
    override val usersRepository: UsersRepository by lazy {
        OfflineUsersRepository(database.ownUserDao())
    }
    override val authRepository: AuthRepository
        get() = throw NotImplementedError()

    override val tagsRepository: TagsRepository
        get() = throw NotImplementedError()

    override val imagesRepository: ImagesRepository
        get() = throw NotImplementedError()

    override val bidsRepository: BidsRepository
        get() = throw NotImplementedError()

}