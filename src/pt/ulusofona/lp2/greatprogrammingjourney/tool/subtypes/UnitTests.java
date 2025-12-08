package pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class UnitTests extends Tool {
    public UnitTests(int position) {
        super("Testes Unitários", 2, position, "unit-tests.png", EventType.TOOL);
    }

    @Override
    public String playerInteraction(Player player, Board board) {
        if (!player.hasTool(this)) {
            player.getTools().add(this);
        }
        return "jogador agarrou " + getName();
    }
}
