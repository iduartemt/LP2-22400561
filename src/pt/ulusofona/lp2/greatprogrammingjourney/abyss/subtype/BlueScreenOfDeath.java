package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class BlueScreenOfDeath extends Abyss {

    public BlueScreenOfDeath(int position) {
        super("BSOD", 7, position, null, "bsdo.png");
    }

    @Override
    public void playerInteraction(Player player) {
    }
}
