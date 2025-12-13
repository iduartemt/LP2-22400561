package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Slot {
    private final int nrSlot;
    private final List<Player> players = new ArrayList<>();
    Event event;

    public Slot(int nrSlot) {
        this.nrSlot = nrSlot;
    }

    //=====================================================GETTERS======================================================

    public int getNrSlot() {
        return nrSlot;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Event getEvent() {
        return event;
    }

    //=====================================================METODOS======================================================

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    Player findPlayerByID(int currentPlayerId) {
        for (Player p : players) {
            if (p.getId() == currentPlayerId) {
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
            sb.append(player.getId());
        }
        return sb.toString();
    }

    public void addEvent(Event event) {
        this.event = event;
    }

    public void removeEvent() {
        this.event = null;
    }

}
