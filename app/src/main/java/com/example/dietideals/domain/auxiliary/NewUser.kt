package com.example.dietideals.domain.auxiliary

import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.User
import java.sql.Timestamp
import java.util.Date

sealed interface Gender {
    data object Male : Gender
    data object Female : Gender
    data class Other(val other: String) : Gender

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

data class NewUser(
    var username: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var gender: Gender = Gender.Male,
    var birthdate: Date = Date(),
    var userType: UserType = UserType.Auctioneer,
    var email: String = "",
    var password: String = "",
    var passwordConfirm: String = "",
) {
    fun toUser(): User {
        return when (userType) {
            UserType.Auctioneer -> {
                Auctioneer(
                    username = username,
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    proPicPath = null,
                    bio = null,
                    nationality = null,
                    gender = gender.name,
                    birthdate = Timestamp(birthdate.time),
                    auctions = mutableListOf()
                )
            }
            UserType.Buyer -> {
                Buyer(
                    username = username,
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    proPicPath = null,
                    bio = null,
                    nationality = null,
                    gender = gender.name,
                    birthdate = Timestamp(birthdate.time),
                    bids = emptyList()
                )
            }
        }
    }
}