package pl.piekoszek.ing.endpoints.onlinegame;

import java.util.List;

record OnlineGameRequest(int groupCount, List<Clan> clans) {
}
