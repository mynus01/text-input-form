package com.mynus01.textinputform.util

import android.text.InputType
import android.text.method.NumberKeyListener


class CustomDigitsKeyListener : NumberKeyListener() {
    private lateinit var mAccepted: CharArray
    var mInputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

    companion object {
        fun getInstance(accepted: String): CustomDigitsKeyListener {
            val custom = CustomDigitsKeyListener()
            custom.mAccepted = accepted.toCharArray(CharArray(accepted.length), 0, 0, accepted.length)
            return custom
        }
    }

    override fun getAcceptedChars(): CharArray {
        return mAccepted
    }

    override fun getInputType(): Int {
        return mInputType
    }
}