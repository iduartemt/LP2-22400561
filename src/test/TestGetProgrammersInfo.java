import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;

import static org.junit.jupiter.api.Assertions.*;

class TestGetProgrammersInfo {

    @Test
    void testGetProgrammersInfo_InitialState() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        
        String expected = "Alice : No tools | Bob : No tools";
        assertEquals(expected, gameManager.getProgrammersInfo());
    }

    @Test
    void testGetProgrammersInfo_WithTools() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"1", "4", "2"}, // IDE at 2
            {"1", "2", "3"}  // UnitTests at 3
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Alice gets IDE
        gameManager.moveCurrentPlayer(1); // to 2
        gameManager.reactToAbyssOrTool();

        // Bob gets UnitTests
        gameManager.moveCurrentPlayer(2); // to 3
        gameManager.reactToAbyssOrTool();
        
        String expected = "Alice : IDE | Bob : Testes Unitários";
        assertEquals(expected, gameManager.getProgrammersInfo());
    }

    @Test
    void testGetProgrammersInfo_OnePlayerDefeated() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "7", "4"} // BlueScreenOfDeath at 4
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Alice hits BlueScreenOfDeath and is defeated
        gameManager.moveCurrentPlayer(3); // to 4
        gameManager.reactToAbyssOrTool();
        
        String expected = "Bob : No tools"; // Only Bob should appear
        assertEquals(expected, gameManager.getProgrammersInfo());
    }

    @Test
    void testGetProgrammersInfo_PlayerOrder() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"3", "Charlie", "C++", "Purple"}, // Corrected color
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        
        // Expected order is by ID: Alice (1), Bob (2), Charlie (3)
        String expected = "Alice : No tools | Bob : No tools | Charlie : No tools";
        assertEquals(expected, gameManager.getProgrammersInfo());
    }

    @Test
    void testGetProgrammersInfo_ToolOrder() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"1", "2", "2"}, // UnitTests at 2
            {"1", "0", "3"}, // Inheritance at 3
            {"1", "4", "4"}  // IDE at 4
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Alice gets UnitTests
        gameManager.moveCurrentPlayer(1); // to 2
        gameManager.reactToAbyssOrTool();
        
        // Bob's turn - move him somewhere without a tool
        gameManager.moveCurrentPlayer(5);
        gameManager.reactToAbyssOrTool();

        // Alice gets Inheritance
        gameManager.moveCurrentPlayer(1); // from 2 to 3
        gameManager.reactToAbyssOrTool();

        // Bob's turn
        gameManager.moveCurrentPlayer(1);
        gameManager.reactToAbyssOrTool();
        
        // Alice gets IDE
        gameManager.moveCurrentPlayer(1); // from 3 to 4
        gameManager.reactToAbyssOrTool();

        // Tools should be sorted alphabetically: Herança;IDE;Testes Unitários
        String expected = "Alice : Herança;IDE;Testes Unitários | Bob : No tools";
        assertEquals(expected, gameManager.getProgrammersInfo());
    }
    
    @Test
    void testGetProgrammersInfo_AllPlayersDefeated() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        String[][] abysses = {
            {"0", "7", "2"}, // BlueScreenOfDeath at 2
            {"0", "7", "3"}  // BlueScreenOfDeath at 3
        };
        gameManager.createInitialBoard(playerInfo, 10, abysses);

        // Alice hits abyss
        gameManager.moveCurrentPlayer(1);
        gameManager.reactToAbyssOrTool();

        // Bob hits abyss
        gameManager.moveCurrentPlayer(2);
        gameManager.reactToAbyssOrTool();

        assertEquals("", gameManager.getProgrammersInfo(), "A string devia ser vazia se todos os jogadores estiverem derrotados.");
    }
}
