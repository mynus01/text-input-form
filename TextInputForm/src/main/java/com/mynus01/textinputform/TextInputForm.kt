package com.mynus01.textinputform

import android.os.Build
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.mynus01.textinputform.model.FormField
import com.mynus01.textinputform.util.*

class TextInputForm(
    private vararg val fieldsList: FormField,
    private val viewToEnable: View,
    var isExtraConditionValid: Boolean = true
) {
    private var selectionActionMode: ActionMode.Callback? = null
    private var insertionActionMode: ActionMode.Callback? = null

    init {
        for ((index, field) in fieldsList.withIndex()) {
            field.layout.editText?.doOnTextChanged { text, _, _, _ ->
                selectionActionMode = createActionMode(field, true)
                insertionActionMode = createActionMode(field, false)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    field.layout.editText?.customInsertionActionModeCallback = insertionActionMode
                    field.layout.editText?.customSelectionActionModeCallback = selectionActionMode
                }

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
                    val minLength = field.typeProperties?.delimiters?.size?.let { min - it - 1 } ?: min
                    if (field.maxLength != null && field.maxLength == min) {
                        field.layout.error = field.layout.context.getString(R.string.form_field_length_error, field.typeProperties?.name, minLength)
                    } else {
                        field.layout.error = field.layout.context.getString(R.string.form_field_min_length_error, field.typeProperties?.name, minLength)
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

    private fun createActionMode(field: FormField, isSelection: Boolean) = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater = mode?.menuInflater
            inflater?.inflate(R.menu.custom_selection_action_mode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                android.R.id.cut -> {
                    field.layout.context.copyToClipboard(field.getValue())
                    field.layout.editText?.setText("")
                    field.layout.context.showToast("Selection cut worked")
                }
                android.R.id.copy -> {
                    field.layout.context.copyToClipboard(field.getValue())
                    field.layout.context.showToast("Selection copy worked")
                }
            }
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {

            selectionActionMode = null
        }
    }
}