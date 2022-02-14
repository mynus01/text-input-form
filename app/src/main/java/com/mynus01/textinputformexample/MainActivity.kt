package com.mynus01.textinputformexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import com.mynus01.textinputform.TextInputForm
import com.mynus01.textinputform.enums.FieldType
import com.mynus01.textinputform.enums.ValidationType
import com.mynus01.textinputform.model.FormField
import com.mynus01.textinputform.util.showToast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lytCPF = findViewById<TextInputLayout>(R.id.textInputLayoutCPF)
        val lytEmail = findViewById<TextInputLayout>(R.id.textInputLayoutEmail)
        val swtTerms = findViewById<SwitchMaterial>(R.id.swtTerms)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)

        val cpfField = FormField(lytCPF, FieldType.CPF)

        val emailField = FormField(lytEmail, FieldType.EMAIL,
            validationType = ValidationType.ON_FOCUS_CHANGED,
        )

        val form = TextInputForm(
            cpfField, emailField,
            viewToEnable = btnLogin,
            isExtraConditionValid = false
        )

        swtTerms.setOnCheckedChangeListener { _, value ->
            form.isExtraConditionValid = value
        }

        btnLogin.setOnClickListener {
            showToast("Masked CPF: ${cpfField.getText()}\nUnmasked CPF: ${cpfField.getValue()}")
        }
    }
}