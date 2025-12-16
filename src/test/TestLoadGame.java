import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import pt.ulusofona.lp2.greatprogrammingjourney.InvalidFileException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class TestLoadGame {

    @Test
    void testLoadGame_Successful(@TempDir Path tempDir) throws IOException, InvalidFileException {
        // 1. Setup and Save Game State
        GameManager originalGame = new GameManager();
        String[][] playerInfo = {
            {"1", "Alice", "Java", "Blue"},
            {"2", "Bob", "Python", "Green"}
        };
        String[][] abyssesAndTools = {
            {"0", "0", "4"}, // SyntaxError at 4
            {"1", "4", "2"}  // IDE at 2
        };
        originalGame.createInitialBoard(playerInfo, 10, abyssesAndTools);

        // Make some moves
        originalGame.moveCurrentPlayer(1); // Alice to 2 (gets IDE)
        originalGame.reactToAbyssOrTool(); // Turn -> Bob
        originalGame.moveCurrentPlayer(3); // Bob to 4
        originalGame.reactToAbyssOrTool(); // Turn -> Alice

        File saveFile = tempDir.resolve("savegame.txt").toFile();
        assertTrue(originalGame.saveGame(saveFile), "O jogo deveria ter sido guardado com sucesso.");

        // 2. Load Game State
        GameManager loadedGame = new GameManager();
        loadedGame.loadGame(saveFile);

        // 3. Assertions
        assertEquals(originalGame.getCurrentPlayerID(), loadedGame.getCurrentPlayerID(), "O ID do jogador atual não corresponde.");
        
        // Check player states
        String[] originalAlice = originalGame.getProgrammerInfo(1);
        String[] loadedAlice = loadedGame.getProgrammerInfo(1);
        assertArrayEquals(originalAlice, loadedAlice, "A informação da Alice não corresponde após o load.");

        String[] originalBob = originalGame.getProgrammerInfo(2);
        String[] loadedBob = loadedGame.getProgrammerInfo(2);
        assertArrayEquals(originalBob, loadedBob, "A informação do Bob não corresponde após o load.");
        
        // Check board events
        assertNotNull(loadedGame.getSlotInfo(4)[1], "Deveria haver um evento na casa 4.");
        assertEquals("Erro de sintaxe", loadedGame.getSlotInfo(4)[1]);
        assertNotNull(loadedGame.getSlotInfo(2)[1], "Deveria haver um evento na casa 2.");
        assertEquals("IDE", loadedGame.getSlotInfo(2)[1]);
    }

    @Test
    void testLoadGame_InvalidFile(@TempDir Path tempDir) throws IOException {
        File invalidFile = tempDir.resolve("invalid.txt").toFile();
        try (FileWriter writer = new FileWriter(invalidFile)) {
            writer.write("isto não é um ficheiro de jogo válido");
        }

        GameManager gameManager = new GameManager();
        assertThrows(InvalidFileException.class, () -> {
            gameManager.loadGame(invalidFile);
        }, "Deveria ser lançada uma InvalidFileException para um ficheiro mal formatado.");
    }

    @Test
    void testLoadGame_FileNotFound() {
        GameManager gameManager = new GameManager();
        File nonExistentFile = new File("non_existent_file.txt");

        assertThrows(FileNotFoundException.class, () -> {
            gameManager.loadGame(nonExistentFile);
        }, "Deveria ser lançada uma FileNotFoundException para um ficheiro que não existe.");
    }
}
