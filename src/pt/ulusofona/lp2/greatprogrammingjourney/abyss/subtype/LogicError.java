package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class LogicError extends Abyss {
    public LogicError(int position) {
        super("Erro de Lógica", 1, position, null, "logic.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

        Tool unitTests = null;

        for (Tool t : player.getTools()) {
            if (t.getId() == 1) {
                unitTests = t;
                break;
            }
        }

        if (unitTests != null) {
            player.getTools().remove(unitTests);
            System.out.println("Erro de lógica anulado por " + unitTests.getName());
        }

        Slot currentSlot = null;

        for (Slot s : board.getSlots()) {
            if (s.getPlayers().contains(player)) {
                currentSlot = s;
                break;
            }
        }

        if (currentSlot == null) {
            return;
        }

        //Dividir o valor do dado por 2
        int splitNrSpaces = player.getLastDiceValue()/2;
        //subtrair a posicao do slot atual pelo valor do dado arrendondado por baixo
        int findDestinationSlot = currentSlot.getNrSlot() - splitNrSpaces;

        //Guardar o slot para onde o jogador vai recuar
        Slot destinationSlot = board.encontraSlot(findDestinationSlot);
        //remover o jogador da posicao atual
        currentSlot.removePlayer(player);
        //colocar o jogador na sua nova posicao (recuo)
        destinationSlot.addPlayer(player);

    }
}