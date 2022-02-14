package com.mynus01.textinputform

import android.view.View
import androidx.core.widget.doOnTextChanged
import com.mynus01.textinputform.enums.FieldType
import com.mynus01.textinputform.enums.ValidationType
import com.mynus01.textinputform.model.FormField
import com.mynus01.textinputform.util.*

/**
 * base class and constructor for a simple form with field type validations and automatically enabling/disabling a [View] (usually the submit button).
 *
 * @param fieldsList an array of [FormField]s which contains all the required and optional fields to be validated.
 * @param viewToEnable the view that will be enabled if all the validations for each [FormField]s in [fieldsList] were supplied.
 * @param isExtraConditionValid an extra condition that needs to be fulfilled in order to [viewToEnable] be enabled,
 * use [validate] when updating this parameter.
 *
 *
 * @since 0.0.1
 */
class TextInputForm(
    private vararg val fieldsList: FormField,
    private val viewToEnable: View,
    var isExtraConditionValid: Boolean = true
) {
    init {
        for ((index, field) in fieldsList.withIndex()) {
            when (field.validationType) {
                ValidationType.ON_TEXT_CHANGED -> {
                    field.layout.editText?.doOnTextChanged { text, _, _, _ ->
                        fieldsList[index].isOk =
                            validateFieldLength(index, text) && validateTypeConditions(index, text)
                        validate()
                    }
                }
                ValidationType.ON_FOCUS_CHANGED -> {
                    field.layout.editText?.setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            fieldsList[index].isOk = validateFieldLength(
                                index,
                                field.getText()
                            ) && validateTypeConditions(index, field.getText())
                            validate()
                        }
                    }
                }
            }
        }
        validate()
    }

    /**
     * enables or disables [viewToEnable] based on [FormField.isOk] parameter for each item on [fieldsList],
     * in case they all are true and the optional conditional parameter [isExtraConditionValid] is true
     * enables [viewToEnable], otherwise disables it.
     *
     *** use this method if you need to force a form validation (for example, when the [isExtraConditionValid] parameter was updated).
     *
     *
     * @since 0.0.1
     */
    fun validate() {
        viewToEnable.isEnabled = fieldsList.none { !it.isOk } && isExtraConditionValid
    }


    /**
     * checks if the text length of a given [FormField] is between [FormField.minLength] and [FormField.maxLength] parameters,
     * sets to [FormField.layout] the [com.google.android.material.textfield.TextInputLayout.setErrorEnabled] attribute based on the result and a custom message if
     * the error is enabled.
     *
     * @param index the index of the desired [FormField] in [fieldsList].
     * @param text the text from the [FormField] which the length will be validated.
     *
     * @return true if the field is not required ([FormField.isRequired] == false) or the [text] parameter is
     * between [FormField.minLength] and [FormField.maxLength], false otherwise.
     *
     *
     * @since 0.0.1
     */
    private fun validateFieldLength(index: Int, text: CharSequence?): Boolean {
        val field = fieldsList[index]

        if (field.isRequired) {
            if (text.isNullOrBlank()) {
                field.layout.error = field.layout.context.getString(
                    R.string.form_field_empty_error,
                    field.typeProperties?.name
                )
                field.layout.isErrorEnabled = true
                return false
            }

            field.minLength?.let { min ->
                if (text.length < min) {
                    val minLength =
                        field.typeProperties?.delimiters?.size?.let { min - it - 1 } ?: min
                    if (field.maxLength != null && field.maxLength == min) {
                        field.layout.error = field.layout.context.getString(
                            R.string.form_field_length_error,
                            field.typeProperties?.name,
                            minLength
                        )
                    } else {
                        field.layout.error = field.layout.context.getString(
                            R.string.form_field_min_length_error,
                            field.typeProperties?.name,
                            minLength
                        )
                    }
                    field.layout.isErrorEnabled = true
                    return false
                }
            }
        }

        field.layout.isErrorEnabled = false

        return true
    }

    /**
     * checks if the text of a given [FormField] is fulfilling some [FormField.type] determined rules,
     * and sets to [FormField.layout] the [com.google.android.material.textfield.TextInputLayout.setError] attribute based on the result with
     * a custom message if the given rule wasn't fulfilled.
     *
     * @param index the index of the desired [FormField] in [fieldsList].
     * @param text the text from the [FormField] which will be validated.
     *
     * @return true if the field has no rules or the [text] parameter is
     * fulfilling the existing rules, false otherwise.
     *
     *
     * @since 0.0.1
     */
    private fun validateTypeConditions(index: Int, text: CharSequence?): Boolean {
        val field = fieldsList[index]

        when (field.type) {
            FieldType.CPF -> {
                if (!field.getValue().isValidCPF()) {
                    field.layout.error = field.layout.context.getString(
                        R.string.form_field_validation_error_o,
                        field.typeProperties?.name
                    )
                    if (field.isRequired)
                        return false
                }
            }
            FieldType.CNPJ -> {
                if (text?.isValidCNPJ() != true) {
                    field.layout.error = field.layout.context.getString(
                        R.string.form_field_validation_error_o,
                        field.typeProperties?.name
                    )
                    if (field.isRequired)
                        return false
                }
            }
            FieldType.EMAIL -> {
                if (text?.isValidEmail() != true) {
                    field.layout.error = field.layout.context.getString(
                        R.string.form_field_validation_error_o,
                        field.typeProperties?.name
                    )
                    if (field.isRequired)
                        return false
                }
            }
            else -> return true
        }
        return true
    }
}