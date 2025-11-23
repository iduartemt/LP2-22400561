package pt.ulusofona.lp2.greatprogrammingjourney.abyss;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.Event;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public abstract class Abyss extends Event {
    Tool counter;

    public Abyss(String name, int id, int position, Tool counter, String image, EventType type) {
        super(name, id, position,image, type);
        this.counter = counter;
    }

    @Override
    public abstract void playerInteraction(Player player, Board board);

}
