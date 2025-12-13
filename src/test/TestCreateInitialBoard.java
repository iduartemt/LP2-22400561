package test;

import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import static org.junit.jupiter.api.Assertions.*;

public class TestCreateInitialBoard {

    // 1. Teste: Sucesso - Criação válida e verificação do estado inicial
    @Test
    public void testCreateBoard_Success_CheckState() {
        GameManager gm = new GameManager();
        String[][] playerInfo = {
                {"10", "Ana", "Java", "Blue"},
                {"5", "Rui", "Python", "Green"}
        };
        // Tamanho 10 é suficiente (2 jogadores * 2 = 4 mínimo)
        boolean result = gm.createInitialBoard(playerInfo, 10);

        assertTrue(result, "O tabuleiro deve ser criado com sucesso");

        // Verificar Efeitos Colaterais (Estado do Jogo)
        assertNotNull(gm.getProgrammerInfo(10), "O Board deve ter sido instanciado");

        // Verifica se o currentPlayerId é o mais baixo (5 < 10)
        assertEquals(5, gm.getCurrentPlayerID(), "O primeiro jogador deve ser o de ID mais baixo");

        // Verifica se o turno começa em 1 (Validar via getGameResults ou lógica de turno)
        // Como o turnCount é privado e não tem getter direto simples no código fornecido,
        // assumimos que a inicialização funcionou se o resto do estado estiver coerente.
    }

    // 2. Teste: Falha - Número de jogadores insuficiente (< 2)
    @Test
    public void testCreateBoard_Fail_TooFewPlayers() {
        GameManager gm = new GameManager();
        String[][] playerInfo = {
                {"1", "Ana", "Java", "Blue"}
        };

        // Chama nrValidPlayers -> false
        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Não deve iniciar com menos de 2 jogadores");
    }

    // 3. Teste: Falha - Número de jogadores excessivo (> 4)
    @Test
    public void testCreateBoard_Fail_TooManyPlayers() {
        GameManager gm = new GameManager();
        String[][] playerInfo = {
                {"1", "P1", "Java", "Blue"},
                {"2", "P2", "Java", "Green"},
                {"3", "P3", "Java", "Purple"},
                {"4", "P4", "Java", "Brown"},
                {"5", "P5", "Java", "RANDOM"} // 5º Jogador
        };

        // Chama nrValidPlayers -> false
        boolean result = gm.createInitialBoard(playerInfo, 20);
        assertFalse(result, "Não deve iniciar com mais de 4 jogadores");
    }

    // 4. Teste: Falha - Tamanho do Mundo Insuficiente
    @Test
    public void testCreateBoard_Fail_WorldSizeTooSmall() {
        GameManager gm = new GameManager();
        String[][] playerInfo = {
                {"1", "Ana", "Java", "Blue"},
                {"2", "Rui", "Python", "Green"}
        };

        // Regra: worldSize < players * 2
        // 2 Jogadores * 2 = 4. Se o mundo for 3, deve falhar.
        boolean result = gm.createInitialBoard(playerInfo, 3);

        assertFalse(result, "O mundo deve ter pelo menos o dobro do tamanho do número de jogadores");
    }

    // 5. Teste: Limite - Tamanho do Mundo Exatamente no Limite
    @Test
    public void testCreateBoard_Boundary_WorldSizeExact() {
        GameManager gm = new GameManager();
        String[][] playerInfo = {
                {"1", "Ana", "Java", "Blue"},
                {"2", "Rui", "Python", "Green"}
        };

        // 2 Jogadores -> Mínimo 4 casas. Testamos com 4.
        boolean result = gm.createInitialBoard(playerInfo, 4);

        assertTrue(result, "Deve aceitar tamanho do mundo exatamente igual ao dobro dos jogadores");
    }

    // 6. Teste: Falha - Dados de Jogador Inválidos (Integração)
    @Test
    public void testCreateBoard_Fail_InvalidPlayerData() {
        GameManager gm = new GameManager();
        // Embora nrValidPlayers e worldSize passem, validateAndCreatePlayersByLine deve falhar
        String[][] playerInfo = {
                {"1", "Ana", "Java", "Blue"},
                {"1", "Clone", "C", "Green"} // ID Repetido
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);

        assertFalse(result, "Deve falhar se validateAndCreatePlayersByLine retornar null (dados inválidos)");
    }

    // 7. Teste: Input Null
    @Test
    public void testCreateBoard_Fail_NullInput() {
        GameManager gm = new GameManager();

        boolean result = gm.createInitialBoard(null, 10);

        assertFalse(result, "Deve lidar com input null graciosamente");
    }


}