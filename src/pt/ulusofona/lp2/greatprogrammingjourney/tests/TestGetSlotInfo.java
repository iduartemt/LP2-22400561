package pt.ulusofona.lp2.greatprogrammingjourney.tests;

import org.junit.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import static org.junit.Assert.*;

public class TestGetSlotInfo {

    @Test
    public void testGetSlotInfo_InvalidPosition() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "P1", "C", "Brown"},
            {"2", "P2", "Java", "Blue"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        assertNull(gameManager.getSlotInfo(0)); // Position 0 is invalid
        assertNull(gameManager.getSlotInfo(11)); // Position 11 is out of bounds
    }

    @Test
    public void testGetSlotInfo_EmptySlot() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "P1", "C", "Brown"},
            {"2", "P2", "Java", "Blue"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        String[] info = gameManager.getSlotInfo(5);
        assertNotNull(info);
        assertEquals("", info[0]); // No players
        assertEquals("", info[1]); // No event
        assertEquals("", info[2]); // No event type
    }

    @Test
    public void testGetSlotInfo_WithPlayer() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"7", "P1", "C", "Brown"},
            {"8", "P2", "Java", "Blue"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        String[] info = gameManager.getSlotInfo(1); // Players start at position 1
        assertNotNull(info);
        assertTrue(info[0].contains("7"));
        assertTrue(info[0].contains("8"));
        assertEquals("", info[1]);
        assertEquals("", info[2]);
    }

    @Test
    public void testGetSlotInfo_WithAbyss() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "P1", "C", "Brown"},
            {"2", "P2", "Java", "Blue"}
        };
        String[][] abyssesAndTools = {{"0", "2", "4"}}; // Abyss type 2 at pos 4
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);
        String[] info = gameManager.getSlotInfo(4);
        assertNotNull(info);
        assertEquals("", info[0]);
        // The description depends on the abyss implementation, so we check if it's not empty
        assertFalse(info[1].isEmpty()); 
        assertEquals("A:2", info[2]);
    }

    @Test
    public void testGetSlotInfo_WithTool() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "P1", "C", "Brown"},
            {"2", "P2", "Java", "Blue"}
        };
        String[][] abyssesAndTools = {{"1", "3", "7"}}; // Tool type 3 at pos 7
        gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools);
        String[] info = gameManager.getSlotInfo(7);
        assertNotNull(info);
        assertEquals("", info[0]);
        // The description depends on the tool implementation, so we check if it's not empty
        assertFalse(info[1].isEmpty());
        assertEquals("T:3", info[2]);
    }
}
