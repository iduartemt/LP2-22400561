package pt.ulusofona.lp2.greatprogrammingjourney.abyss;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.Event;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public abstract class Abyss extends Event {

    public Abyss(String name, int id, int position, String image, EventType type) {
        super(name, id, position,image, type);
    }

    @Override
    public abstract String playerInteraction(Player player, Board board);

}
