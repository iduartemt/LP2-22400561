package pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class ExceptionHandling extends Tool {
    public ExceptionHandling(int position) {
        super("Tratamento de Excepções", 3, position, "catch.png", EventType.TOOL);
    }

    @Override
    public void playerInteraction(Player player, Board board) {
        if (!player.hasTool(this)) {
            player.getTools().add(this);
        }
    }
}
