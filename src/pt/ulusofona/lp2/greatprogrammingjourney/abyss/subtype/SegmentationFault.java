package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

import java.util.ArrayList;
import java.util.List;

public class SegmentationFault extends Abyss {
    public SegmentationFault(int position) {
        super("Segmentation Fault", 9, position, null, "core-dumped.png", EventType.ABYSS);
    }

    @Override
    public String playerInteraction(Player player, Board board) {

        for (Slot slot : board.getSlots()) {
            if (slot.getPlayers().contains(player)) {

                int destinySlot = slot.getNrSlot() - 3;

                if (destinySlot < 1) {
                    destinySlot = 1;
                }

                Slot destinationSlot = board.encontraSlot(destinySlot);

                if (slot.getPlayers().size() >= 2) {
                    List<Player> players = new ArrayList<>(slot.getPlayers());

                    for (Player p : players) {
                        slot.removePlayer(p);
                        destinationSlot.addPlayer(p);
                    }
                }
            }
        }
        return null;
    }
}
