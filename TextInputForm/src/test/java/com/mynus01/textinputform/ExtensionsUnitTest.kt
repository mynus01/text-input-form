package com.mynus01.textinputform

import com.mynus01.textinputform.util.isValidCNPJ
import com.mynus01.textinputform.util.isValidCPF
import com.mynus01.textinputform.util.isValidEmail
import org.junit.Test

import org.junit.Assert.*

class ExtensionsUnitTest {
    @Test
    fun isEmailWithoutDotInvalid() {
        assertEquals("aaa@gmail".isValidEmail(), false)
    }

    @Test
    fun isEmailWithoutAtInvalid() {
        assertEquals("aaa.gmail".isValidEmail(), false)
    }

    @Test
    fun isEmailValid() {
        assertEquals("aaa@gmail.com".isValidEmail(), true)
    }

    @Test
    fun isCPFWithRepeatedNumbersInvalid() {
        assertEquals("000.000.000-00".isValidCPF(), false)
    }

    @Test
    fun isCPFWithRandomNumbersInvalid() {
        assertEquals("123.456.789-01".isValidCPF(), false)
    }

    @Test
    fun isCPFValid() {
        assertEquals("100.202.149-95".isValidCPF(), true)
    }

    @Test
    fun isCNPJWithRepeatedNumbersInvalid() {
        assertEquals("00.000.000/0000-00".isValidCNPJ(), false)
    }

    @Test
    fun isCNPJWithRandomNumbersInvalid() {
        assertEquals("12.345.678/9012.34".isValidCNPJ(), false)
    }

    @Test
    fun isCNPJValid() {
        assertEquals("41.705.727/0001-51".isValidCNPJ(), true)
    }
}