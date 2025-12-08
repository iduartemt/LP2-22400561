package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.PlayerState;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;

public class BlueScreenOfDeath extends Abyss {

    public BlueScreenOfDeath(int position) {
        super("Blue Screen of Death", 7, position, "bsod.png", EventType.ABYSS);
    }

    @Override
    public String playerInteraction(Player player, Board board) {
        player.setState(PlayerState.DERROTADO);
        return "Blue Screen of Death! O jogador morreu :(";
    }
}
