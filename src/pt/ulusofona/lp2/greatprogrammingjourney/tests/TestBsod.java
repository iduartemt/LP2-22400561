package pt.ulusofona.lp2.greatprogrammingjourney.tests;

import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;
import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype.BlueScreenOfDeath;
import pt.ulusofona.lp2.greatprogrammingjourney.PlayerState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBsod {

    @Test
    public void testBlueScreenOfDeathDefeatsOnlyCurrentPlayer() {
        // 1. Criar jogadores
        List<Player> players = new ArrayList<>();
        Player player1 = new Player(1, "Player One", "Java", "Blue");
        Player player2 = new Player(2, "Player Two", "Python", "Green");
        players.add(player1);
        players.add(player2);

        // 2. Criar o tabuleiro e adicionar os jogadores à primeira casa
        Board board = new Board(players, 10);

        // 3. Criar e adicionar o abismo BSOD
        BlueScreenOfDeath bsod = new BlueScreenOfDeath(5);
        board.encontraSlot(5).addEvent(bsod);

        // 4. Mover o jogador 1 para a casa do BSOD
        board.movePlayer(player1, 4);

        // 5. Simular a interação com o abismo
        bsod.playerInteraction(player1, board);

        // 6. Verificar o estado dos jogadores
        assertEquals(PlayerState.DERROTADO, player1.getState(), "O Jogador 1, que caiu no BSOD, deve estar 'Derrotado'");
        assertEquals(PlayerState.EM_JOGO, player2.getState(), "O Jogador 2 não interagiu com o abismo e deve continuar 'Em jogo'");
    }
}
