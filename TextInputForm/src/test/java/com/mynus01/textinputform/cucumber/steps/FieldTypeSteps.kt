package com.mynus01.textinputform.cucumber.steps

import com.mynus01.textinputform.enums.FieldType
import com.mynus01.textinputform.util.isValidCNPJ
import com.mynus01.textinputform.util.isValidCPF
import com.mynus01.textinputform.util.isValidEmail
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.assertEquals

class FieldTypeSteps {
    private lateinit var fieldType: FieldType
    private var result: Boolean = false

    @Given("FieldType is {string}")
    fun field_type_is(type: String) {
        fieldType =  FieldType.valueOf(type)
    }

    @When("{string} is inserted")
    fun is_inserted(value: String) {
        result = when(fieldType) {
            FieldType.CPF -> value.isValidCPF()
            FieldType.CNPJ -> value.isValidCNPJ()
            FieldType.EMAIL -> value.isValidEmail()
            else -> true
        }
    }

    @Then("it should return {string}")
    fun it_should_return(value: String) {
        val expected = value == "true"
        assertEquals(expected, result)
    }
}