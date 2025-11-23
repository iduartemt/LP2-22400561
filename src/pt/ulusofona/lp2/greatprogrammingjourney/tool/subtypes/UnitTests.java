package pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class UnitTests extends Tool {
    public UnitTests(int position) {
        super("Testes Unitários", 2, position, "unit-tests.png");
    }

    @Override
    public void playerInteraction(Player player, Board board) {
        player.getTools().add(this);
    }
}
