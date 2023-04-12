package pl.piekoszek.ing.endpoints.onlinegame

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import pl.piekoszek.ing.endpoints.BaseBenchmark

class OnlineGameBenchmarkSpec extends BaseBenchmark {
    def "benchmark onlinegame/calculate"() throws JsonProcessingException {
        given:
        def medianTime = startBenchmark()

        expect:
        medianTime < 3_000
    }

    static Random random = new Random(642)

    String requestBody() throws JsonProcessingException {

        def body = new OnlineGameRequest(1_000, [])

        for (int i = 0; i < 20_000; i++) {
            def clan = new Clan()
            clan.points = random.nextInt(999_990) + 1
            clan.numberOfPlayers = random.nextInt(999) + 1
            body.clans().add(clan)
        }
        return new ObjectMapper().writeValueAsString(body)
    }

    @Override
    String requestPath() {
        return "onlinegame/calculate"
    }

    @Override
    int numberOfRequests() {
        return 10
    }
}
