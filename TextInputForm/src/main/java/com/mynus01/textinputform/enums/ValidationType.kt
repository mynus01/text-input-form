package com.mynus01.textinputform.enums

/**
 * enum class containing all the validation types supported by [com.mynus01.textinputform.model.FormField].
 * @property [ON_TEXT_CHANGED] will validate the field every time the user inserts something.
 * @property [ON_FOCUS_CHANGED] will validate the field only when the field loses focus.
 *
 * @since 0.0.1
 */
enum class ValidationType {
    ON_TEXT_CHANGED, ON_FOCUS_CHANGED
}