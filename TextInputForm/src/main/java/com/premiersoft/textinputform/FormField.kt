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
                    layout.context.getString(R.string.characters_to_email_allowed)
                )
            }
            FieldType.DATE -> {
                minLength = 8
                maxLength = 10

                typeProperties = TypeProperties(
                    "Data", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.only_numbers_allowed), "##/##/####"
                )
            }
            FieldType.CELLPHONE -> {
                minLength = 14
                maxLength = 16

                typeProperties = TypeProperties(
                    "Celular", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.only_numbers_allowed), "(##) #####--####"
                )
            }
            FieldType.PHONE -> {
                minLength = 14
                maxLength = 14

                typeProperties = TypeProperties(
                    "Telefone", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.only_numbers_allowed), "(##) ####-####"
                )
            }
            FieldType.CPF -> {
                minLength = 14
                maxLength = 14

                typeProperties = TypeProperties(
                    "CPF", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.only_numbers_allowed), "###.###.###-##"
                )
            }
            FieldType.CNPJ -> {
                minLength = 18
                maxLength = 18

                typeProperties = TypeProperties(
                    "CNPJ", InputType.TYPE_CLASS_NUMBER,
                    layout.context.getString(R.string.only_numbers_allowed), "##.###.###/####-##"
                )
            }
            FieldType.CUSTOM -> {
                if (typeProperties == null) {
                    typeProperties = TypeProperties("Campo")
                }
            }
        }

        typeProperties?.apply {
            maskPattern?.let { pattern ->
                delimiters = getDelimiters(listOf(pattern), maskPlaceholder)
                layout.editText?.mask(pattern, maxLength, maskPlaceholder, delimiters)
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

    private fun getDelimiters(patterns: List<String>, placeholder: Char): List<Char> {
        val delimiters = mutableListOf<Char>()

        for (p in patterns) {
            for (c in p) {
                if (c != placeholder && !delimiters.contains(c)) {
                    delimiters.add(c)
                }
            }
        }

        return delimiters
    }

    /**
     * removes the mask of the extracted text from the TextInputLayout
     * and returns it;
     *
     * in case you want the text with the mask, see [getText];
     *
     * @returns the unmasked text.
     */
    fun getValue(): String {
        var text = layout.editText?.text.toString()
        val delimiters = typeProperties?.delimiters ?: emptyList()

        for (c in delimiters) {
           text = text.replace(c.toString(), "")
        }

        return text
    }

    /**
     * gets the text from TextInputLayout and returns it;
     *
     * in case you want the unmasked text, see [getValue];
     *
     * @returns the raw text.
     */
    fun getText(): String {
        return layout.editText?.text.toString()
    }
}