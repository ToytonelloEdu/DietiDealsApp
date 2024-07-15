package com.example.dietideals

import android.app.Application
import com.example.dietideals.data.AppContainer
import com.example.dietideals.data.RetrofitAppContainer

class DietiDealsApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = RetrofitAppContainer()
    }
}