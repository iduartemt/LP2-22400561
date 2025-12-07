package pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class TeacherHelp extends Tool {
    public TeacherHelp(int position) {
        super("Ajuda do Professor", 5, position, "ajuda-professor.png", EventType.TOOL);
    }

    @Override
    public void playerInteraction(Player player, Board board) {
        if (!player.hasTool(this)) {
            player.getTools().add(this);
        }    }
}
