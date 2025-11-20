package pt.ulusofona.lp2.greatprogrammingjourney.Abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class BlueScreenOfDeath extends Abyss {

    public BlueScreenOfDeath(int position) {
        super("BSOD", 7, position, null, "bsdo.png");
    }

    @Override
    public void playerInteraction() {
    }
}
