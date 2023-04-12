package pl.piekoszek.ing.endpoints.onlinegame;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/onlinegame/calculate")
class OnlineGameController {
    private final OnlineGameService onlineGameService;

    OnlineGameController(OnlineGameService onlineGameService) {
        this.onlineGameService = onlineGameService;
    }

    @PostMapping
    List<List<Clan>> letInOrder(@RequestBody OnlineGameRequest request) {
        return onlineGameService.letInOrder(request.clans());
    }
}
