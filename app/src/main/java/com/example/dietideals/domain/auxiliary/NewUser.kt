package com.example.dietideals.domain.auxiliary

import android.util.Patterns
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.User
import java.sql.Timestamp
import java.util.Date

sealed interface Gender {
    data object Male : Gender
    data object Female : Gender
    data class Other(val other: String = "Other") : Gender

    val name: String
        get() = when (this) {
            Male -> "Male"
            Female -> "Female"
            is Other -> "Other" //TODO: Manage case Other
        }
}

sealed interface UserType {
    data object Buyer : UserType
    data object Auctioneer : UserType

    val name: String
        get() = when (this) {
            Buyer -> "Buyer"
            Auctioneer -> "Vendor"
        }
}

internal val eighteenYrs: Long = 60 * 60 * 24 * 365 * 18

data class NewUser(
    var username: FormField<String> =
        FormField("") {
            if(it.isBlank()) throw IllegalArgumentException("Username cannot be blank")
            if(it.length < 3) throw IllegalArgumentException("Username must be at least 3 characters long")
            if(it.length > 50) throw IllegalArgumentException("Username must be at most 50 characters long")
            true
        } ,
    var firstName: FormField<String> =
        FormField("") {
            if(it.isBlank()) throw IllegalArgumentException("First name cannot be blank")
            if(it.length > 20) throw IllegalArgumentException("First name must be at most 20 characters long")
            true
        },
    var lastName: FormField<String> =
        FormField("") {
            if(it.isBlank()) throw IllegalArgumentException("First name cannot be blank")
            if(it.length > 20) throw IllegalArgumentException("First name must be at most 20 characters long")
            true
        },
    var gender: FormField<Gender> = FormField(Gender.Male) {true},
    var birthdate: FormField<Date> =
        FormField(Date(System.currentTimeMillis() - eighteenYrs*1000)) {
            if((it.time + eighteenYrs*1000) > System.currentTimeMillis())
                throw IllegalArgumentException("You must be at least 18 years old to register")
            true
        },
    var userType: FormField<UserType> = FormField(UserType.Auctioneer) {true},
    var email: FormField<String> = FormField("") {
        if(it.isBlank()) throw IllegalArgumentException("Email cannot be blank")
        if(it.length > 50) throw IllegalArgumentException("Email must be at most 50 characters long")
        if(!Patterns.EMAIL_ADDRESS.matcher(it).matches()) throw IllegalArgumentException("Invalid email")
        true
    },
    var password: FormField<String> = FormField("") { pw ->
        if(pw.isBlank()) throw IllegalArgumentException("Password cannot be blank")
        if(pw.length < 8) throw IllegalArgumentException("Password must be at least 8 characters long")
        if(pw.length > 20) throw IllegalArgumentException("Password must be at most 20 characters long")
        if(!pw.any { it.isUpperCase() }) throw IllegalArgumentException("Password must contain at least one uppercase letter")
        if(!pw.any { it.isLowerCase() }) throw IllegalArgumentException("Password must contain at least one lowercase letter")
        if(!pw.any { it.isDigit() }) throw IllegalArgumentException("Password must contain at least one digit")
        if(!pw.any { !it.isLetterOrDigit() }) throw IllegalArgumentException("Password must contain at least one special character")
        true
    },
    var passwordConfirm: FormField<String> = FormField("") {
        if(it.isBlank()) throw IllegalArgumentException("Password confirmation cannot be blank")
        if(it != password.value) throw IllegalArgumentException("Passwords do not match")
        true
    },
): Form {
    fun toUser(): User {
        return when (userType.value) {
            UserType.Auctioneer -> {
                Auctioneer(
                    username = username.value,
                    email = email.value,
                    password = password.value,
                    firstName = firstName.value,
                    lastName = lastName.value,
                    proPicPath = null,
                    bio = null,
                    nationality = null,
                    gender = gender.value.name,
                    birthdate = Timestamp(birthdate.value.time),
                    auctions = mutableListOf()
                )
            }
            UserType.Buyer -> {
                Buyer(
                    username = username.value,
                    email = email.value,
                    password = password.value,
                    firstName = firstName.value,
                    lastName = lastName.value,
                    proPicPath = null,
                    bio = null,
                    nationality = null,
                    gender = gender.value.name,
                    birthdate = Timestamp(birthdate.value.time),
                    bids = emptyList()
                )
            }
        }
    }

    val firstHalfValid: Boolean
        get() {
            return username.isValid &&
                firstName.isValid &&
                lastName.isValid &&
                gender.isValid &&
                birthdate.isValid &&
                userType.isValid
        }

    val secondHalfValid: Boolean
        get() {
            return email.isValid &&
                    password.isValid &&
                    passwordConfirm.isValid
        }

    override val isValid: Boolean
        get() {
            return username.isValid &&
                firstName.isValid &&
                lastName.isValid &&
                gender.isValid &&
                birthdate.isValid &&
                userType.isValid &&
                email.isValid &&
                password.isValid &&
                passwordConfirm.isValid
        }
}