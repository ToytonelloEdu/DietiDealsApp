package com.example.dietideals.domain.auxiliary

import java.util.Date


interface Form {
    val isValid: Boolean
}

class FormField<T> (
    initialValue: T,
    internal val validator: (value: T) -> Boolean,
) {
    var value = initialValue;

    val isValid: Boolean
        get() {
            return try {
                validator(value)
            } catch (e: IllegalArgumentException) {
                false
            }

        }

    val message: String
        get() {
            return try {
                validator(value)
                "Valid"
            } catch (e: IllegalArgumentException) {
                e.message ?: "Invalid"
            }
        }
}

fun FormField<Date>.validator(): (value: Date) -> Boolean {
    return validator
}
