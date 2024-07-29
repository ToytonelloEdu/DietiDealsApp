package com.example.dietideals.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

val SlideIntoContainer :
        @JvmSuppressWildcards() AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
            null
}