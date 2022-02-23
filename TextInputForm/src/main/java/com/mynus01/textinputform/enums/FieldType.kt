package com.mynus01.textinputform.enums

/**
 * enum class containing all the default types supported by [com.mynus01.textinputform.model.FormField].
 *
 * you can also define specific rules and behaviours by using [CUSTOM] and applying a [com.mynus01.textinputform.model.TypeProperties]
 * object to your [com.mynus01.textinputform.model.FormField].
 *
 *
 * @since 0.0.1
 */
enum class FieldType {
    TEXT, TEXT_ONLY, EMAIL, DATE, CELLPHONE, PHONE, CPF, CNPJ, CEP, CUSTOM
}