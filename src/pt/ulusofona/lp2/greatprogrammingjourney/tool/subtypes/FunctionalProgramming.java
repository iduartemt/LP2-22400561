package pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class FunctionalProgramming extends Tool {
    public FunctionalProgramming(int position) {
        super("Programação Funcional", 1, position, "functional.png", EventType.TOOL);
    }

    @Override
    public void playerInteraction(Player player, Board board) {
        player.addTool(this);
    }
}
