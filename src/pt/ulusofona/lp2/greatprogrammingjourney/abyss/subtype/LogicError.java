package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class LogicError extends Abyss {
    public LogicError(int position) {
        super("Erro de Lógica", 1, position, null, "logic.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

    }
}
