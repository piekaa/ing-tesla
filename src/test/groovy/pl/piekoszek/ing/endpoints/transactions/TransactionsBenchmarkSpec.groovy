package pl.piekoszek.ing.endpoints.transactions

import com.fasterxml.jackson.core.JsonProcessingException
import pl.piekoszek.ing.endpoints.BaseBenchmark

class TransactionsBenchmarkSpec extends BaseBenchmark {
    def "benchmark transactions/report"() throws JsonProcessingException {
        given:
        def medianTime = startBenchmark()

        expect:
        medianTime < 3_000
    }

    @Override
    String requestBody() {
        def transactionJSONs = []
        for (int i = 0; i < 100_000; i++) {
            transactionJSONs.add(transactionJSON())
        }
        return "[${transactionJSONs.join(",")}]"
    }

    private static def transactionJSON() {
        return """{"creditAccount":"${randomAccountNumber()}","debitAccount":"${randomAccountNumber()}","amount":100}"""
    }

    static Random random = new Random(342)

    private static def randomAccountNumber() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("000000000000000000000");
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    @Override
    String requestPath() {
        return "transactions/report"
    }

    @Override
    int numberOfRequests() {
        return 10
    }
}
