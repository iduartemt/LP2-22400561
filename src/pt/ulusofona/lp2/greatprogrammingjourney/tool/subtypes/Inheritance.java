package pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class Inheritance extends Tool {
    public Inheritance(int position) {
        super("Herança", 0, position, "inheritance.png", EventType.TOOL);
    }

    @Override
    public void playerInteraction(Player player, Board board) {
        if (!player.hasTool(this)) {
            player.getTools().add(this);
        }    }
}
