package com.premiersoft.textinputform

import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import com.google.android.material.textfield.TextInputLayout

data class FormField(
    val layout: TextInputLayout,
    val type: FieldType = FieldType.CUSTOM,
    var typeProperties: TypeProperties? = null,
    val isRequired: Boolean = true,
    var minLength: Int? = null,
    var maxLength: Int? = null,
    var isOk: Boolean = false
) {
    init {
        when (type) {
            FieldType.TEXT -> {
                typeProperties = TypeProperties(
                    "Campo", InputType.TYPE_CLASS_TEXT,
                    layout.context.getString(R.string.letters_accentuation_and_numbers_allowed)
                )
            }
            FieldType.TEXT_ONLY -> {
                typeProperties = TypeProperties(
                    "Campo", InputType.TYPE_CLASS_TEXT,
                    layout.context.getString(R.string.letters_and_accentuation_allowed)
                )
            }
            FieldType.EMAIL -> {
                typeProperties = TypeProperties(
                    "E-mail", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                    layout.context.getString(R.string.only_numbers_allowed)
                )
            }
            FieldType.DATE -> {
                minLength = 8
                maxLength = 10

                typeProperties = TypeProperties(
                    "Data", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.characters_to_email_allowed), mapOf(Pair(8, "#/#/####"), Pair(9, "#/##/####"), Pair(10, "##/##/####"))
                )
            }
            FieldType.CELLPHONE -> {
                minLength = 14
                maxLength = 15

                typeProperties = TypeProperties(
                    "Celular", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.only_numbers_allowed), mapOf(Pair(14, "(##) ####-####"), Pair(15, "(##) #####-####"))
                )
            }
            FieldType.PHONE -> {
                minLength = 14
                maxLength = 14

                typeProperties = TypeProperties(
                    "Telefone", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.only_numbers_allowed),  mapOf(Pair(14, "(##) ####-####"))
                )
            }
            FieldType.CPF -> {
                minLength = 14
                maxLength = 14

                typeProperties = TypeProperties(
                    "CPF", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.only_numbers_allowed), mapOf(Pair(14, "###.###.###-##"))
                )
            }
            FieldType.CNPJ -> {
                minLength = 18
                maxLength = 18

                typeProperties = TypeProperties(
                    "CNPJ", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.only_numbers_allowed), mapOf(Pair(18, "##.###.###/####-##"))
                )
            }
            FieldType.CUSTOM -> {
                if (typeProperties == null) {
                    typeProperties = TypeProperties("Campo")
                }
            }
        }

        typeProperties?.apply {
            maskPatterns?.let { pattern ->
                layout.editText?.mask(pattern, minLength!!, maskPlaceholder)
            }
            allowedChars?.let { chars ->
                layout.editText?.keyListener = DigitsKeyListener.getInstance(chars)
            }
            inputType?.let { type ->
                layout.editText?.inputType = type
            }
        }

        maxLength?.let { max ->
            layout.editText?.let { edit ->
                edit.filters += InputFilter.LengthFilter(max)
            }
        }

        if (!isRequired) {
            isOk = true
        }
    }
}