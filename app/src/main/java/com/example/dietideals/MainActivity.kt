package com.example.dietideals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dietideals.ui.AppScreen
import com.example.dietideals.ui.AppViewModel
import com.example.dietideals.ui.theme.DietiDealsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DietiDealsTheme {
                val viewModel: AppViewModel =
                    viewModel(factory = AppViewModel.Factory)
                AppScreen(viewModel)
            }
        }
    }
}