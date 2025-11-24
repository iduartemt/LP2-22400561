package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class BlueScreenOfDeath extends Abyss {

    public BlueScreenOfDeath(int position) {
        super("BSOD", 7, position, null, "bsod.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

        player.setIsAlive(false);

        for (Slot s : board.getSlots()) {
            s.getPlayers().remove(player);
            System.out.println("Jogador Derrotado");
        }
    }
}
