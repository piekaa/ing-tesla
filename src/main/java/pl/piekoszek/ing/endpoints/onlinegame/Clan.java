package pl.piekoszek.ing.endpoints.onlinegame;

public class Clan implements Comparable<Clan> {
    public int numberOfPlayers;
    public int points;

    public Clan() {
    }

    @Override
    public int compareTo(Clan o) {
        if (points == o.points) {
            return numberOfPlayers - o.numberOfPlayers;
        }
        return o.points - points;
    }
}
