package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class SecundaryEffects extends Abyss {
    public SecundaryEffects(int position) {
        super("Efeitos Secundários", 6, position, null, "secondary-effects.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

    }
}
