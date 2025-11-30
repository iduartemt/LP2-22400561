package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class SecundaryEffects extends Abyss {
    public SecundaryEffects(int position) {
        super("Efeitos secundários", 6, position, null, "secondary-effects.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

        Tool FunctionalProgramming = null;

        for (Tool t : player.getTools()) {
            if (t.getId() == 1) {
                FunctionalProgramming = t;
                break;
            }
        }

        if (FunctionalProgramming != null) {
            player.getTools().remove(FunctionalProgramming);
            System.out.println("Efeitos secundários anulado por " + FunctionalProgramming.getName());
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

        int posTwoMovesAgo = player.getPositionTwoMovesAgo();

        if (posTwoMovesAgo < 1) {
            posTwoMovesAgo = 1;
        }

        Slot destinationSlot = board.encontraSlot(posTwoMovesAgo);

        if (destinationSlot == null) {
            return;
        }

        currentSlot.removePlayer(player);
        destinationSlot.addPlayer(player);
    }
}
