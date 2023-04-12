package pl.piekoszek.ing.endpoints.onlinegame;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

class ClanWaitingList {
    private final PriorityQueue<Clan>[] waitingClansByNumberOfPlayers;
    private int largestGroup;

    ClanWaitingList(List<Clan> clans) {
        waitingClansByNumberOfPlayers = new PriorityQueue[1001];

        for (int i = 0; i < waitingClansByNumberOfPlayers.length; i++) {
            waitingClansByNumberOfPlayers[i] = new PriorityQueue<>();
        }

        for (Clan clan : clans) {
            largestGroup = Math.max(largestGroup, clan.numberOfPlayers);
            waitingClansByNumberOfPlayers[clan.numberOfPlayers].add(clan);
        }
    }

    int largestGroup() {
        for (int i = largestGroup; i > 0; i--) {
            if (!waitingClansByNumberOfPlayers[i].isEmpty()) {
                largestGroup = i;
                return largestGroup;
            }
        }
        throw new IllegalStateException();
    }

    Optional<Clan> letInTopScore(int maxNumberOfPlayers) {
        var topScoreIndex = 0;

        var nullClan = new Clan();
        nullClan.points = 0;
        nullClan.numberOfPlayers = 10000;

        var topClanSoFar = nullClan;

        for (int i = maxNumberOfPlayers; i > 0; i--) {

            if (waitingClansByNumberOfPlayers[i].isEmpty()) {
                continue;
            }

            var candidateClan = waitingClansByNumberOfPlayers[i].peek();
            if (candidateClan.compareTo(topClanSoFar) < 0) {
                topClanSoFar = candidateClan;
                topScoreIndex = i;
            }
        }

        if (topClanSoFar == nullClan) {
            return Optional.empty();
        }

        return Optional.of(waitingClansByNumberOfPlayers[topScoreIndex].poll());
    }
}
