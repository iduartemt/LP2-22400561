package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Board {
    List<Slot> slots = new ArrayList<>();


    public Board(List<Player> players, int worldSize) {
        addSlotList(worldSize);
        addPlayerSlotFirstSlot(players);
    }

    public Board(List<Player> players, int worldSize, String[][] abyssesAndTools) {
        addSlotList(worldSize);
        addPlayerSlotFirstSlot(players);
    }

    //adiciona cada slot a lista de slots
    private void addSlotList(int worldSize) {
        for (int i = 0; i < worldSize; i++) {
            slots.add(new Slot(i + 1));
        }
    }

    //Adiciona jogadores na primeira casa
    private void addPlayerSlotFirstSlot(List<Player> players) {
        for (Player player : players) {
            slots.get(0).addPlayer(player);
        }
    }

    int getNrTotalSlots() {
        return slots.size();
    }

    public Slot encontraSlot(int nrSlot) {
        for (Slot slot : slots) {
            if (slot.nrSlot == nrSlot) {
                return slot;
            }
        }
        return null;
    }

    public boolean hasPlayerOnLastSlot() {
        if (slots.isEmpty()) {
            return false;
        }
        int lastIndex = slots.size() - 1;
        return !slots.get(lastIndex).players.isEmpty();
    }

}
