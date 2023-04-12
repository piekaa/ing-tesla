package pl.piekoszek.ing.endpoints.atm;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/atms/calculateOrder")
class AtmController {
    private final AtmService atmService;

    AtmController(AtmService atmService) {
        this.atmService = atmService;
    }

    @PostMapping
    List<ATM> calculate(@RequestBody Task[] tasks) {
        return atmService.calculate(tasks);
    }
}
