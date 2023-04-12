package pl.piekoszek.ing.mutable.collections

import pl.piekoszek.ing.mutable.Decimal
import spock.lang.Specification

class DecimalSpec extends Specification {
    def "Should read and prints same integer, input ending with space"() {
        given:
        def decimal = new Decimal()
        def stringWriter = new StringWriter()
        def printWriter = new PrintWriter(stringWriter)

        when: "read value and space, to mimic part of JSON deserialization"
        decimal.read(new ByteArrayInputStream("123 ".getBytes()))
        and: "write value to outputStream"
        decimal.print(printWriter)

        then:
        stringWriter.toString() == "123"
    }

    def "Should read and print same integer, input ending with comma"() {
        given:
        def decimal = new Decimal()
        def stringWriter = new StringWriter()
        def printWriter = new PrintWriter(stringWriter)

        when: "read value and comma, to mimic part of JSON deserialization"
        decimal.read(new ByteArrayInputStream("123,".getBytes()))
        and: "write value to outputStream"
        decimal.print(printWriter)

        then:
        stringWriter.toString() == "123"
    }

    def "Should read and print same, single digit integer"() {
        given:
        def decimal = decimal("5")

        expect:
        decimalToString(decimal) == "5"
    }

    def "Should read and print same float with one decimal place"() {
        given:
        def decimal = decimal("432.5")

        expect:
        decimalToString(decimal) == "432.5"
    }

    def "Should read and print same float with two decimal places"() {
        given:
        def decimal = decimal("432.57")

        expect:
        decimalToString(decimal) == "432.57"
    }

    def "Should read and print same float with three decimal places"() {
        given:
        def decimal = decimal("432.523")

        expect:
        decimalToString(decimal) == "432.523"
    }

    def "Should read and print same float with zero integer part"() {
        given:
        def decimal = decimal("0.64")

        expect:
        decimalToString(decimal) == "0.64"
    }

    def "Should read and print same float with zero at the end"() {
        given:
        def decimal = decimal("0.60")

        expect:
        decimalToString(decimal) == "0.60"
    }

    def "Should read and print same negative integer"() {
        given:
        def decimal = decimal("-4355")

        expect:
        decimalToString(decimal) == "-4355"
    }

    def "Should read and print same negative float"() {
        given:
        def decimal = decimal("-432.55")

        expect:
        decimalToString(decimal) == "-432.55"
    }

    def "Should read and print same negative float with zero integer part"() {
        given:
        def decimal = decimal("-0.55")

        expect:
        decimalToString(decimal) == "-0.55"
    }

    def "Should add two integers"() {
        given:
        def firstNumber = decimal("100")
        def secondNumber = decimal("50")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "150"
    }

    def "Should add two floats"() {
        given:
        def firstNumber = decimal("10.34")
        def secondNumber = decimal("20.51")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "30.85"
    }

    def "Should add two floats with different decimal places"() {
        given:
        def firstNumber = decimal("10.34")
        def secondNumber = decimal("20.5")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "30.84"
    }


    def "Should add two floats with different decimal places 2"() {
        given:
        def firstNumber = decimal("10.3")
        def secondNumber = decimal("20.51")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "30.81"
    }

    def "Should add integer to float"() {
        given:
        def firstNumber = decimal("10.53")
        def secondNumber = decimal("20")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "30.53"
    }

    def "Should add float to integer"() {
        given:
        def firstNumber = decimal("10")
        def secondNumber = decimal("20.51")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "30.51"
    }

    def "Should add negative integer to positive integer"() {
        given:
        def firstNumber = decimal("30")
        def secondNumber = decimal("-20")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "10"
    }

    def "Should add negative integer to positive integer resulting in negative sum"() {
        given:
        def firstNumber = decimal("30")
        def secondNumber = decimal("-40")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "-10"
    }

    def "Should add negative integer to negative integer"() {
        given:
        def firstNumber = decimal("-30")
        def secondNumber = decimal("-20")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "-50"
    }

    def "Should add negative float to positive float"() {
        given:
        def firstNumber = decimal("23.23")
        def secondNumber = decimal("-12.02")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "11.21"
    }

    def "Should subtract integer from integer"() {
        given:
        def firstNumber = decimal("40")
        def secondNumber = decimal("5")

        when:
        firstNumber.subtract(secondNumber)

        then:
        decimalToString(firstNumber) == "35"
    }

    def "Should subtract float from float"() {
        given:
        def firstNumber = decimal("23.23")
        def secondNumber = decimal("12.02")

        when:
        firstNumber.subtract(secondNumber)

        then:
        decimalToString(firstNumber) == "11.21"
    }

    def "Should subtract negative float from positive float"() {
        given:
        def firstNumber = decimal("23.23")
        def secondNumber = decimal("-12.02")

        when:
        firstNumber.subtract(secondNumber)

        then:
        decimalToString(firstNumber) == "35.25"
    }

    def "Should add large integer to small integer"() {
        given:
        def firstNumber = decimal("10")
        def secondNumber = decimal("10000")

        when:
        firstNumber.add(secondNumber)

        then:
        decimalToString(firstNumber) == "10010"
    }

    def "Should add and subtract multiple times"() {
        given:
        def number = decimal("0")

        when:
        number.add(decimal("10"))

        then:
        decimalToString(number) == "10"

        when:
        number.add(decimal("0.12"))

        then:
        decimalToString(number) == "10.12"

        when:
        number.subtract(decimal("0"))

        then:
        decimalToString(number) == "10.12"

        when:
        number.add(decimal("-10.12"))

        then:
        decimalToString(number) == "0.00"

        when:
        number.subtract(decimal("0.01"))

        then:
        decimalToString(number) == "-0.01"

        when:
        number.add(decimal("100000000"))

        then:
        decimalToString(number) == "99999999.99"

        when:
        number.subtract(decimal("1"))

        then:
        decimalToString(number) == "99999998.99"

        number.add(decimal("1"))

        then:
        decimalToString(number) == "99999999.99"

        number.subtract(decimal("0.01"))

        then:
        decimalToString(number) == "99999999.98"

        number.add(decimal("0.01"))

        then:
        decimalToString(number) == "99999999.99"

        then:
        decimalToString(number) == "99999999.99"

        when:
        number.subtract(decimal("99999999.99"))

        then:
        decimalToString(number) == "0.00"
    }

    private def decimal(String value) {
        def decimal = new Decimal()
        decimal.read(new ByteArrayInputStream((value + ",").getBytes()))
        return decimal
    }

    private def decimalToString(Decimal decimal) {
        def stringWriter = new StringWriter()
        def printWriter = new PrintWriter(stringWriter)

        decimal.print(printWriter)
        printWriter.close()

        return stringWriter.toString();
    }
}
