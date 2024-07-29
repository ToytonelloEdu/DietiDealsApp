package com.example.dietideals.domain.models

abstract class User (
    open val username: String,
    open val email: String,
    open val password: String? = null,
    open val firstName: String,
    open val lastName: String,
    open val proPicPath: String? = null,
    open val bio: String? = null,
    open val nationality: String? = null,
) {
}