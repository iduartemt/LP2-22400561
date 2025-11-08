package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Slot {
    int nrSlot;
    List<Player> players = new ArrayList<>();

    public Slot(int nrSlot) {
        this.nrSlot = nrSlot;
    }

    void addPlayer(Player player){
        players.add(player);
    }

    void removePlayer(Player player){
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
}
