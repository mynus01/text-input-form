package com.mynus01.textinputformexample

import android.content.Context
import android.view.KeyEvent
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
    private lateinit var instrumentationContext: Context
    private var cpfToBeTyped = "10020214995"
    private var emailToBeTyped = "matheus.sandri@premiersoft.net"

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun successScenario() {
        val cpfToBeOutput = "100.202.149-95"

        onView(withId(R.id.editTextCPF))
            .perform(
                typeText(cpfToBeTyped),
                closeSoftKeyboard()
            )
            .check(matches(withText(cpfToBeOutput)))

        onView(withId(R.id.editTextEmail))
            .perform(
                typeText(emailToBeTyped),
                closeSoftKeyboard(),
                pressKey(KeyEvent.KEYCODE_TAB)
            )

        onView(withId(R.id.swtTerms))
            .perform(click())

        onView(withId(R.id.buttonLogin))
            .check(matches(isEnabled()))
    }

    @Test
    fun errorCPFScenario() {
        cpfToBeTyped = "00000000000"

        val errorText = instrumentationContext.getString(R.string.form_field_validation_error_o, "CPF")

        onView(withId(R.id.editTextCPF))
            .perform(
                typeText(cpfToBeTyped),
                closeSoftKeyboard()
            )

        onView(withId(R.id.textInputLayoutCPF))
            .check { view, _ ->
                val actualError = (view as TextInputLayout).error
                assertEquals(actualError, errorText)
            }

        onView(withId(R.id.editTextEmail))
            .perform(
                typeText(emailToBeTyped),
                closeSoftKeyboard()
            )

        onView(withId(R.id.swtTerms))
            .perform(click())

        onView(withId(R.id.buttonLogin))
            .check(matches(isEnabled()))
    }

    @Test
    fun errorEmailScenario() {
        val errorText = instrumentationContext.getString(R.string.form_field_validation_error_o, "E-mail")
        emailToBeTyped = "aaaaaaa"

        onView(withId(R.id.editTextEmail))
            .perform(
                typeText(emailToBeTyped),
                closeSoftKeyboard()
            )

        onView(withId(R.id.editTextCPF))
            .perform(
                typeText(cpfToBeTyped),
                closeSoftKeyboard()
            )

        onView(withId(R.id.textInputLayoutEmail))
            .check { view, _ ->
                val actualError = (view as TextInputLayout).error
                assertEquals(actualError, errorText)
            }

        onView(withId(R.id.swtTerms))
            .perform(click())

        onView(withId(R.id.buttonLogin))
            .check(matches(isNotEnabled()))
    }
}