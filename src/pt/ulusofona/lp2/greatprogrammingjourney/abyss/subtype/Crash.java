package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class Crash extends Abyss {

    public Crash(int position) {
        super("crash", 4, position, null, "crash.png");
    }

    @Override
    public void playerInteraction(Player player, Board board) {

    }
}
