package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class InfiniteLoop extends Abyss {
    public InfiniteLoop(int position) {
        super("Ciclo Infinito", 8, position, null, "infinite-loop.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {


    }
}
