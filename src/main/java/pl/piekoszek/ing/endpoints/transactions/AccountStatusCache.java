package pl.piekoszek.ing.endpoints.transactions;

import org.springframework.stereotype.Component;
import pl.piekoszek.ing.cache.Cache;

@Component
class AccountStatusCache extends Cache<AccountStatus> {
    @Override
    protected AccountStatus[] createArray() {
        var array = new AccountStatus[100_100];
        for (int i = 0; i < array.length; i++) {
            array[i] = new AccountStatus();
        }
        return array;
    }
}
