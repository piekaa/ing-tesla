package pl.piekoszek.ing.endpoints.onlinegame

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import pl.piekoszek.ing.http.HttpClient
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OnlineGameApiSpec extends Specification {
    @LocalServerPort
    private int port

    private HttpClient httpClient

    def setup() {
        httpClient = new HttpClient(port, "onlinegame/calculate")
    }

    def "let in players in groups based on their points"() {
        given:
        def (groups, status) = httpClient.post("""
            {
              "groupCount": 6,
              "clans": [
                {
                  "numberOfPlayers": 4,
                  "points": 50
                },
                {
                  "numberOfPlayers": 2,
                  "points": 70
                },
                {
                  "numberOfPlayers": 6,
                  "points": 60
                },
                {
                  "numberOfPlayers": 1,
                  "points": 15
                },
                {
                  "numberOfPlayers": 5,
                  "points": 40
                },
                {
                  "numberOfPlayers": 3,
                  "points": 45
                },
                {
                  "numberOfPlayers": 1,
                  "points": 12
                },
                {
                  "numberOfPlayers": 4,
                  "points": 40
                }
              ]
            }
            """)

        expect:
        status == 200

        groups[0].collect { it.numberOfPlayers } == [2, 4]
        groups[0].collect { it.points } == [70, 50]

        groups[1].collect { it.numberOfPlayers } == [6]
        groups[1].collect { it.points } == [60]

        groups[2].collect { it.numberOfPlayers } == [3, 1, 1]
        groups[2].collect { it.points } == [45, 15, 12]

        groups[3].collect { it.numberOfPlayers } == [4]
        groups[3].collect { it.points } == [40]

        groups[4].collect { it.numberOfPlayers } == [5]
        groups[4].collect { it.points } == [40]
    }
}
