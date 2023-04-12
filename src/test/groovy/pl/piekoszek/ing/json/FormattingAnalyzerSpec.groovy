package pl.piekoszek.ing.json

import spock.lang.Specification

import java.nio.charset.StandardCharsets

class FormattingAnalyzerSpec extends Specification {

    def "should analyze pretty json formatting and field order"() {
        given:
        def json = """
[
  {
    "debitAccount": "32309111922661937852684864",
    "creditAccount": "06105023389842834748547303",
    "amount": 10.90
  },
  {
    "debitAccount": "31074318698137062235845814",
    "creditAccount": "66105036543749403346524547",
    "amount": 200.90
  },
  {
    "debitAccount": "66105036543749403346524547",
    "creditAccount": "32309111922661937852684864",
    "amount": 50.10
  }
]
"""

        when:
        def formatting = new FormattingAnalyzer().analyze(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)))

        then:
        formatting.fields() == ["debitAccount", "creditAccount", "amount"]

        formatting.bytesBetweenObjectOpenAndFirstNameChar() == 6
        formatting.bytesBetweenLastNameCharAndFirstValueToken() == 3
        formatting.bytesBetweenLastValueTokenAndComma() == 0
        formatting.bytesBetweenLastValueTokenAndFirstNameChar() == 7
        formatting.bytesBetweenLastObjectValueTokenAndAfterNextObjectOpen() == 9
    }

    def "should analyze minified json formatting and field order"() {
        given:
        def minifiedJson = """
[{"debitAccount":"32309111922661937852684864","creditAccount":"06105023389842834748547303","amount":10.9},{"debitAccount":"31074318698137062235845814","creditAccount":"66105036543749403346524547","amount":200.9},{"debitAccount":"66105036543749403346524547","creditAccount":"32309111922661937852684864","amount":50.1}]"""

        when:
        def formatting = new FormattingAnalyzer().analyze(new ByteArrayInputStream(minifiedJson.getBytes(StandardCharsets.UTF_8)))

        then:
        formatting.fields() == ["debitAccount", "creditAccount", "amount"]

        formatting.bytesBetweenObjectOpenAndFirstNameChar() == 1
        formatting.bytesBetweenLastNameCharAndFirstValueToken() == 2
        formatting.bytesBetweenLastValueTokenAndComma() == 0
        formatting.bytesBetweenLastValueTokenAndFirstNameChar() == 2
        formatting.bytesBetweenLastObjectValueTokenAndAfterNextObjectOpen() == 3
    }
}
