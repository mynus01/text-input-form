package com.mynus01.textinputform.model

/**
 * base class for customizing your [FormField].
 *
 * @param name the name this field will be referred as in error messages.
 * @param inputType the [android.text.InputType] the field will use.
 * @param allowedChars an string containing all the chars allowed to be added to the [FormField],
 * chars that are not in the string will be ignored.
 *
 * &nbsp;
 *
 * for example:
 * ```
 *
 * FormField(lytExample, FieldType.CUSTOM, typeProperties = TypeProperties("Example field", allowedChars = "wasd"))
 *
 * ```
 * the [FormField] above will only accept the chars 'w', 'a', 's', 'd'.
 *
 * &nbsp;
 *
 * @param maskPattern a pattern that this field will follow such as "##/##/####" for dates, you can use any char as the placeholder by passing it to the [maskPlaceholder].
 * @param maskPlaceholder the placeholder that represents the user input in [maskPattern].
 * @param delimiters an auto generated list used for masking, containing all chars in [maskPattern] except [maskPlaceholder]s.
 *
 *
 * @since 0.0.1
 */
data class TypeProperties(
    val name: String,
    val inputType: Int? = null,
    val allowedChars: String? = null,
    val maskPattern: String? = null,
    val maskPlaceholder: Char = '#',
    var delimiters: List<Char>? = null
)