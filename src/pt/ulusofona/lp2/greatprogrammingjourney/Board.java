package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Board {
    List<Slot> slots = new ArrayList<>();

    public Board(List<Player> players, int worldSize) {
        addSlotList(worldSize);
        addPlayerSlotFirstSlot(players);
    }

    private void addSlotList(int worldSize) {
        for (int i = 0; i < worldSize; i++) {
            slots.add(new Slot(i + 1));
        }
    }

    private void addPlayerSlotFirstSlot(List<Player> players) {
        for (Player player: players){
            slots.get(0).addPlayer(player);
        }
    }

    int getNrTotalSlots() {
        return slots.size();
    }


}
