package com.example.dietideals

import com.example.dietideals.data.RetrofitAppContainer
import com.example.dietideals.data.repos.NetworkTagsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TagFetchTest {

    val tagsRepo = RetrofitAppContainer().tagsRepository;

    @Test
    fun fetchTags() {
        runTest {
            tagsRepo.getTags().forEach {
                println(it.tagName)
            }
        }
    }
}