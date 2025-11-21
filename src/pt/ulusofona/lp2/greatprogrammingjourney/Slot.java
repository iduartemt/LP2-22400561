package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Slot {
    int nrSlot;
    List<Player> players = new ArrayList<>();
    Event event;

    public Slot(int nrSlot) {
        this.nrSlot = nrSlot;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    void removePlayer(Player player) {
        players.remove(player);
    }

    Player findPlayerByID(int currentPlayerId) {
        for (Player p : players) {
            if (p.id == currentPlayerId) {
                return p;
            }
        }
        return null;
    }

    public String buildPlayerIds() {
        if (players.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Player player : players) {
            if (!sb.isEmpty()) {
                sb.append(",");
            }
            sb.append(player.id);
        }
        return sb.toString();
    }

    public void addEvent(Event event) {
        this.event = event;
    }

    public void removeEvent() {
        this.event = null;
    }

    public Event getEvent() {
        return event;
    }

}
