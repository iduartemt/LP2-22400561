package pt.ulusofona.lp2.greatprogrammingjourney.tool;

import pt.ulusofona.lp2.greatprogrammingjourney.Event;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public abstract class Tool extends Event {

    public Tool(String name, int id, int position, String image) {
        super(name, id, position, image);
    }

    @Override
    public abstract void playerInteraction(Player player);
}
