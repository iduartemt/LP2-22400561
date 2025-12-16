import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;

import static org.junit.jupiter.api.Assertions.*;

class TestReactToAbyssOrTool {

    // Helper method to skip a player's turn (move 1 space and react)
    private void skipTurn(GameManager gameManager) {
        gameManager.moveCurrentPlayer(1);
        gameManager.reactToAbyssOrTool();
    }

    @Test
    void testReactToSyntaxErrorAbyss() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "0", "4"} // Abyss, SyntaxError, position 4
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);
        
        // Player 1 turn
        gameManager.moveCurrentPlayer(3); // Moves to 4
        String message = gameManager.reactToAbyssOrTool();
        
        assertEquals("Recua 1 casa", message);
        String[] playerInfoAfter = gameManager.getProgrammerInfo(1);
        assertEquals("3", playerInfoAfter[4]);
    }

    @Test
    void testReactToSyntaxErrorAbyssWithIDE() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "0", "4"}, // Abyss, SyntaxError, position 4
            {"1", "4", "2"}  // Tool, IDE, position 2
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);
        
        // Player 1 turn: Get IDE
        gameManager.moveCurrentPlayer(1); // Moves to 2
        gameManager.reactToAbyssOrTool();

        // Player 2 turn: Skip
        skipTurn(gameManager);

        // Player 1 turn: Move to abyss
        gameManager.moveCurrentPlayer(2); // Moves from 2 to 4
        String message = gameManager.reactToAbyssOrTool();

        assertEquals("Erro de sintaxe anulado por IDE", message);
        String[] playerInfoAfter = gameManager.getProgrammerInfo(1);
        assertEquals("4", playerInfoAfter[4]);
    }

    @Test
    void testReactToLogicErrorAbyss() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "1", "6"} // Abyss, LogicError, position 6
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);
        
        // Player 1 turn
        gameManager.moveCurrentPlayer(5); // Moves to 6
        String message = gameManager.reactToAbyssOrTool();
        
        assertEquals("Erro de Lógica", message);
        String[] playerInfoAfter = gameManager.getProgrammerInfo(1);
        // 6 - (5/2) = 6 - 2 = 4
        assertEquals("4", playerInfoAfter[4]);
    }

    @Test
    void testReactToLogicErrorAbyssWithUnitTests() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "1", "6"}, // Abyss, LogicError, position 6
            {"1", "2", "2"}  // Tool, UnitTests, position 2
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Player 1 turn: Get Unit Tests
        gameManager.moveCurrentPlayer(1); // Moves to 2
        gameManager.reactToAbyssOrTool();

        // Player 2 turn: Skip
        skipTurn(gameManager);

        // Player 1 turn: Move to abyss
        gameManager.moveCurrentPlayer(4); // Moves from 2 to 6
        String message = gameManager.reactToAbyssOrTool();

        assertEquals("Erro de Lógica anulado por Testes Unitários", message);
        String[] playerInfoAfter = gameManager.getProgrammerInfo(1);
        assertEquals("6", playerInfoAfter[4]);
    }

    @Test
    void testReactToInfiniteLoopAbyss() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "8", "4"} // Abyss, InfiniteLoop, position 4
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);
        
        // Player 1 turn
        gameManager.moveCurrentPlayer(3); // Moves to 4
        String message = gameManager.reactToAbyssOrTool();
        
        assertEquals("Ciclo Infinito", message);
        String[] playerInfoAfter = gameManager.getProgrammerInfo(1);
        assertEquals("Preso", playerInfoAfter[6]);
    }

    @Test
    void testReactToNoEvent() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        
        // Player 1 turn
        gameManager.moveCurrentPlayer(3);
        String message = gameManager.reactToAbyssOrTool();
        
        assertNull(message);
        String[] playerInfoAfter = gameManager.getProgrammerInfo(1);
        assertEquals("4", playerInfoAfter[4]);
    }

    @Test
    void testReactWhenStuck() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "8", "4"} // Abyss, InfiniteLoop, position 4
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Player 1 turn: Get stuck
        gameManager.moveCurrentPlayer(3);
        gameManager.reactToAbyssOrTool(); // Returns "Ciclo Infinito", P1 becomes PRESO, Turn -> P2

        // Player 2 turn: Skip
        skipTurn(gameManager); // Turn -> P1

        // Player 1 turn: Try to react again
        String message = gameManager.reactToAbyssOrTool();
        assertEquals("Ciclo Infinito! O jogador ficou preso na casa.", message);
    }

    @Test
    void testReactToTool() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"1", "4", "3"} // Tool, IDE, position 3
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);
        
        // Player 1 turn
        gameManager.moveCurrentPlayer(2); // Moves to 3
        String message = gameManager.reactToAbyssOrTool();
        
        assertEquals("jogador agarrou IDE", message);
        String[] playerInfoAfter = gameManager.getProgrammerInfo(1);
        assertEquals("IDE", playerInfoAfter[5]);
    }
}
