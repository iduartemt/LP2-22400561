package pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class Ide extends Tool {
    public Ide(int position) {
        super("IDE", 4, position, "IDE.png");
    }

    @Override
    public void playerInteraction(Player player, Board board) {
        player.getTools().add(this);
    }
}
