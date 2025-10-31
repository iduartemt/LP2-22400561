package pt.ulusofona.lp2.greatprogrammingjourney.Tests;

import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreateInitBoard {

    private String[][] validPlayers() {
        return new String[][]{
                {"1", "Goiaba", "Java", "Green"},
                {"2", "Bruninho", "Python", "Blue"}
        };
    }

    @Test
    void testCreateInitBoard_invalidWorldSize_ReturnFalse() {
        GameManager gm = new GameManager();
        // Para 2 jogadores, mínimo é 4. 3 é inválido.
        assertFalse(gm.createInitialBoard(validPlayers(), 3),
                "Retorna false se o tamanho da board for menor que o dobro dos jogadores");
    }

    @Test
    void testCreateInitBoard_validWorldSize_ReturnTrue() {
        GameManager gm = new GameManager();
        // Exatamente o dobro (válido)
        assertTrue(gm.createInitialBoard(validPlayers(), 4),
                "Retorna true quando o tamanho da board é pelo menos o dobro dos jogadores");
    }

    @Test
    void winningMove_isCounted_andDoesNotAdvanceTurn() {
        GameManager gm = new GameManager();
        // Para vencer numa única jogada válida (<=6), usa board de 7 e move 6 (1 -> 7)
        assertTrue(gm.createInitialBoard(validPlayers(), 7), "Falha ao criar board");

        int first = gm.getCurrentPlayerID();
        assertEquals(1, first, "O primeiro jogador (menor id) devia ser o 1");

        // Jogada vencedora: 1 -> 7
        assertTrue(gm.moveCurrentPlayer(6), "Jogada vencedora devia ser válida");
        assertTrue(gm.gameIsOver(), "Jogo devia ter terminado após a jogada vencedora");

        // NÃO passa a vez após a jogada vencedora
        assertEquals(1, gm.getCurrentPlayerID(),
                "Não deve rodar turno após a jogada vencedora (mantém no vencedor)");
    }

    @Test
    void noMovesAfterGameOver_andTurnCountDoesNotIncrease() {
        GameManager gm = new GameManager();
        // Cenário de vitória em 1 jogada válida (<=6)
        assertTrue(gm.createInitialBoard(validPlayers(), 7), "Falha ao criar board");

        // Vence em 1 jogada (1 -> 7)
        assertTrue(gm.moveCurrentPlayer(6));
        assertTrue(gm.gameIsOver());

        // Tentar jogar depois do fim tem de falhar e não contar
        assertFalse(gm.moveCurrentPlayer(1), "Não deve permitir jogadas após o fim do jogo");

        // Verifica NR. DE TURNOS apresentado (mantendo o +1 na impressão)
        ArrayList<String> results = gm.getGameResults();
        assertFalse(results.isEmpty(), "Resultados não deviam estar vazios após fim do jogo");

        // Formato: [ "THE GREAT PROGRAMMING JOURNEY", "", "NR. DE TURNOS", <numero>, ... ]
        String shownTurns = results.get(3);

        // 1 jogada real → apresentação mantém +1 → "2"
        assertEquals("2", shownTurns,
                "getGameResults deve apresentar turnCount + 1 (mantém +1 para compatibilidade)");
    }

    @Test
    void keepPlusOnePresentation_butRealCounterStopsAtGameOver() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(validPlayers(), 7));

        // 1ª jogada: vitória
        assertTrue(gm.moveCurrentPlayer(6));
        assertTrue(gm.gameIsOver());

        // 2ª e 3ª tentativas: devem falhar e não contar
        assertFalse(gm.moveCurrentPlayer(2));
        assertFalse(gm.moveCurrentPlayer(3));

        // Mostragem continua com +1
        ArrayList<String> results = gm.getGameResults();
        String shownTurns = results.get(3);
        assertEquals("2", shownTurns, "Apresentação deve manter +1 após o fim do jogo");
    }
}
