package com.premiersoft.textinputform

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * @throws PatternException
 */
fun EditText.mask(patterns: List<String>, minLength: Int, placeholder: Char) {

    val patternsMap = HashMap<Int, String>(patterns.size)
    patterns.forEach { patternsMap[it.length] = it }

    var currentLength = minLength
    var currentPattern = patternsMap[currentLength] ?: throw PatternException("Invalid pattern")
    var isBackspaceClicked = false

    fun generatePairs(): List<Pair<Int, Char>> {
        val pairs = ArrayList<Pair<Int, Char>>()
        currentPattern = patternsMap[currentLength] ?: throw PatternException("Invalid pattern")

        for ((i, c) in currentPattern.withIndex()) {
            if (c != placeholder) {
                pairs.add(Pair(i, c))
            }
        }

        return pairs
    }

    var dividers = generatePairs()

    val textWatcher = object : TextWatcher {

        var edited = false

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            isBackspaceClicked = after < count
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (!isBackspaceClicked) {
                if (edited) {
                    edited = false
                    return
                }

                var working = getEditText()

                if (patternsMap.containsKey(working.length)) {
                    currentLength = working.length
                    dividers = generatePairs()
                }

                dividers.forEach {
                    working = manageDivider(working, it.first, it.second, start, before)
                }

                edited = true
                setText(working)
                setSelection(text.length)
            }
        }

        private fun manageDivider(working: String, position: Int, dividerCharacter: Char, start: Int, before: Int): String {
            if (working.length == position) {
                return if (before <= position && start < position)
                    working + dividerCharacter
                else
                    working.dropLast(1)
            }
            return working
        }

        private fun getEditText(): String {
            currentPattern.length.let { max ->
                return if (text.length >= max)
                    text.toString().substring(0, max)
                else
                    text.toString()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    addTextChangedListener(textWatcher)
}

fun CharSequence.isValidEmail(): Boolean {
    if (this.isBlank()) return false

    if (!contains('.')) {
        return false
    } else {
        var added = false

        for (c in this) {
            if (c == '@') {
                if (added) {
                    return false
                } else {
                    added = true
                }
            }
        }
    }
    return true
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

