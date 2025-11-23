package pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes;

import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class FunctionalProgramming extends Tool {
    public FunctionalProgramming(int position) {
        super("Programação Funcional", 1, position, "funcional.png");
    }

    @Override
    public void playerInteraction(Player player) {
        player.getTools().add(this);
    }
}
