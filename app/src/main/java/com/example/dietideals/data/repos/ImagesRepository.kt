package com.example.dietideals.data.repos

import android.util.Log
import com.example.dietideals.data.network.NetworkApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

interface ImagesRepository{
    suspend fun uploadProfileImage(username: String, image: File): Boolean
    suspend fun uploadAuctionImage(auctionId: Int, imageIndex: Int, image: File): Boolean
}

class NetworkImagesRepository(
    private val networkData: NetworkApiService
) : ImagesRepository {


    override suspend fun uploadProfileImage(username: String, image: File): Boolean {
        var res = false
        try {
            val multipartBody = MultipartBody.Part.createFormData(
                "file", image.name, image.asRequestBody()
            )

            Log.i("Upload", "Repository: $multipartBody")
            networkData.uploadProfileImage(
                username,
                multipartBody,
                image.asRequestBody("multipart/form-data".toMediaType())
            )
            res = true
        } catch (e: Exception) {
            Log.e("Upload", "Repository: ${e.message.toString()}")
        }

        return res
    }

    override suspend fun uploadAuctionImage(auctionId: Int, imageIndex: Int, image: File): Boolean {
        var res = false
        try {
            val multipartBody = MultipartBody.Part.createFormData(
                "file", image.name, image.asRequestBody()
            )
            networkData.uploadAuctionImage(
                auctionId,
                imageIndex,
                multipartBody,
                image.asRequestBody("multipart/form-data".toMediaType())
            )
            res = true
        } catch (e: Exception) {
            Log.e("Upload", "Repository: ${e.message.toString()}")
        }

        return res
    }

}