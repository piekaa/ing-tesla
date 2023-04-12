package pl.piekoszek.ing.endpoints.transactions;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piekoszek.ing.mutable.collections.SortedNoAllocationsHashMap;

@RestController
@RequestMapping("transactions/report")
class TransactionsController {
    private final TransactionService transactionService;

    TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    SortedNoAllocationsHashMap<AccountStatus> calculateBalances(@RequestBody Transaction[] transactions) {
        return transactionService.calculateBalances(transactions);
    }
}
