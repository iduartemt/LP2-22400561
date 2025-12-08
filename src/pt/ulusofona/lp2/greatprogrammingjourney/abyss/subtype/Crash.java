package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class Crash extends Abyss {

    public Crash(int position) {
        super("Crash", 4, position, "crash.png", EventType.ABYSS);
    }

    @Override
    public String playerInteraction(Player player, Board board) {

        Slot currentSlot = null;

        for (Slot s : board.getSlots()) {
            if (s.getPlayers().contains(player)) {
                currentSlot = s;
                break;
            }
        }

        if (currentSlot == null) {
            return null;
        }

        Slot destinationSlot = board.encontraSlot(1);

        currentSlot.removePlayer(player);
        destinationSlot.addPlayer(player);
        return "Recua para a casa inicial";
    }
}
