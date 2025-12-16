import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;

import static org.junit.jupiter.api.Assertions.*;

class TestGetProgrammerInfoAsStr {

    @Test
    void testGetProgrammerInfoAsStr_InitialState() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        
        String expected = "1 | Alice | 1 | No tools | Java | Em Jogo";
        assertEquals(expected, gameManager.getProgrammerInfoAsStr(1));
    }

    @Test
    void testGetProgrammerInfoAsStr_WithTools() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"1", "4", "2"}, // IDE at 2
            {"1", "0", "3"}  // Inheritance at 3
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Alice gets IDE
        gameManager.moveCurrentPlayer(1); // to 2
        gameManager.reactToAbyssOrTool();
        
        // Bob's turn
        gameManager.moveCurrentPlayer(1);
        gameManager.reactToAbyssOrTool();

        // Alice gets Inheritance
        gameManager.moveCurrentPlayer(1); // from 2 to 3
        gameManager.reactToAbyssOrTool();

        // Tools are sorted: Herança;IDE
        String expected = "1 | Alice | 3 | Herança;IDE | Java | Em Jogo";
        assertEquals(expected, gameManager.getProgrammerInfoAsStr(1));
    }

    @Test
    void testGetProgrammerInfoAsStr_MultipleLanguages() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Python; Java; C++", "Blue"},
            {"2", "Bob", "C", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        
        // Languages are sorted: C++; Java; Python
        String expected = "1 | Alice | 1 | No tools | C++; Java; Python | Em Jogo";
        assertEquals(expected, gameManager.getProgrammerInfoAsStr(1));
    }

    @Test
    void testGetProgrammerInfoAsStr_StuckState() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "8", "4"} // InfiniteLoop at 4
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Alice gets stuck
        gameManager.moveCurrentPlayer(3); // to 4
        gameManager.reactToAbyssOrTool();
        
        String expected = "1 | Alice | 4 | No tools | Java | Preso";
        assertEquals(expected, gameManager.getProgrammerInfoAsStr(1));
    }

    @Test
    void testGetProgrammerInfoAsStr_InvalidId() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        
        assertNull(gameManager.getProgrammerInfoAsStr(99), "Deveria retornar null para um ID de jogador inválido.");
    }
    
    @Test
    void testGetProgrammerInfoAsStr_DefeatedState() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "7", "4"} // BlueScreenOfDeath at 4
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Alice is defeated
        gameManager.moveCurrentPlayer(3); // to 4
        gameManager.reactToAbyssOrTool();
        
        String expected = "1 | Alice | 4 | No tools | Java | Derrotado";
        assertEquals(expected, gameManager.getProgrammerInfoAsStr(1));
    }
}
