package com.example.dietideals

import com.example.dietideals.data.RetrofitAppContainer
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuctionFetchTest {
    val auctionRepo = RetrofitAppContainer().auctionsRepository;

    @Test
    fun auctionFetchTest() {
        runTest {
            val auctions = auctionRepo.getAuctions()
            assert(auctions.isNotEmpty())
        }
    }
}