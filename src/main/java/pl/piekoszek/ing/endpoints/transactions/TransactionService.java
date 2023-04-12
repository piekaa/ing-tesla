package pl.piekoszek.ing.endpoints.transactions;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import pl.piekoszek.ing.mutable.collections.SortedNoAllocationsHashMap;

import java.util.Optional;

@Component
class TransactionService {
    private final AccountStatusCache accountStatusCache;
    private final ThreadLocal<SortedNoAllocationsHashMap<AccountStatus>> threadLocalAccounts = new ThreadLocal<>();

    TransactionService(AccountStatusCache accountStatusCache) {
        this.accountStatusCache = accountStatusCache;
    }

    SortedNoAllocationsHashMap<AccountStatus> calculateBalances(@RequestBody Transaction[] transactions) {
        var accounts = Optional.ofNullable(threadLocalAccounts.get())
                .orElseGet(() -> new SortedNoAllocationsHashMap<>(100_100));
        accounts.clear();

        for (int i = 0; transactions[i] != null; i++) {
            var transaction = transactions[i];
            var creditor = accounts.getOrDefault(transaction.creditAccount,
                    () -> accountStatusCache.use().withAccount(transaction.creditAccount));
            var debtor = accounts.getOrDefault(transaction.debitAccount,
                    () -> accountStatusCache.use().withAccount(transaction.debitAccount));

            accounts.put(creditor);
            accounts.put(debtor);

            creditor.balance.add(transaction.amount);
            debtor.balance.subtract(transaction.amount);

            creditor.creditCount++;
            debtor.debitCount++;
        }

        threadLocalAccounts.set(accounts);
        return accounts;
    }
}
