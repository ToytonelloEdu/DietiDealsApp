package com.example.dietideals

import com.example.dietideals.domain.models.SilentAuction
import org.junit.Assert.*
import org.junit.Test
import java.sql.Timestamp

class SilentAuctionTest {

    private val silentAuction = SilentAuction(
        objectName = "Penna",
        description = "Una penna blu",
        date = Timestamp.valueOf("2024-09-01 10:00:00"),
        bids = mutableListOf(),
        tags = emptyList(),
        expirationDate = Timestamp.valueOf("2024-09-14 13:00:00"))

    private val silentAuction2 = SilentAuction(
        objectName = "Matita",
        description = "Una nuova matita",
        date = Timestamp.valueOf("2024-08-15 15:00:00"),
        bids = mutableListOf(),
        tags = emptyList(),
        expirationDate = Timestamp.valueOf("2024-09-10 10:22:00"))

    private val silentAuction3 = SilentAuction(
        objectName = "Astuccio",
        description = "Un astuccio per la penna e la matita",
        date = Timestamp.valueOf("2025-02-15 17:30:00"),
        bids = mutableListOf(),
        tags = emptyList(),
        expirationDate = Timestamp.valueOf("2025-02-28 15:43:05"))

    @Test
    fun testHasBeenOverFor_0DaysSilentAuctionOver() {
        assertTrue(silentAuction.hasBeenOverFor(0))
    }

    @Test
    fun testHasBeenOverFor_14DaysSilentAuctionOver(){
        assertTrue(silentAuction2.hasBeenOverFor(14))
    }

    @Test
    fun testHasBeenOverFor_8DaysSilentAuctionOver(){
        assertTrue(silentAuction2.hasBeenOverFor(8))
    }

    @Test
    fun testHasBeenOverFor_3DaysSilentAuctionOver(){
        assertTrue(silentAuction.hasBeenOverFor(3))
    }

    @Test
    fun testHasBeenOverFor_5DaysSilentAuctionNotOver(){
        assertFalse("L'asta è già terminata", silentAuction3.hasBeenOverFor(5))
    }

    @Test
    fun testHasBeenOverFor_18Days_throwsException(){
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            silentAuction2.hasBeenOverFor(18)
        }
    }

    @Test
    fun testHasBeenOverFor_NegativeDays_throwsException(){
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            silentAuction.hasBeenOverFor(-2)
        }
    }
}