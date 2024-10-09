package com.example.dietideals.domain.auxiliary

import android.net.Uri

data class ProfileForm(
    val bio: FormField<String?> = FormField("") {true},
    val nationality: FormField<String?> = FormField("") {true},
    val website: FormField<String?> = FormField("") {true},
    val instagram: FormField<String?> = FormField("") {true},
    val twitter: FormField<String?> = FormField("") {true},
    val facebook: FormField<String?> = FormField("") {true},
    var photo: Uri? = null
): Form {
    override val isValid: Boolean
        get() {
            return true
        }

    fun addSocial(selected: String, social: String): Boolean = when (selected) {
        "Instagram" -> {
            val valid = instagram.validator(social)
            if(valid) {
                instagram.value = social
            }
            valid
        }
        "Twitter" -> {
            val valid = twitter.validator(social)
            if(valid) {
                twitter.value = social
            }
            valid
        }
        "Facebook" -> {
            val valid = facebook.validator(social)
            if(valid) {
                facebook.value = social
            }
            valid
        }
        else -> false
    }
}
