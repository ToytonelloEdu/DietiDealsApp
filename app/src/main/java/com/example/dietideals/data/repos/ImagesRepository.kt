package com.example.dietideals.data.repos

import android.util.Log
import com.example.dietideals.data.network.NetworkApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface ImagesRepository{
    suspend fun uploadProfileImage(name: String, image: File): Boolean
}

class NetworkImagesRepository(
    private val networkData: NetworkApiService
) : ImagesRepository {


    override suspend fun uploadProfileImage(name: String, image: File): Boolean {
        var res = false
        try {
            networkData.uploadProfileImage(
                name, MultipartBody.Part.createFormData(
                    "image", image.name, image.asRequestBody()
                )
            ).enqueue(
                object : Callback<ResponseBody> {
                    override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                        Log.e("Upload Error", p1.message.toString())
                    }

                    override fun onResponse(p0: Call<ResponseBody>, p1: Response<ResponseBody>) {
                        val body = p1.body()
                        Log.i("Upload Success", body.toString())
                    }

                }
            )
            res = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return res
    }
}