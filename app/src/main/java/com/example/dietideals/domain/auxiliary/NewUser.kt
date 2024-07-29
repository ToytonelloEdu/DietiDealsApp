package com.example.dietideals.domain.auxiliary

import java.util.Date

sealed interface Gender {
    data object Male : Gender
    data object Female : Gender
    data class Other(val other: String) : Gender
}

sealed interface UserType {
    data object Buyer : UserType
    data object Auctioneer : UserType

    val name: String
        get() = when (this) {
            UserType.Buyer -> "Buyer"
            UserType.Auctioneer -> "Vendor"
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
)