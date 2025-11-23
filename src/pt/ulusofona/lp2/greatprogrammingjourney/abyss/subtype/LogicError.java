package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class LogicError extends Abyss {
    public LogicError(int position) {
        super("Erro de Lógica", 1, position, null, "logic.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

        Tool unitTests = null;

        for (Tool t : player.getTools()) {
            if (t.getId() == 2) {
                unitTests = t;
                break;
            }
        }
    }
}
