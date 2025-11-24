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

        Tool unitTests = null;

        for (Tool t : player.getTools()) {
            if (t.getId() == 2) {
                unitTests = t;
                break;
            }
        }

        if (unitTests != null) {
            player.getTools().remove(unitTests);
            System.out.println("Efeitos secundários anulado por " + unitTests.getName());
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
        Slot destinationSlot = board.encontraSlot(posTwoMovesAgo);

        if (destinationSlot == null) {
            return;
        }

        if (posTwoMovesAgo < 1) {
            destinationSlot = board.encontraSlot(1);
            currentSlot.removePlayer(player);
            destinationSlot.addPlayer(player);
        }

        currentSlot.removePlayer(player);
        destinationSlot.addPlayer(player);
    }
}
