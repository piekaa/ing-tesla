package pl.piekoszek.ing.endpoints.transactions


import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import pl.piekoszek.ing.http.HttpClient
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionsApiSpec extends Specification {
    @LocalServerPort
    private int port

    private HttpClient httpClient

    def setup() {
        httpClient = new HttpClient(port, "transactions/report")
    }

    def "should accept one zero items in request"() {
        given:

        def (accounts, status) = httpClient.post("[]")

        expect:
        status == 200
        accounts == []
    }

    def "should accept one item in request"() {
        given:

        def (accounts, status) = httpClient.post("""
            [
              {
                "debitAccount": "32309111922661937852684864",
                "creditAccount": "06105023389842834748547303",
                "amount": 10.90
              }
            ]""")

        expect:
        status == 200
        accounts.collect { it.balance } == [10.90, -10.90]
    }

    def "should accept two items in request"() {
        given:

        def (accounts, status) = httpClient.post("""
            [
              {
                "debitAccount": "32309111922661937852684864",
                "creditAccount": "06105023389842834748547303",
                "amount": 10
              },
              {
                "debitAccount": "32309111922661937852684864",
                "creditAccount": "66105036543749403346524547",
                "amount": 10
              }
            ]""")

        expect:
        status == 200
        accounts.collect { it.balance } == [10, -20, 10]
    }

    def "debitAccount, creditAccount, amount order"() {
        given:

        def (accounts, status) = httpClient.post("""
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
            ]""")

        expect:
        correctResponse(accounts, status)
    }

    def "creditAccount, debitAccount, amount order"() {
        given:

        def (accounts, status) = httpClient.post("""
            [
              {
                "creditAccount": "06105023389842834748547303",
                "debitAccount": "32309111922661937852684864",
                "amount": 10.90
              },
              {
                "creditAccount": "66105036543749403346524547",
                "debitAccount": "31074318698137062235845814",
                "amount": 200.90
              },
              {
                "creditAccount": "32309111922661937852684864",
                "debitAccount": "66105036543749403346524547",
                "amount": 50.10
              }""")

        expect:
        correctResponse(accounts, status)
    }

    def "creditAccount, amount, debitAccount order"() {
        given:

        def (accounts, status) = httpClient.post("""
            [
              {
                "creditAccount": "06105023389842834748547303",
                "amount": 10.90,
                "debitAccount": "32309111922661937852684864"
              },
              {
                "creditAccount": "66105036543749403346524547",
                "amount": 200.90,
                "debitAccount": "31074318698137062235845814"
              },
              {
                "creditAccount": "32309111922661937852684864",
                "amount": 50.10,
                "debitAccount": "66105036543749403346524547"
              }
            ]""")

        expect:
        correctResponse(accounts, status)
    }

    def "amount, creditAccount, debitAccount order"() {
        given:

        def (accounts, status) = httpClient.post("""
            [
              {
                "amount": 10.90,
                "creditAccount": "06105023389842834748547303",
                "debitAccount": "32309111922661937852684864"
              },
              {
                "amount": 200.90,
                "creditAccount": "66105036543749403346524547",
                "debitAccount": "31074318698137062235845814"
              },
              {
                "amount": 50.10,
                "creditAccount": "32309111922661937852684864",
                "debitAccount": "66105036543749403346524547"
              }]""")

        expect:
        correctResponse(accounts, status)
    }

    def "amount debitAccount, creditAccount order"() {
        given:

        def (accounts, status) = httpClient.post("""
            [
              {
                "amount": 10.90,
                "debitAccount": "32309111922661937852684864",
                "creditAccount": "06105023389842834748547303"
              },
              {
                "amount": 200.90,
                "debitAccount": "31074318698137062235845814",
                "creditAccount": "66105036543749403346524547"
              },
              {
                "amount": 50.10,
                "debitAccount": "66105036543749403346524547",
                "creditAccount": "32309111922661937852684864"
              }
            ]""")

        expect:
        correctResponse(accounts, status)
    }

    def "debitAccount, amount, creditAccount order"() {
        given:

        def (accounts, status) = httpClient.post("""
            [
              {
                "debitAccount": "32309111922661937852684864",
                "amount": 10.90,
                "creditAccount": "06105023389842834748547303"
              },
              {
                "debitAccount": "31074318698137062235845814",
                "amount": 200.90,
                "creditAccount": "66105036543749403346524547"
              },
              {
                "debitAccount": "66105036543749403346524547",
                "amount": 50.10,
                "creditAccount": "32309111922661937852684864"
              }
            ]""")

        expect:
        correctResponse(accounts, status)
    }

    def "debitAccount, creditAccount, amount order - minified"() {
        given:

        def (accounts, status) = httpClient.post("""
            [{"debitAccount":"32309111922661937852684864","creditAccount":"06105023389842834748547303","amount":10.9},{"debitAccount":"31074318698137062235845814","creditAccount":"66105036543749403346524547","amount":200.9},{"debitAccount":"66105036543749403346524547","creditAccount":"32309111922661937852684864","amount":50.1}]""")

        expect:
        correctResponse(accounts, status)
    }

    def "creditAccount, debitAccount, amount order - minified"() {
        given:

        def (accounts, status) = httpClient.post("""
            [{"creditAccount":"06105023389842834748547303","debitAccount":"32309111922661937852684864","amount":10.9},{"creditAccount":"66105036543749403346524547","debitAccount":"31074318698137062235845814","amount":200.9},{"creditAccount":"32309111922661937852684864","debitAccount":"66105036543749403346524547","amount":50.1}]""")

        expect:
        correctResponse(accounts, status)
    }

    def "creditAccount, amount, debitAccount order - minified"() {
        given:

        def (accounts, status) = httpClient.post("""
            [{"creditAccount":"06105023389842834748547303","amount":10.9,"debitAccount":"32309111922661937852684864"},{"creditAccount":"66105036543749403346524547","amount":200.9,"debitAccount":"31074318698137062235845814"},{"creditAccount":"32309111922661937852684864","amount":50.1,"debitAccount":"66105036543749403346524547"}]""")

        expect:
        correctResponse(accounts, status)
    }

    def "amount, creditAccount, debitAccount order - minified"() {
        given:

        def (accounts, status) = httpClient.post("""
            [{"amount":10.9,"creditAccount":"06105023389842834748547303","debitAccount":"32309111922661937852684864"},{"amount":200.9,"creditAccount":"66105036543749403346524547","debitAccount":"31074318698137062235845814"},{"amount":50.1,"creditAccount":"32309111922661937852684864","debitAccount":"66105036543749403346524547"}]""")

        expect:
        correctResponse(accounts, status)
    }

    def "amount, debitAccount, creditAccount order - minified"() {
        given:

        def (accounts, status) = httpClient.post("""
            [{"amount":10.9,"debitAccount":"32309111922661937852684864","creditAccount":"06105023389842834748547303"},{"amount":200.9,"debitAccount":"31074318698137062235845814","creditAccount":"66105036543749403346524547"},{"amount":50.1,"debitAccount":"66105036543749403346524547","creditAccount":"32309111922661937852684864"}]""")

        expect:
        correctResponse(accounts, status)
    }

    def "debitAccount, amount, creditAccount order - minified"() {
        given:

        def (accounts, status) = httpClient.post("""
            [{"debitAccount":"32309111922661937852684864","amount":10.9,"creditAccount":"06105023389842834748547303"},{"debitAccount":"31074318698137062235845814","amount":200.9,"creditAccount":"66105036543749403346524547"},{"debitAccount":"66105036543749403346524547","amount":50.1,"creditAccount":"32309111922661937852684864"}]""")

        expect:
        correctResponse(accounts, status)
    }

    def "request should be idempotent"() {
        when:
        def (accounts, status) = httpClient.post("""
            [{"debitAccount":"32309111922661937852684864","amount":10.9,"creditAccount":"06105023389842834748547303"},{"debitAccount":"31074318698137062235845814","amount":200.9,"creditAccount":"66105036543749403346524547"},{"debitAccount":"66105036543749403346524547","amount":50.1,"creditAccount":"32309111922661937852684864"}]""")

        then:
        correctResponse(accounts, status)

        when:
        (accounts, status) = httpClient.post("""
            [{"debitAccount":"32309111922661937852684864","amount":10.9,"creditAccount":"06105023389842834748547303"},{"debitAccount":"31074318698137062235845814","amount":200.9,"creditAccount":"66105036543749403346524547"},{"debitAccount":"66105036543749403346524547","amount":50.1,"creditAccount":"32309111922661937852684864"}]""")

        then: "response should be the same as in first request"
        correctResponse(accounts, status)
    }

    def correctResponse(Object accounts, int status) {
        assert status == 200

        assert accounts.collect { it.account } == [
                "06105023389842834748547303",
                "31074318698137062235845814",
                "32309111922661937852684864",
                "66105036543749403346524547"
        ]

        assert accounts.collect { it.debitCount } == [0, 1, 1, 1]
        assert accounts.collect { it.creditCount } == [1, 0, 1, 1]

        assert accounts[0].balance == 10.9
        assert accounts[1].balance == -200.9
        assert accounts[2].balance == 39.2
        assert accounts[3].balance == 150.8
        return true
    }
}
