package pl.piekoszek.ing.endpoints.transactions;

import pl.piekoszek.ing.cache.Resettable;
import pl.piekoszek.ing.mutable.Decimal;

public class Transaction implements Resettable {
    public String debitAccount;
    public String creditAccount;
    public Decimal amount;

    public Transaction() {
        amount = new Decimal();
    }

    @Override
    public void reset() {
        amount.reset();
    }
}
