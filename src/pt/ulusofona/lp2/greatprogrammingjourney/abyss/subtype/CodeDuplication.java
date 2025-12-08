package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class CodeDuplication extends Abyss {

    public CodeDuplication(int position) {
        super("Código duplicado", 5, position, "duplicated-code.png", EventType.ABYSS);
    }

    @Override
    public String playerInteraction(Player player, Board board) {

        Tool inheritance = null;

        for (Tool t : player.getTools()) {
            if (t.getId() == 0) {
                inheritance = t;
                break;
            }
        }

        if (inheritance != null) {
            player.getTools().remove(inheritance);
            return "Código duplicado anulado por " + inheritance.getName();
        }

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

        Slot destinationSlot = board.encontraSlot(currentSlot.getNrSlot() - player.getLastDiceValue());

        currentSlot.removePlayer(player);
        destinationSlot.addPlayer(player);
        return "Recua para a casa anterior";
    }
}
