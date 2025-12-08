package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.PlayerState;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class InfiniteLoop extends Abyss {
    public InfiniteLoop(int position) {
        super("Ciclo Infinito", 8, position, "infinite-loop.png", EventType.ABYSS);
    }

    @Override
    public String playerInteraction(Player player, Board board) {

        Tool functionalProgramming = null;

        for (Tool t : player.getTools()) {
            if (t.getId() == 1) {
                functionalProgramming = t;
                break;
            }
        }

        if (functionalProgramming != null) {
            player.getTools().remove(functionalProgramming);
            // REGRA: "não fica preso, mas também não liberta o que lá estava"
            // Por isso, fazemos return imediato.
            return "Ciclo Infinito anulado por " + functionalProgramming.getName();
        }

        // 2. ENCONTRAR O SLOT ATUAL
        Slot currentSlot = board.getSlotOfPlayer(player.getId());

        if (currentSlot == null) {
            return null;
        }

        // 3. VERIFICAR SE JÁ EXISTE ALGUÉM PRESO E LIBERTAR
        // (Iteramos sobre os jogadores no mesmo slot)
        for (Player p : currentSlot.getPlayers()) {
            // Se encontrarmos um jogador que não sejamos nós e que esteja preso
            if (p.getId() != player.getId() && p.getState() == PlayerState.PRESO) {
                p.setState(PlayerState.EM_JOGO);
                System.out.println("O jogador " + p.getName() + " foi libertado do Loop pelo novo jogador!");
            }
        }

        // 4. PRENDER O JOGADOR ATUAL
        player.setState(PlayerState.PRESO);
        return "Ciclo Infinito";
    }
}
