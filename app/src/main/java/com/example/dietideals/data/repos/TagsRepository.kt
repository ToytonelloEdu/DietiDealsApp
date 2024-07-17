package com.example.dietideals.data.repos

import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.ui.models.Tag

interface TagsRepository {
    suspend fun getTags(): List<Tag>
}

class NetworkTagsRepository(
    private val networkApi: NetworkApiService
) : TagsRepository {
    override suspend fun getTags(): List<Tag> = networkApi.getTags().map { Tag(it) }
}