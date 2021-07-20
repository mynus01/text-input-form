package com.premiersoft.textinputform

data class TypeProperties(
    val name: String,
    val inputType: Int? = null,
    val allowedChars: String? = null,
    val maskPatterns: List<String>? = null,
    val maskPlaceholder: Char = '#',
    var delimiters: List<Char>? = null
)