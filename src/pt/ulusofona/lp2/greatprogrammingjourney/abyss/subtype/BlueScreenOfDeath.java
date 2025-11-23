package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class BlueScreenOfDeath extends Abyss {

    public BlueScreenOfDeath(int position) {
        super("BSOD", 7, position, null, "bsod.png");
    }

    @Override
    public void playerInteraction(Player player, Board board) {
    }
}
