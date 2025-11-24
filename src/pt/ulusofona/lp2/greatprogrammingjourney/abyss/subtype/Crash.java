package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class Crash extends Abyss {

    public Crash(int position) {
        super("Crash", 4, position, null, "crash.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

        Tool ide = null;

        for (Tool t : player.getTools()) {
            if (t.getId()==4) {
                ide = t;
                break;
            }
        }

        if (ide != null) {
            player.getTools().remove(ide);
            System.out.println("Crash anulado por " + ide.getName());
        }

        Slot currentSlot = null;

        for (Slot s : board.getSlots()) {
            if (s.getPlayers().contains(player)) {
                currentSlot = s;
                break;
            }
        }

        if (currentSlot == null) {
            return;
        }

        Slot destinationSlot = board.encontraSlot(1);

        currentSlot.removePlayer(player);
        destinationSlot.addPlayer(player);

    }
}
