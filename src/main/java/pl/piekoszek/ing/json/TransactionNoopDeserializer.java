package pl.piekoszek.ing.json;

import pl.piekoszek.ing.endpoints.transactions.Transaction;

import java.io.IOException;
import java.io.InputStream;

class TransactionNoopDeserializer implements Deserializer<Transaction> {
    TransactionNoopDeserializer(Formatting formatting) {
    }

    @Override
    public boolean deserialize(InputStream inputStream, Transaction toFill) throws IOException {
        return true;
    }
}
