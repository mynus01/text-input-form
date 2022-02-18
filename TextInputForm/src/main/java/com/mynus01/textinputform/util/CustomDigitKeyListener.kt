package com.mynus01.textinputform.util

import android.text.InputType
import android.text.method.NumberKeyListener

/**
 * as the name suggests, this class acts as [import android.text.method.KeyListener] based on [mInputType] and [mAccepted].
 *
 * @property mAccepted defines the only chars that the [android.widget.EditText] this class is implemented on will accept.
 * @property mInputType defines the [android.widget.EditText.setInputType] of the [android.widget.EditText] this class is implemented on.
 *
 *
 * @since 0.0.1
 */
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