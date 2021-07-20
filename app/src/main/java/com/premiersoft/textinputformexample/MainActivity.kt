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

        val lytCPF = findViewById<TextInputLayout>(R.id.textInputLayoutCPF)
        val lytEmail = findViewById<TextInputLayout>(R.id.textInputLayoutEmail)
        val swtTerms = findViewById<SwitchMaterial>(R.id.swtTerms)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)

        val cpfField = FormField(lytCPF, FieldType.CPF)
        val emailField = FormField(lytEmail, FieldType.EMAIL, isRequired = false)

        val form =  TextInputForm(cpfField, emailField,
            viewToEnable = btnLogin,
            isExtraConditionValid = false
        )

        swtTerms.setOnCheckedChangeListener { _, value ->
            form.isExtraConditionValid = value
            form.validate()
        }
    }
}