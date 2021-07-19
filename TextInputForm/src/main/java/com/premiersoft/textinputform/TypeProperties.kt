package com.premiersoft.textinputform

data class TypeProperties(
    val name: String,
    val inputType: Int? = null,
    val allowedChars: String? = null,
    val maskPatterns: Map<Int, String>? = null,
    val maskPlaceholder: Char = '#'
)