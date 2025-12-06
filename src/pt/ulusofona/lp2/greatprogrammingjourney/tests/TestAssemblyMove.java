package pt.ulusofona.lp2.greatprogrammingjourney.tests;

import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAssemblyMove {

    @Test
    void testMoveCurrentPlayerValidLanguage() {


        GameManager gm = new GameManager();
        String[][] playersInfo = {
                {"2", "Adversario", "Assembly;C++", "Green"},
                {"4", "User", "Java", "Blue"}
        };
        gm.createInitialBoard(playersInfo, 30, null);

        assertFalse(gm.moveCurrentPlayer(5));
    }
}
