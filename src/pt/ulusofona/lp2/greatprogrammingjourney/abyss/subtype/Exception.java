package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class Exception extends Abyss {
    public Exception(int position) {
        super("Exception", 2, position, null, "exception.png");
    }

    @Override
    public void playerInteraction(Player player) {

    }
}
