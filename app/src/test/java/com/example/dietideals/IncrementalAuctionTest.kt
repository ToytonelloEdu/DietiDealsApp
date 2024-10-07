package com.example.dietideals

import com.example.dietideals.domain.models.Bid
import com.example.dietideals.domain.models.IncrementalAuction
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Test
import java.sql.Timestamp

class IncrementalAuctionTest {

    private val incrementalAuction = IncrementalAuction(
        pictures = emptyList(),
        objectName = "Computer",
        description = "Un nuovo portatile",
        date = Timestamp.valueOf("2024-09-14 13:00:00"),
        bids = mutableListOf(
            Bid(amount = 420.00, time = Timestamp.valueOf("2024-09-14 13:14:00")),
            Bid(amount = 440.00, time = Timestamp.valueOf("2024-09-14 13:26:00"))
        ),
        lastBid = Bid(amount = 440.00, time = Timestamp.valueOf("2024-09-14 13:26:00")),
        tags = emptyList(),
        timeInterval = 1800,
        startingPrice = 400.00,
        raisingThreshold = 20.00
    )

    private val incrementalAuction2 = IncrementalAuction(
        pictures = emptyList(),
        objectName = "Televisore",
        description = "Una smart TV",
        date = Timestamp.valueOf("2024-09-27 15:00:00"),
        bids = mutableListOf(
            Bid(amount = 310.00, time = Timestamp.valueOf("2024-09-27 15:42:21")),
            Bid(amount = 320.00, time = Timestamp.valueOf("2024-09-27 16:20:00")),
            Bid(amount = 330.00, time = Timestamp.valueOf("2024-09-27 16:53:10"))
        ),
        lastBid = null,
        tags = emptyList(),
        timeInterval = 3600,
        startingPrice = 300.00,
        raisingThreshold = 10.00
    )

    private val incrementalAuction3 = IncrementalAuction(
        pictures = emptyList(),
        objectName = "Cellulare",
        description = "Un nuovo telefono samsung",
        date = Timestamp.valueOf("2024-10-06 12:30:00"),
        bids = mutableListOf(
            Bid(amount = 630.00, time = Timestamp.valueOf("2024-10-06 12:58:00")),
            Bid(amount = 6600.00, time = Timestamp.valueOf("2024-10-06 13:24:11"))
        ),
        lastBid = Bid(amount = 660.00, time = Timestamp.valueOf("2024-10-06 13:24:11")),
        tags = emptyList(),
        timeInterval = 1800,
        startingPrice = 600.00,
        raisingThreshold = 30.00
    )

    private val incrementalAuction4 = IncrementalAuction(
        pictures = emptyList(),
        objectName = "Telecomando",
        description = "Telecomando per TV",
        date = Timestamp.valueOf("2024-10-08 18:00:00"),
        bids = mutableListOf(
            Bid(amount = 12.00, time = Timestamp.valueOf("2024-10-08 18:33:21")),
            Bid(amount = 14.00, time = Timestamp.valueOf("2024-10-08 18:45:00"))
        ),
        lastBid = null,
        tags = emptyList(),
        timeInterval = 2700,
        startingPrice = 10.00,
        raisingThreshold = 2.00
    )

    @Test
    fun testHasBeenOverFor_0DaysAuctionOver(){
        assertTrue(incrementalAuction.hasBeenOverFor(0))
    }

    @Test
    fun testHasBeenOverFor_14DaysAuctionOver(){
        assertTrue(incrementalAuction.hasBeenOverFor(14))
    }

    @Test
    fun testHasBeenOverFor_7DaysAuctionOver(){
        assertTrue(incrementalAuction2.hasBeenOverFor(7))
    }

    @Test
    fun testHasBeenOverFor_5DaysAuctionIsNotOver(){
        assertFalse("L'asta è già terminata", incrementalAuction3.hasBeenOverFor(5))
    }

    @Test
    fun testHasBeenOverFor_10DaysAuctionIsNotOverNullLastBid(){
        assertFalse("L'asta è già terminata", incrementalAuction4.hasBeenOverFor(10))
    }

    @Test
    fun testHasBeenOverFor_18Days_throwsException(){
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            incrementalAuction.hasBeenOverFor(15)
        }
    }

    @Test
    fun testHasBeenOverFor_NegativeDays_throwsException(){
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            incrementalAuction3.hasBeenOverFor(-1)
        }
    }

}