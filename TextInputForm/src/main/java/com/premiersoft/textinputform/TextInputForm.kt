package com.premiersoft.textinputform

import android.view.View
import androidx.core.widget.doOnTextChanged

class TextInputForm(
    private vararg val fieldsList: FormField,
    private val viewToEnable: View,
    var isExtraConditionValid: Boolean = true
) {
    init {
        for ((index, field) in fieldsList.withIndex()) {
            field.layout.editText?.doOnTextChanged { text, _, _, _ ->
                fieldsList[index].isOk = validateFieldLength(index, text) && validateTypeConditions(index, text)
                validate()
            }
        }

        validate()
    }

    fun validate() {
        val result = fieldsList.none { !it.isOk } && isExtraConditionValid
        viewToEnable.isEnabled = result
    }

    private fun validateFieldLength(index: Int, text: CharSequence?): Boolean {
        val field = fieldsList[index]

        if (field.isRequired) {
            if (text == null || text.isBlank()) {
                field.layout.error = field.layout.context.getString(R.string.form_field_empty_error, field.typeProperties?.name)
                field.layout.isErrorEnabled = true
                return false
            }

            field.minLength?.let { min ->
                if (text.count() < min) {
                    if (field.maxLength != null && field.maxLength == min) {
                        field.layout.error = field.layout.context.getString(R.string.form_field_length_error, field.typeProperties?.name, field.minLength)
                    } else {
                        field.layout.error = field.layout.context.getString(R.string.form_field_min_length_error, field.typeProperties?.name, field.minLength)
                    }
                    field.layout.isErrorEnabled = true
                    return false
                }
            }
        }

        field.layout.isErrorEnabled = false

        return true
    }

    private fun validateTypeConditions(index: Int, text: CharSequence?): Boolean {
        val field = fieldsList[index]

        when (field.type) {
            FieldType.CPF -> {
                if (!field.getValue().isValidCPF()) {
                    field.layout.error = field.layout.context.getString(R.string.form_field_validation_error_o, field.typeProperties?.name)
                    if (field.isRequired)
                    return false
                }
            }
            FieldType.CNPJ -> {
                if (text?.isValidCNPJ() != true) {
                    field.layout.error = field.layout.context.getString(R.string.form_field_validation_error_o, field.typeProperties?.name)
                    if (field.isRequired)
                    return false
                }
            }
            FieldType.EMAIL -> {
                if (text?.isValidEmail() != true) {
                    field.layout.error = field.layout.context.getString(R.string.form_field_validation_error_o, field.typeProperties?.name)
                    if (field.isRequired)
                    return false
                }
            }
            else -> return true
        }
        return true
    }
}