package com.mynus01.textinputform.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat

fun EditText.mask(pattern: String, maxLength: Int?, placeholder: Char, delimiters: List<Char>? = null) {
    var isBackspaceClicked = false

    fun generatePairs(): List<Pair<Int, Char>> {
        val pairs = ArrayList<Pair<Int, Char>>()

        for ((i, c) in pattern.withIndex()) {
            if (c != placeholder) {
                pairs.add(Pair(i, c))
            }
        }

        return pairs
    }

    val dividers = generatePairs()

    val textWatcher = object : TextWatcher {

        var isEdited = false
        var lastChar: Char? = null

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            isBackspaceClicked = after < count

            if (isBackspaceClicked) {
                lastChar = s[s.length - 1]
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (!isBackspaceClicked) {
                if (isEdited) {
                    isEdited = false
                    return
                }

                var working = getEditText()

                dividers.forEach {
                    working = manageDivider(working, it.first, it.second, start, before)
                }

                isEdited = true
                setText(working)
                setSelection(text.length)
            }
        }

        private fun manageDivider(working: String, position: Int, dividerCharacter: Char, start: Int, before: Int): String {
            if (working.length == position) {
                return if (before <= position && start < position) {
                    working + dividerCharacter
                } else {
                    working.dropLast(1)
                }
            } else if (working.length == position + 1) {
                return dividerCharacter + working
            }
            return working
        }

        private fun getEditText(): String {
            maxLength?.let { max ->
                if (text.length >= max) {
                    return text.toString().substring(0, max)
                }
            }
            return text.toString()
        }

        override fun afterTextChanged(s: Editable) {
            delimiters?.let {
                if (isBackspaceClicked && s.length > 1) {
                    lastChar?.let { last ->
                        if (it.contains(last)) {
                            s.delete(s.length - 1, s.length)

                            while (s.length > 1 && it.contains(s[s.length - 1])) {
                                s.delete(s.length - 1, s.length)
                            }
                            setSelection(text.length)
                        }
                    }
                }
                if (isBackspaceClicked && s.length == 1 && it.contains(s[0])) {
                    s.delete(s.length - 1, s.length)
                    setSelection(text.length)
                }
            }
        }
    }

    addTextChangedListener(textWatcher)
}

fun CharSequence.isValidEmail(): Boolean {
    if (isBlank()) return false

    if (!contains('.')) {
        return false
    } else {
        var isAtAdded = false

        for ((i, c) in withIndex()) {
            if (c == '@' && i > 0) {
                if (isAtAdded) {
                    return false
                } else {
                    isAtAdded = true
                }
            }
        }
        if (isAtAdded) {
            return true
        }
    }
    return false
}

fun CharSequence.isValidCPF(): Boolean {
    if (isBlank()) return false

    val numbers = arrayListOf<Int>()

    filter { it.isDigit() }.forEach {
        numbers.add(it.toString().toInt())
    }

    if (numbers.size != 11) return false

    (0..9).forEach { n ->
        val digits = arrayListOf<Int>()
        repeat((0..10).count()) { digits.add(n) }
        if (numbers == digits) return false
    }

    val dv1 = ((0..8).sumOf { (it + 1) * numbers[it] }).rem(11).let {
        if (it >= 10) 0 else it
    }

    val dv2 = ((0..8).sumOf { it * numbers[it] }.let { (it + (dv1 * 9)).rem(11) }).let {
        if (it >= 10) 0 else it
    }

    return numbers[9] == dv1 && numbers[10] == dv2
}

fun CharSequence.isValidCNPJ(): Boolean {
    if (isBlank()) return false

    val numbers = arrayListOf<Int>()

    filter { it.isDigit() }.forEach {
        numbers.add(it.toString().toInt())
    }

    if (numbers.size != 14) return false

    (0..9).forEach { n ->
        val digits = arrayListOf<Int>()
        repeat((0..13).count()) { digits.add(n) }
        if (numbers == digits) return false
    }

    fun validateVerificationDigit(firstDigit: Boolean, cnpj: List<Int>): Boolean {
        val startPos = when (firstDigit) {
            true -> 11
            else -> 12
        }
        val weightOffset = when (firstDigit) {
            true -> 0
            false -> 1
        }
        val sum = (startPos downTo 0).fold(0) { acc, pos ->
            val weight = 2 + ((11 + weightOffset - pos) % 8)
            val num = cnpj[pos]
            val sum = acc + (num * weight)
            sum
        }
        val expectedDigit = when (val result = sum % 11) {
            0, 1 -> 0
            else -> 11 - result
        }

        val actualDigit = cnpj[startPos + 1].toString().toInt()

        return expectedDigit == actualDigit
    }

    return validateVerificationDigit(true, numbers) &&
            validateVerificationDigit(false, numbers)
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    val clip = ClipData.newPlainText("label", text)
    clipboard?.setPrimaryClip(clip)
}

