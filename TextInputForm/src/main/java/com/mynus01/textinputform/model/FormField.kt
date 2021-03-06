package com.mynus01.textinputform.model

import android.text.InputFilter
import android.text.InputType
import com.google.android.material.textfield.TextInputLayout
import com.mynus01.textinputform.enums.FieldType
import com.mynus01.textinputform.R
import com.mynus01.textinputform.enums.ValidationType
import com.mynus01.textinputform.util.CustomDigitsKeyListener
import com.mynus01.textinputform.util.mask

/**
 * class used for manipulating and customizing a [TextInputLayout].
 *
 * @param layout the [TextInputLayout] for the rules to be applied.
 * @param type a [FieldType] for applying rules on [layout].
 * @param typeProperties a custom set of rules in case you have selected [type] as [FieldType.CUSTOM] (otherwise your rules will be ignored).
 * @param isRequired true if this field is required, false otherwise.
 * @param minLength a rule for this field that determines a minimum length (in case it's the same as [maxLength] the error messages will change).
 * @param maxLength a rule for this field that determines a maximum length (in case it's the same as [minLength] the error messages will change).
 * @param validationType the type of validation this field will have.
 * @param isOk an auto generated parameter that says this field is fulfilling all the given rules.
 *
 *
 * @since 0.0.1
 */
class FormField(
    val layout: TextInputLayout,
    val type: FieldType = FieldType.CUSTOM,
    var typeProperties: TypeProperties? = null,
    val isRequired: Boolean = true,
    var minLength: Int? = null,
    var maxLength: Int? = null,
    var validationType: ValidationType = ValidationType.ON_TEXT_CHANGED,
    var isOk: Boolean = false
) {
    init {
        layout.context.apply {
            when (type) {
                FieldType.TEXT -> {
                    typeProperties = TypeProperties(
                        getString(R.string.form_field_name_generic), InputType.TYPE_CLASS_TEXT,
                        getString(R.string.letters_accentuation_and_numbers_allowed)
                    )
                }
                FieldType.TEXT_ONLY -> {
                    typeProperties = TypeProperties(
                        getString(R.string.form_field_name_generic), InputType.TYPE_CLASS_TEXT,
                        getString(R.string.letters_and_accentuation_allowed)
                    )
                }
                FieldType.EMAIL -> {
                    minLength = 6
                    maxLength = 255

                    typeProperties = TypeProperties(
                        getString(R.string.form_field_name_email), InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                        getString(R.string.characters_to_email_allowed)
                    )
                }
                FieldType.DATE -> {
                    minLength = 10
                    maxLength = 10

                    typeProperties = TypeProperties(
                        getString(R.string.form_field_name_date), InputType.TYPE_CLASS_NUMBER,
                        getString(R.string.only_numbers_allowed), getString(R.string.form_field_pattern_date)
                    )
                }
                FieldType.CELLPHONE -> {
                    minLength = 14
                    maxLength = 16

                    typeProperties = TypeProperties(
                        getString(R.string.form_field_name_cellphone), InputType.TYPE_CLASS_NUMBER,
                        getString(R.string.only_numbers_allowed), getString(R.string.form_field_pattern_cellphone)
                    )
                }
                FieldType.PHONE -> {
                    minLength = 14
                    maxLength = 14

                    typeProperties = TypeProperties(
                        getString(R.string.form_field_name_telephone), InputType.TYPE_CLASS_NUMBER,
                        getString(R.string.only_numbers_allowed), getString(R.string.form_field_pattern_telephone)
                    )
                }
                FieldType.CPF -> {
                    minLength = 14
                    maxLength = 14

                    typeProperties = TypeProperties(
                        getString(R.string.form_field_name_cpf), InputType.TYPE_CLASS_NUMBER,
                        getString(R.string.only_numbers_allowed), getString(R.string.form_field_pattern_cpf)
                    )
                }
                FieldType.CNPJ -> {
                    minLength = 18
                    maxLength = 18

                    typeProperties = TypeProperties(
                        getString(R.string.form_field_name_cnpj), InputType.TYPE_CLASS_NUMBER,
                        getString(R.string.only_numbers_allowed), getString(R.string.form_field_pattern_cnpj)
                    )
                }
                FieldType.CEP -> {
                    minLength = 9
                    maxLength = 9

                    typeProperties = TypeProperties(
                        getString(R.string.form_field_name_cep), InputType.TYPE_CLASS_NUMBER,
                        getString(R.string.only_numbers_allowed), getString(R.string.form_field_pattern_cep)
                    )
                }
                FieldType.CUSTOM -> {
                    if (typeProperties == null) {
                        typeProperties = TypeProperties(getString(R.string.form_field_name_generic))
                    }
                }
            }
        }

        layout.editText?.let { edit ->
            typeProperties?.apply {
                maskPattern?.let { pattern ->
                    delimiters = getDelimiters(listOf(pattern), maskPlaceholder)
                    edit.mask(pattern, maskPlaceholder, delimiters, maxLength)
                }
                allowedChars?.let { chars ->
                    val keyListener = CustomDigitsKeyListener.getInstance(chars)
                    inputType?.let { type ->
                        keyListener.mInputType = type
                    }
                    edit.keyListener = keyListener
                }
            }
            maxLength?.let { max ->
                edit.filters += InputFilter.LengthFilter(max)
            }
        }

        if (!isRequired) {
            isOk = true
        }
    }

    /**
     * removes the mask of the extracted text from the [TextInputLayout]
     * and returns it.
     *
     * in case you want the text with the mask, see [getText].
     *
     * @return the unmasked text.
     *
     *
     * @since 0.0.1
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
     * gets the text from [TextInputLayout] and returns it.
     *
     * in case you want the unmasked text, see [getValue].
     *
     * @return the raw text.
     *
     *
     * @since 0.0.1
     */
    fun getText(): String {
        return layout.editText?.text.toString()
    }

    /**
     * iterates on [patterns] and on each char of that string to see if it differs from [placeholder],
     * if it does, adds to a list. after all iterations, sets this list as [TypeProperties.delimiters] of [typeProperties] object.
     *
     * @param patterns a list of patterns that will be used to mask a [FormField].
     * @param placeholder the placeholder char used in [patterns].
     *
     * @return a list containing all the non-placeholder chars.
     *
     *
     * @since 0.0.1
     */
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
}