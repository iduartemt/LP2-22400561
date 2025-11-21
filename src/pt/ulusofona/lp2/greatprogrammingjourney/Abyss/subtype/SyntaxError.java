package pt.ulusofona.lp2.greatprogrammingjourney.Abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class SyntaxError extends Abyss {

    public SyntaxError(int position) {
        super("Erro de sintaxe", 0, position, null, "syntax.png");
    }

    @Override
    public void playerInteraction(Player player) {
        for (Tool t : player.getTools()) {
            if (t.getName().equals("IDE")) {
                System.out.println("Erro de sintaxe anulado por " + t.getName());
            }
        }
    }
}

