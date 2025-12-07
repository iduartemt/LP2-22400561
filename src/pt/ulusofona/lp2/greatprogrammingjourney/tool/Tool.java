package pt.ulusofona.lp2.greatprogrammingjourney.tool;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.Event;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public abstract class Tool extends Event {

    public Tool(String name, int id, int position, String image, EventType type) {
        super(name, id, position, image, type);
    }

    @Override
    public abstract String playerInteraction(Player player, Board board);
}
