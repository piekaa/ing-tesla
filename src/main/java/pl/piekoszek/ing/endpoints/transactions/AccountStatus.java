package pl.piekoszek.ing.endpoints.transactions;

import pl.piekoszek.ing.cache.Resettable;
import pl.piekoszek.ing.mutable.Decimal;
import pl.piekoszek.ing.mutable.collections.MapEntry;

import java.io.PrintWriter;

class AccountStatus extends MapEntry implements Resettable {
    public String account;
    public int debitCount;
    public int creditCount;
    public Decimal balance;

    AccountStatus() {
        balance = new Decimal();
    }

    public void reset() {
        balance.reset();
        creditCount = 0;
        debitCount = 0;
    }

    @Override
    public int compareTo(MapEntry o) {
        if (o instanceof AccountStatus other) {
            return account.compareTo(other.account);
        }
        return 0;
    }

    AccountStatus withAccount(String account) {
        this.account = account;
        return this;
    }

    @Override
    protected String key() {
        return account;
    }

    @Override
    public void printJSON(PrintWriter printWriter) {

        printWriter.print("{\"account\":\"");
        printWriter.print(account);
        printWriter.print("\",");

        printWriter.print("\"debitCount\":");
        printWriter.print(debitCount);
        printWriter.print(',');

        printWriter.print("\"creditCount\":");
        printWriter.print(creditCount);
        printWriter.print(',');

        printWriter.print("\"balance\":");
        balance.print(printWriter);
        printWriter.print('}');
    }
}
