package pt.ulusofona.lp2.greatprogrammingjourney.Abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class InfiniteLoop extends Abyss {
    public InfiniteLoop(int position) {
        super("Ciclo Infinito", 8, position, null, "infinite-loop.png");
    }

    @Override
    public void playerInteraction() {

    }
}
