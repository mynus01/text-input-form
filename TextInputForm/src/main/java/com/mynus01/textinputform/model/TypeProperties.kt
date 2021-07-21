package com.mynus01.textinputform.model

data class TypeProperties(
    val name: String,
    val inputType: Int? = null,
    val allowedChars: String? = null,
    val maskPattern: String? = null,
    val maskPlaceholder: Char = '#',
    var delimiters: List<Char>? = null
)