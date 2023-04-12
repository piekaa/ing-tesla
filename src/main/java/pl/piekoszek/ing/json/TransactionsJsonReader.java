package pl.piekoszek.ing.json;

import org.springframework.stereotype.Component;
import pl.piekoszek.ing.endpoints.transactions.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class TransactionsJsonReader {
    private final ThreadLocal<Transaction[]> threadLocalTransactions = new ThreadLocal<>();

    private final TransactionsCache transactionsCache;

    TransactionsJsonReader(TransactionsCache transactionsCache) {
        this.transactionsCache = transactionsCache;
    }

    Transaction[] read(InputStream inputStream) throws IOException, SingleItemException, NoItemsException {
        var transactions = Optional.ofNullable(threadLocalTransactions.get())
                .orElseGet(() -> new Transaction[100_100]);

        var analyzer = new FormattingAnalyzer();
        var formatting = analyzer.analyze(inputStream);

        var deserializer = switch (formatting.fields().stream()
                .map(field -> field.substring(0, 1))
                .collect(Collectors.joining())) {

            case "dac" -> new TransactionDACDeserializer(formatting);
            case "cad" -> new TransactionCADDeserializer(formatting);
            case "dca" -> new TransactionDCADeserializer(formatting);
            case "cda" -> new TransactionCDADeserializer(formatting);
            case "acd" -> new TransactionACDDeserializer(formatting);
            case "adc" -> new TransactionADCDeserializer(formatting);
            default -> new TransactionNoopDeserializer(formatting);
        };

        var is = new SequenceInputStream(formatting.usedData(), inputStream);

        while (is.read() != '{') {
        }

        var i = 0;
        for (var end = false; !end; i++) {
            var transaction = transactionsCache.use();
            end = deserializer.deserialize(is, transaction);
            transactions[i] = transaction;
        }
        transactions[i + 1] = null;

        threadLocalTransactions.set(transactions);

        return transactions;
    }
}
