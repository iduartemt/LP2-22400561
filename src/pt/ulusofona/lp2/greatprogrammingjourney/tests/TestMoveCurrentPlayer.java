package pt.ulusofona.lp2.greatprogrammingjourney.tests;

import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

import static org.junit.jupiter.api.Assertions.*;

public class TestMoveCurrentPlayer {

    @Test
    void testMoveCurrentPlayerValidLanguage() {
        GameManager gm = new GameManager();
        String[][] playersInfo = {
                {"1", "Duarte", "C", "Blue"},
                {"2", "Adversario", "Assembly", "Green"
                }
        };

        boolean init = gm.createInitialBoard(playersInfo, 79);
        assertTrue(init, "O tabuleiro devia ter sido criado com sucesso");

        assertEquals(1, gm.getCurrentPlayerID());

        boolean moveInvalid = gm.moveCurrentPlayer(4);
        assertFalse(moveInvalid, "Jogadores com C nao podem andar 4 casas");
        String message = gm.reactToAbyssOrTool();

        assertEquals(2, gm.getCurrentPlayerID());
        assertNull(message);

        boolean moveValid = gm.moveCurrentPlayer(3);
        message = gm.reactToAbyssOrTool();
        assertFalse(moveValid, "Jogadores de C devem conseguir mover 3 casas");

        assertEquals(1, gm.getCurrentPlayerID());
        assertNull(message);

        moveInvalid=gm.moveCurrentPlayer(3);
        message = gm.reactToAbyssOrTool();
        assertEquals(2, gm.getCurrentPlayerID());
        assertFalse(moveInvalid, "O jogador nao se pode mover mais do que 2 slots");
        assertEquals("Ciclo Infinito! O jogador ficou preso na casa.", message);

   moveValid =gm.moveCurrentPlayer(2);
   assertTrue(moveValid, "Jogador deve conseguir andar menos de 3 casas");
    }
}
