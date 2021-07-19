package com.premiersoft.textinputform

import android.view.View
import androidx.core.widget.doOnTextChanged

class TextInputForm(
    private vararg val fieldsList: FormField,
    private val viewToEnable: View,
    var isOptionalConditionSupplied: Boolean = true
) {
    init {
        for ((index, field) in fieldsList.withIndex()) {
            field.layout.editText?.doOnTextChanged { text, _, _, _ ->
                fieldsList[index].isOk = validateFieldLength(index, text)
                validate()
            }
        }
        validate()
    }

    fun validate() {
        val result = fieldsList.none { !it.isOk } && isOptionalConditionSupplied

        viewToEnable.isEnabled = result
    }

    private fun validateFieldLength(index: Int, text: CharSequence?): Boolean {
        val field = fieldsList[index]

        if (field.isRequired) {
            if (text == null || text.isBlank()) {
                field.layout.error = field.layout.context.getString(R.string.form_empty_error, field.typeProperties?.name)
                field.layout.isErrorEnabled = true
                return false
            }

            field.minLength?.let { min ->
                if (text.count() < min) {
                    if (field.maxLength != null && field.maxLength == min) {
                        field.layout.error = field.layout.context.getString(R.string.form_length_error, field.typeProperties?.name, field.minLength)
                    } else {
                        field.layout.error = field.layout.context.getString(R.string.form_min_length_error, field.typeProperties?.name, field.minLength)
                    }
                    field.layout.isErrorEnabled = true
                    return false
                }
            }
        }
        field.layout.isErrorEnabled = false
        return true
    }
}