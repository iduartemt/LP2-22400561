package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.Slot;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class InfiniteLoop extends Abyss {
    public InfiniteLoop(int position) {
        super("Ciclo Infinito", 8, position, null, "infinite-loop.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

        Tool unitTests = null;
        for (Tool t : player.getTools()) {
            if (t.getId() == 2) {
                unitTests = t;
                break;
            }
        }

        if (unitTests != null) {
            player.getTools().remove(unitTests);
            System.out.println("Ciclo Infinito anulado por " + unitTests.getName());
            // Se usou a ferramenta, não fica preso.
            // O enunciado diz: "não fica preso, mas também não liberta o que lá estava".
            // Por isso fazemos return imediato.
            return;
        }

        // 2. ENCONTRAR O SLOT ATUAL
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

        // 3. VERIFICAR SE JÁ EXISTE ALGUÉM PRESO E LIBERTAR
        // (Iteramos sobre os jogadores no mesmo slot)
        for (Player p : currentSlot.getPlayers()) {
            // Se encontrarmos um jogador que não sejamos nós e que esteja preso
            if (p != player && p.isTrapped()) {
                p.setTrapped(false);
                System.out.println("O jogador " + p.getName() + " foi libertado do Loop!");
            }
        }

        // 4. PRENDER O JOGADOR ATUAL
        player.setTrapped(true);
        System.out.println(player.getName() + " entrou num Infinite Loop e ficou preso!");
    }
}
