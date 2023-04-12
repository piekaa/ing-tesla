package pl.piekoszek.ing.endpoints.atm

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import pl.piekoszek.ing.http.HttpClient
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AtmApiSpec extends Specification {
    @LocalServerPort
    private int port

    private HttpClient httpClient

    def setup() {
        httpClient = new HttpClient(port, "atms/calculateOrder")
    }

    def "Calculate ATM service order"() {
        given:
        def (order, status) = httpClient.post("""
            [
              {
                "region": 4,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 1,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 2,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 3,
                "requestType": "PRIORITY",
                "atmId": 2
              },
              {
                "region": 3,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 2,
                "requestType": "SIGNAL_LOW",
                "atmId": 1
              },
              {
                "region": 5,
                "requestType": "STANDARD",
                "atmId": 2
              },
              {
                "region": 5,
                "requestType": "FAILURE_RESTART",
                "atmId": 1
              }
            ]
            """)

        expect:
        status == 200

        order.collect { it.region } == [1, 2, 3, 3, 4, 5, 5]
        order.collect { it.atmId } == [1, 1, 2, 1, 1, 1, 2]
    }


    def "Calculate ATM service order, slightly bigger request"() {
        given:
        def (order, status) = httpClient.post("""
            [
               {
                 "region": 1,
                 "requestType": "STANDARD",
                 "atmId": 2
               },
               {
                 "region": 1,
                 "requestType": "STANDARD",
                 "atmId": 1
               },
               {
                 "region": 2,
                 "requestType": "PRIORITY",
                 "atmId": 3
               },
               {
                 "region": 3,
                 "requestType": "STANDARD",
                 "atmId": 4
               },
               {
                 "region": 4,
                 "requestType": "STANDARD",
                 "atmId": 5
               },
               {
                 "region": 5,
                 "requestType": "PRIORITY",
                 "atmId": 2
               },
               {
                 "region": 5,
                 "requestType": "STANDARD",
                 "atmId": 1
               },
               {
                 "region": 3,
                 "requestType": "SIGNAL_LOW",
                 "atmId": 2
               },
               {
                 "region": 2,
                 "requestType": "SIGNAL_LOW",
                 "atmId": 1
               },
               {
                 "region": 3,
                 "requestType": "FAILURE_RESTART",
                 "atmId": 1
               }
             ]
            """)

        expect:
        status == 200

        order.collect { it.region } == [1, 1, 2, 2, 3, 3, 3, 4, 5, 5]
        order.collect { it.atmId } == [2, 1, 3, 1, 1, 2, 4, 5, 2, 1]
    }
}
