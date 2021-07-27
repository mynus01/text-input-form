package com.mynus01.textinputformexample

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.textfield.TextInputLayout
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTests {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun successScenario() {
        val cpfToBeTyped = "10020214995"
        val cpfToBeOutput = "100.202.149-95"
        val emailToBeTyped = "matheus.sandri@premiersoft.net"

        onView(withId(R.id.editTextCPF))
            .perform(typeText(cpfToBeTyped))
            .check(matches(withText(cpfToBeOutput)))

        onView(withId(R.id.editTextEmail))
            .perform(typeText(emailToBeTyped), closeSoftKeyboard())

        onView(withId(R.id.swtTerms))
            .perform(click())

        onView(withId(R.id.buttonLogin))
            .check(matches(isEnabled()))
    }

    @Test
    fun errorScenario() {
        val cpfToBeTyped = "00000000000"
        val errorText = instrumentationContext.getString(R.string.form_field_validation_error_o, "CPF")
        val emailToBeTyped = "matheus.sandri@premiersoft.net"

        onView(withId(R.id.editTextCPF))
            .perform(typeText(cpfToBeTyped))

        onView(withId(R.id.textInputLayoutCPF))
            .check { view, _ ->
                val actualError = (view as TextInputLayout).error
                assertEquals(actualError, errorText)
            }

        onView(withId(R.id.editTextEmail))
            .perform(typeText(emailToBeTyped), closeSoftKeyboard())

        onView(withId(R.id.swtTerms))
            .perform(click())

        onView(withId(R.id.buttonLogin))
            .check(matches(isNotEnabled()))
    }
}