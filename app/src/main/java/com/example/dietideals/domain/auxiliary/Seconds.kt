package com.example.dietideals.domain.auxiliary

class Seconds (
    private val seconds: Int
) {

    override fun toString(): String {
        if (seconds > 0) {
            val minutes: Int = seconds / 60
            return when (val seconds: Int = seconds % 60) {
                in 0..9 -> "$minutes:0$seconds"
                else -> "$minutes:$seconds"
            }
        }
        return "00:00"
    }

    fun isTimeRemaining(): Boolean {
        return seconds > 0
    }

}