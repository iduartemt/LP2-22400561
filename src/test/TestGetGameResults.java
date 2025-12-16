import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TestGetGameResults {

    @Test
    void testGetGameResults_NoWinner() {
        GameManager gameManager = new GameManager();
        String[][] playerInfo = {
            {"1", "Player1", "Java", "Blue"},
            {"2", "Player2", "Python", "Green"}
        };
        gameManager.createInitialBoard(playerInfo, 10);
        ArrayList<String> results = gameManager.getGameResults();
        assertTrue(results.isEmpty(), "A lista de resultados devia estar vazia porque ninguém ganhou.");
    }


}
