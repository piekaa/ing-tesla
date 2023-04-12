package pl.piekoszek.ing.endpoints

import com.fasterxml.jackson.core.JsonProcessingException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.util.StopWatch
import pl.piekoszek.ing.http.HttpClient
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseBenchmark extends Specification {
    @LocalServerPort
    private int port

    private HttpClient httpClient

    def setup() {
        httpClient = new HttpClient(port, requestPath())
    }

    def startBenchmark() throws JsonProcessingException {

        def runs = numberOfRequests()
        def body = requestBody()

        def times = []

        def stopwatch = new StopWatch()
        stopwatch.start()
        for (int i = 0; i < runs; i++) {
            request(body)
            stopwatch.stop()
            times.add(stopwatch.getTotalTimeMillis())
            stopwatch = new StopWatch()
            stopwatch.start()
        }

        def medianTime = times.sort()[times.size() / 2]

        println(medianTime)
        return medianTime
    }

    abstract String requestBody()
    abstract String requestPath()
    abstract int numberOfRequests()

    private def request(String body) {
        def (_, status) = httpClient.post(body)
        assert status == 200
    }
}
