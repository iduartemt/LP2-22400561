import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;

import static org.junit.jupiter.api.Assertions.*;

class TestMoveCurrentPlayer {

    @Test
    void testMoveCurrentPlayerValid() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
                {"1", "Player1", "Java", "Blue"},
                {"2", "Player2", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        assertTrue(gameManager.moveCurrentPlayer(3));
    }

    @Test
    void testMoveCurrentPlayerInvalidSpaces() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
                {"1", "Player1", "Java", "Blue"},
                {"2", "Player2", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        assertFalse(gameManager.moveCurrentPlayer(0));
        assertFalse(gameManager.moveCurrentPlayer(7));
    }


    @Test
    void testMoveCurrentPlayerWhenStuck() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
                {"1", "Player1", "Java", "Blue"}
        };
        String[][] abyssesAndTools = {
                {"0", "8", "4"} // Abyss, InfiniteLoop, position 4
        };
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Move player to the abyss
        gameManager.moveCurrentPlayer(3);
        gameManager.reactToAbyssOrTool();

        // Now the player should be stuck
        // The turn has passed to the next player, but since there's only one, it's the same player
        assertFalse(gameManager.moveCurrentPlayer(1));
    }

    @Test
    void testMoveCurrentPlayerAssemblyRestriction() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
                {"1", "Player1", "Assembly", "Blue"},
                {"2", "Player2", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        assertFalse(gameManager.moveCurrentPlayer(3));
        assertTrue(gameManager.moveCurrentPlayer(2));
    }

    @Test
    void testMoveCurrentPlayerCRestriction() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
                {"1", "Player1", "C", "Blue"},
                {"2", "Player2", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        assertFalse(gameManager.moveCurrentPlayer(4));
        assertTrue(gameManager.moveCurrentPlayer(3));
    }
}
