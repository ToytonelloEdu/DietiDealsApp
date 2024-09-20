package com.example.dietideals

import android.app.Application
import com.example.dietideals.data.AppContainer
import com.example.dietideals.data.RetrofitAppContainer
import com.example.dietideals.data.RoomAppContainer

class DietiDealsApplication : Application() {
    lateinit var container: AppContainer
    lateinit var offlineContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = RetrofitAppContainer(this)
        offlineContainer = RoomAppContainer(this)
    }
}