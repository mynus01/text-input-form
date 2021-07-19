package com.premiersoft.textinputform

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.mask(patterns: Map<Int, String>, minLength: Int, placeholder: Char) {
    var currentLength = minLength
    var currentPattern = patterns[currentLength]!!

    fun generatePairs() : List<Pair<Int, Char>> {
        val pairs = ArrayList<Pair<Int, Char>>()
        currentPattern = patterns[currentLength]!!

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

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (edited) {
                edited = false
                return
            }

            var working = getEditText()

            if (patterns.containsKey(working.length)) {
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
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    }

    addTextChangedListener(textWatcher)
}