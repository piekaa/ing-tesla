package pl.piekoszek.ing.json;

import org.springframework.stereotype.Component;
import pl.piekoszek.ing.cache.Cache;
import pl.piekoszek.ing.endpoints.transactions.Transaction;

@Component
class TransactionsCache extends Cache<Transaction> {
    @Override
    protected Transaction[] createArray() {
        var array = new Transaction[100_100];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Transaction();
        }
        return array;
    }
}
