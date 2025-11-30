package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class Exception extends Abyss {
    public Exception(int position) {
        super("Exception", 2, position, null, "exception.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

        Tool ExpectionHandling = null;

        for (Tool t : player.getTools()) {
            if (t.getId() == 3) {
                ExpectionHandling = t;
                break;
            }
        }

        if (ExpectionHandling != null) {
            player.getTools().remove(ExpectionHandling);
            System.out.println("Exception anulado por " + ExpectionHandling.getName());
            return;
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

        //Recuar 2 casas
        Slot destination = board.encontraSlot(currentSlot.getNrSlot() - 2);
        //remover o player da slot atual
        currentSlot.removePlayer(player);
        //adicionar o player à nova slot
        destination.addPlayer(player);

    }
}
