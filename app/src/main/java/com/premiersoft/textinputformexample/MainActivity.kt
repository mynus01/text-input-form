package com.premiersoft.textinputformexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import com.premiersoft.textinputform.TextInputForm
import com.premiersoft.textinputform.FieldType
import com.premiersoft.textinputform.FormField

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lytEmail = findViewById<TextInputLayout>(R.id.textInputLayoutCPF)
        val lytPassword = findViewById<TextInputLayout>(R.id.textInputLayoutEmail)
        val swtTerms = findViewById<SwitchMaterial>(R.id.swtTerms)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)

        val form =  TextInputForm(
            FormField(lytEmail, FieldType.CPF),
            FormField(lytPassword, isRequired = false),
            viewToEnable = btnLogin,
            isOptionalConditionSupplied = false
        )

        swtTerms.setOnCheckedChangeListener { _, value ->
            form.isOptionalConditionSupplied = value
            form.validate()
        }
    }
}