Feature: Validate FieldTypes
  @formfield @fieldtype

  Scenario Outline: Check if all FieldType rules are working as expected
    Given FieldType is <type>
    When <value> is inserted
    Then it should return <expected>

    Examples:
      | type    | value                | expected |
      | "CPF"   | "111.111.111-11"     | "false"  |
      | "CPF"   | "123.456.789-01"     | "false"  |
      | "CPF"   | "100.202.149-95"     | "true"   |

      | "CNPJ"  | "11.111.111/1111-11" | "false"  |
      | "CNPJ"  | "12.345.678/9012-34" | "false"  |
      | "CNPJ"  | "41.705.727/0001-51" | "true"   |

      | "EMAIL" | "johndoe.net"        | "false"  |
      | "EMAIL" | "john@doenet"        | "false"  |
      | "EMAIL" | "john@doe.net"       | "true"   |