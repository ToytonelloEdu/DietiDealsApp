package com.example.dietideals

import com.example.dietideals.data.RetrofitAppContainer
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuctionFetchTest {
    private val auctionRepo = RetrofitAppContainer().auctionsRepository;

    @Test
    fun auctionFetchTest() {
        runTest {
            val auction = auctionRepo.getAuctionById(20)
        }
    }
}