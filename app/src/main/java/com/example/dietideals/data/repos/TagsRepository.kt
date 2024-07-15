package com.example.dietideals.data.repos

import com.example.dietideals.data.entities.Net_Tag
import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.ui.models.Tag

interface TagsRepository {
    suspend fun getTags(): List<Tag>
}

class NetworkTagsRepository(
    private val networkData: NetworkApiService
) : TagsRepository {
    override suspend fun getTags(): List<Tag> {
        return networkData.getTags().map {
            Tag(it.tagName)
        }
    }

}