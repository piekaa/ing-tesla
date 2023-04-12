package pl.piekoszek.ing.endpoints.onlinegame;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class OnlineGameService {
    List<List<Clan>> letInOrder(List<Clan> clans) {
        var waitingList = new ClanWaitingList(clans);
        var result = new ArrayList<List<Clan>>();
        for (var addedClans = 0; addedClans < clans.size(); ) {
            var group = new ArrayList<Clan>();
            for (var spaceInGroup = waitingList.largestGroup(); ; ) {
                var clanOptional = waitingList.letInTopScore(spaceInGroup);
                if (clanOptional.isEmpty()) {
                    break;
                }
                var clan = clanOptional.get();
                spaceInGroup -= clan.numberOfPlayers;
                group.add(clan);
                addedClans++;
            }
            result.add(group);
        }
        return result;
    }
}
