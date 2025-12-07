package pt.ulusofona.lp2.greatprogrammingjourney.tests;

import org.junit.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import static org.junit.Assert.*;

public class TestBSOD {

    @Test
    public void testBSODOnlyDefeatsOnePlayer() {
        GameManager gameManager = new GameManager();

        // 1. Criar 3 jogadores
        String[][] players = new String[][]{
                {"1", "PlayerA", "Java", "Purple"},
                {"2", "PlayerB", "Python", "Green"},
                {"3", "PlayerC", "C", "Blue"}
        };

        // 2. Definir um abismo BSOD na casa 5
        String[][] abyssesAndTools = new String[][]{
                {"0", "7", "5"} // Tipo 0 (Abismo), Subtipo 7 (BSOD), Posição 5
        };

        // 3. Criar o tabuleiro
        boolean boardCreated = gameManager.createInitialBoard(players, 20, abyssesAndTools);
        assertTrue("A criação do tabuleiro deveria ter sucesso", boardCreated);

        // 4. Mover o Jogador 1 (PlayerA) para a casa 5
        // O jogador começa na casa 1, por isso precisa de mover 4 espaços
        boolean moved = gameManager.moveCurrentPlayer(4);
        assertTrue("O movimento do jogador deveria ser bem-sucedido", moved);

        // Verificar se o jogador está na posição correta
        String[] playerAInfo = gameManager.getProgrammerInfo(1);
        assertEquals("O jogador A deveria estar na casa 5", "5", playerAInfo[4]);

        // 5. Ativar o evento do abismo
        String reaction = gameManager.reactToAbyssOrTool();
        assertNotNull("Deveria haver uma reação ao abismo", reaction);

        // 6. Verificar o estado dos jogadores
        String[] playerAInfoAfter = gameManager.getProgrammerInfo(1);
        String[] playerBInfoAfter = gameManager.getProgrammerInfo(2);
        String[] playerCInfoAfter = gameManager.getProgrammerInfo(3);

        // Apenas o Jogador A deve estar derrotado
        assertEquals("O Jogador A deveria estar 'Derrotado'", "Derrotado", playerAInfoAfter[6]);
        assertEquals("O Jogador B deveria estar 'Em jogo'", "Em jogo", playerBInfoAfter[6]);
        assertEquals("O Jogador C deveria estar 'Em jogo'", "Em jogo", playerCInfoAfter[6]);

        // Contar jogadores vivos
        int alivePlayers = 0;
        if (playerAInfoAfter[6].equals("Em jogo")) alivePlayers++;
        if (playerBInfoAfter[6].equals("Em jogo")) alivePlayers++;
        if (playerCInfoAfter[6].equals("Em jogo")) alivePlayers++;

        assertEquals("Deveria haver 2 jogadores vivos", 2, alivePlayers);
    }
}
