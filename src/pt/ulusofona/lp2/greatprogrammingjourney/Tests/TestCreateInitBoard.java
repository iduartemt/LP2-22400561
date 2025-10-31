package pt.ulusofona.lp2.greatprogrammingjourney.Tests;

import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;

import static org.junit.jupiter.api.Assertions.*;


public class TestCreateInitBoard {

    private String[][] validPlayers() {
        return new String[][]{
                {"1", "Duarte", "C", "Purple"},
                {"2", "Joao", "Python", "Green"},
                {"3", "Iris", "Java", "Brown"}

        };
    }

    @Test
    void testCreateInitBoard_ifBoardIsDoubleThanPlayersNumber_ReturnFalse() {
        GameManager gm = new GameManager();
        assertFalse(gm.createInitialBoard(validPlayers(), 4),
                "Retorna False se o numero da board nao for o dobro do numero de jogadores");

    }
}

