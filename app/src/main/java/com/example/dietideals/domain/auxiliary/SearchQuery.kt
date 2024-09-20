package com.example.dietideals.domain.auxiliary

data class SearchQuery(
    val objectName: FormField<String> = FormField(""){ it.isNotBlank()},
    val vendor: FormField<String> = FormField(""){ it.isNotBlank()},
    val tags: FormField<MutableList<String>> = FormField(mutableListOf()){ it.isNotEmpty() && it.all { tag -> tag.isNotBlank() }},
): Form {

    override val isValid: Boolean
        get() {
            return objectName.isValid || vendor.isValid || tags.isValid
        }
}
