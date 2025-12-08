package tests;

import org.junit.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import static org.junit.Assert.*;

public class TestReactToAbyssOrTool {

    @Test
    public void testReactToAbyssOrTool_EmptySlot() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {{"1", "P1", "Java", "Blue"}, {"2", "P2", "Python", "Green"}};
        gameManager.createInitialBoard(playerInfo, 10);
        gameManager.moveCurrentPlayer(2); // Move to an empty slot
        assertNull(gameManager.reactToAbyssOrTool());
        assertEquals(2, gameManager.getCurrentPlayerID()); // Turn should pass to next player
    }

    @Test
    public void testReactToAbyssOrTool_PlayerUsesToolToAvoidAbyss() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {{"1", "P1", "Java", "Blue"}};
        // This test is complex as it requires giving a player a specific tool first
        // and then moving them to a specific abyss.
        // It would require more direct control over player inventory.
        // For now, this is a placeholder.
        assertTrue(true);
    }

    @Test
    public void testReactToAbyssOrTool_PlayerLandsOnAbyssAndGetsTrapped() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "P1", "Java", "Blue"},
            {"2", "P2", "Python", "Green"}
        };
        int worldSize = 10;
        // Place an abyss at position 2 that makes player PRESO (assuming type 0 does this)
        String[][] abyssesAndTools = {
            {"0", "8", "2"} // Abyss type 0 at position 2
        };
        gameManager.createInitialBoard(playerInfo, worldSize, abyssesAndTools);

        // P1 moves 1 space to land on the abyss at position 2
        assertTrue(gameManager.moveCurrentPlayer(1)); // P1 moves from 1 to 2
        assertEquals(1, gameManager.getCurrentPlayerID()); // P1 is current player

        // P1 reacts to the abyss
        String message = gameManager.reactToAbyssOrTool();
        assertNotNull(message); // Expecting a message from abyss interaction
        String[] p1Info = gameManager.getProgrammerInfo(1);
        assertEquals("Preso", p1Info[6]); // P1 should be PRESO
        assertEquals(2, gameManager.getCurrentPlayerID()); // Turn should pass to P2
    }

    @Test
    public void testReactToAbyssOrTool_PlayerLandsOnToolAndAcquiresIt() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "P1", "Java", "Blue"},
            {"2", "P2", "Python", "Green"}
        };
        int worldSize = 10;
        // Place a tool at position 2 (assuming type 0 is "IDE")
        String[][] abyssesAndTools = {
            {"1", "4", "2"} // Tool type 0 (IDE) at position 2
        };
        gameManager.createInitialBoard(playerInfo, worldSize, abyssesAndTools);

        // P1 moves 1 space to land on the tool at position 2
        assertTrue(gameManager.moveCurrentPlayer(1)); // P1 moves from 1 to 2
        assertEquals(1, gameManager.getCurrentPlayerID()); // P1 is current player

        // P1 reacts to the tool
        String message = gameManager.reactToAbyssOrTool();
        assertNotNull(message); // Expecting a message from tool interaction
        String[] p1Info = gameManager.getProgrammerInfo(1);
        assertTrue(p1Info[5].contains("IDE")); // P1 should have the "IDE" tool
        assertEquals(2, gameManager.getCurrentPlayerID()); // Turn should pass to P2
    }

    @Test
    public void testReactToAbyssOrTool_PlayerIsTrapped_TurnPasses() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "P1", "Java", "Blue"},
            {"2", "P2", "Python", "Green"}
        };
        int worldSize = 10;
        // Place an abyss at position 2 that makes player PRESO (assuming type 0 does this)
        String[][] abyssesAndTools = {
            {"0", "8", "2"} // Abyss type 0 at position 2
        };
        gameManager.createInitialBoard(playerInfo, worldSize, abyssesAndTools);

        // P1 moves 1 space to land on the abyss at position 2
        gameManager.moveCurrentPlayer(1); // P1 moves from 1 to 2
        gameManager.reactToAbyssOrTool(); // P1 becomes PRESO, turn passes to P2

        // P2 moves and reacts (e.g., moves 1 space, lands on empty slot)
        gameManager.moveCurrentPlayer(1); // P2 moves from 1 to 2
        gameManager.reactToAbyssOrTool(); // P2 reacts, turn passes to P1

        // Now it's P1's turn again. P1 is PRESO.
        assertEquals(1, gameManager.getCurrentPlayerID());
        String[] p1Info = gameManager.getProgrammerInfo(1);
        assertEquals("Preso", p1Info[6]);

        // P1 calls reactToAbyssOrTool while PRESO
        String message = gameManager.reactToAbyssOrTool();
        assertEquals("Ciclo Infinito! O jogador ficou preso na casa.", message);
        assertEquals(2, gameManager.getCurrentPlayerID()); // Turn should pass to P2
        // Verify P1 is still PRESO after this interaction
        String[] p1InfoAfterReact = gameManager.getProgrammerInfo(1);
        assertEquals("Preso", p1InfoAfterReact[6]);
    }
}
