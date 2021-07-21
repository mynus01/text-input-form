package com.mynus01.textinputformexample

import com.mynus01.textinputform.isValidCNPJ
import com.mynus01.textinputform.isValidCPF
import com.mynus01.textinputform.isValidEmail
import org.junit.Test

import org.junit.Assert.*

class ExtensionsUnitTest {
    @Test
    fun isEmailInvalid() {
        assertEquals("aaa@gmail".isValidEmail(), false)
    }

    @Test
    fun isEmailValid() {
        assertEquals("aaa@gmail.com".isValidEmail(), true)
    }

    @Test
    fun isCPFInvalid() {
        assertEquals("111.111.111-11".isValidCPF(), false)
    }

    @Test
    fun isCPFValid() {
        assertEquals("083.193.269-40".isValidCPF(), true)
    }

    @Test
    fun isCNPJInvalid() {
        assertEquals("00.000.000/0000-00".isValidCNPJ(), false)
    }

    @Test
    fun isCNPJValid() {
        assertEquals("41.705.727/0001-51".isValidCNPJ(), true)
    }
}