package pt.ulusofona.lp2.greatprogrammingjourney.tests;

import org.junit.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import static org.junit.Assert.*;

public class TestCreateInitialBoard {

    @Test
    public void testCreateInitialBoard_ValidData() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        assertTrue(gameManager.createInitialBoard(playerInfo, 10));
    }

    @Test
    public void testCreateInitialBoard_InvalidPlayerCount_LessThanTwo() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"}
        };
        assertFalse(gameManager.createInitialBoard(playerInfo, 10));
    }

    @Test
    public void testCreateInitialBoard_InvalidPlayerCount_MoreThanFour() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"},
            {"3", "Player3", "C", "Brown"},
            {"4", "Player4", "JavaScript", "Yellow"},
            {"5", "Player5", "Ruby", "Purple"}
        };
        assertFalse(gameManager.createInitialBoard(playerInfo, 10));
    }

    @Test
    public void testCreateInitialBoard_InvalidBoardSize() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        assertFalse(gameManager.createInitialBoard(playerInfo, 3));
    }

    @Test
    public void testCreateInitialBoard_DuplicateColors() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Blue"}
        };
        assertFalse(gameManager.createInitialBoard(playerInfo, 10));
    }

    @Test
    public void testCreateInitialBoard_WithAbyssesAndTools_ValidData() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "1", "5"}, // Abyss
            {"1", "2", "8"}  // Tool
        };
        assertTrue(gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools));
    }

    @Test
    public void testCreateInitialBoard_WithAbyssesAndTools_InvalidData() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"2", "1", "5"} // Invalid type
        };
        assertFalse(gameManager.createInitialBoard(playerInfo, 10, abyssesAndTools));
    }
}
