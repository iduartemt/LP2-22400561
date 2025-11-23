package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;


public class SyntaxError extends Abyss {

    public SyntaxError(int position) {
        super("Erro de sintaxe", 0, position, null, "syntax.png");
    }

    @Override
    public void playerInteraction(Player player, Board board) {

        Tool ide = null;

        //Verifica se tem a tool IDE
        for (Tool t : player.getTools()) {
            if (t.getName().equals("IDE")) {
                ide = t;
                break;
            }
        }

        //Se tiver o IDE remove e usa
        if (ide != null) {
            player.getTools().remove(ide);
            System.out.println("Erro de sintaxe anulado por " + ide.getName());
            return;
        }

        //Caso nao tenha, tem de recuar -1
        Slot currentSlot = null;

        for (Slot s : board.slots) {
            if (s.equals(player.getId())) {
                currentSlot = s;
                break;
            }
        }

        if (currentSlot == null) {
            return;
        }

        Slot destinationSlot = board.encontraSlot(currentSlot.nrSlot);

    currentSlot.removePlayer(player);
    destinationSlot.addPlayer(player);
    }
}

