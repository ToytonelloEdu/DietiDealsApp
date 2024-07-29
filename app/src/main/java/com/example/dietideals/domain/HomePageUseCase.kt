package com.example.dietideals.domain

import com.example.dietideals.data.repos.AuctionsRepository
import com.example.dietideals.data.repos.NetworkAuctionsRepository
import com.example.dietideals.data.repos.OfflineAuctionsRepository

class HomePageUseCase (
    private val onlineAuctionsRepository: AuctionsRepository,
    private val offlineAuctionsRepository: AuctionsRepository
) {

}