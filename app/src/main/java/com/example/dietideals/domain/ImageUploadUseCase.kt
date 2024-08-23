package com.example.dietideals.domain

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.dietideals.data.repos.ImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class ImageUploadUseCase(
    private val imagesRepository: ImagesRepository
) {
    suspend fun uploadProfileImage(context: Context, username: String, uri: Uri) {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
            uri, "r", null
        ) ?: throw IllegalArgumentException("Invalid file descriptor")

        withContext(Dispatchers.IO) {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(context.cacheDir, context.contentResolver.getFileName(uri))
            val outputStream = file.outputStream()
            inputStream.copyTo(outputStream)

            imagesRepository.uploadProfileImage(username, file)
        }

        parcelFileDescriptor.close()
    }

    suspend fun uploadAuctionImage(context: Context, auctionId: Int, imageIndex: Int, uri: Uri) {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
            uri, "r", null
        ) ?: throw IllegalArgumentException("Invalid file descriptor")

        withContext(Dispatchers.IO) {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(context.cacheDir, context.contentResolver.getFileName(uri))
            val outputStream = file.outputStream()
            inputStream.copyTo(outputStream)

            imagesRepository.uploadAuctionImage(auctionId, imageIndex, file)
        }

        parcelFileDescriptor.close()
    }
}

private fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = it.getString(it.getColumnIndexOrThrow("_display_name"))
    }
    return name
}