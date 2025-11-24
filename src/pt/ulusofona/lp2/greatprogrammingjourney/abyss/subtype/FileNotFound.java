package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class FileNotFound extends Abyss {
    public FileNotFound(int position) {
        super("File not found exception", 3, position, null, "file-not-found-exception.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

        Tool exceptionHandling = null;

        for (Tool t : player.getTools()) {
            if (player.getTools().contains(t)) {
                exceptionHandling = t;
                break;
            }
        }

        if (exceptionHandling != null) {
            player.getTools().remove(exceptionHandling);
            System.out.println(" File not found exception anulado por " + exceptionHandling.getName());
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

        Slot destination = board.encontraSlot(currentSlot.getNrSlot() - 3);
        currentSlot.removePlayer(player);
        destination.addPlayer(player);
    }
}
