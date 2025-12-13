package test;

import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import static org.junit.jupiter.api.Assertions.*;

public class TestCreateInitialBoardEvents {

    // Dados de jogadores válidos para usar em todos os testes
    private final String[][] validPlayers = {
            {"1", "Ana", "Java", "Blue"},
            {"2", "Rui", "Python", "Green"}
    };

    // 1. Teste: Sucesso - Abismos e Ferramentas Válidos (Happy Path)
    @Test
    public void testCreateBoardEvents_Success() {
        GameManager gm = new GameManager();
        // Evento 1: Abismo (Tipo 0), Subtipo 1 (Erro Lógica), Posição 5
        // Evento 2: Ferramenta (Tipo 1), Subtipo 2 (Testes Unitários), Posição 8
        String[][] events = {
                {"0", "1", "5"},
                {"1", "2", "8"}
        };

        boolean result = gm.createInitialBoard(validPlayers, 10, events);

        assertTrue(result, "Deve criar tabuleiro com eventos válidos");

        // Verificar se os eventos foram colocados (Black-box testing via getSlotInfo)
        // getSlotInfo retorna [players, eventName, eventTypeStr]
        String[] slot5 = gm.getSlotInfo(5);
        assertEquals("A:1", slot5[2], "Slot 5 deve ter Abismo tipo 1");

        String[] slot8 = gm.getSlotInfo(8);
        assertEquals("T:2", slot8[2], "Slot 8 deve ter Ferramenta tipo 2");
    }

    // 2. Teste: Sucesso - Array de Eventos a Null (Deve chamar o overload simples)
    @Test
    public void testCreateBoardEvents_NullArray() {
        GameManager gm = new GameManager();
        // Passar null em vez de array de eventos
        boolean result = gm.createInitialBoard(validPlayers, 10, null);

        assertTrue(result, "Deve criar tabuleiro apenas com jogadores se eventos for null");
        assertNotNull(gm.getProgrammerInfo(1), "Jogo deve ter iniciado");
    }

    // 3. Teste: Falha - Linha com formato inválido (tamanho != 3 ou null)
    @Test
    public void testCreateBoardEvents_InvalidLineFormat() {
        GameManager gm = new GameManager();
        String[][] eventsTooShort = { {"0", "1"} }; // Só 2 campos

        assertFalse(gm.createInitialBoard(validPlayers, 10, eventsTooShort),
                "Deve falhar se a linha não tiver 3 campos");

        String[][] eventsNullLine = { null };
        assertFalse(gm.createInitialBoard(validPlayers, 10, eventsNullLine),
                "Deve falhar se a linha for null");
    }

    // 4. Teste: Falha - Tipo Inválido (Nem "0" nem "1")
    @Test
    public void testCreateBoardEvents_InvalidType() {
        GameManager gm = new GameManager();
        String[][] events = { {"2", "1", "5"} }; // Tipo "2" não existe

        assertFalse(gm.createInitialBoard(validPlayers, 10, events),
                "Deve falhar se o tipo não for 0 ou 1");
    }

    // 5. Teste: Falha - Parsing de Números (NumberFormatException)
    @Test
    public void testCreateBoardEvents_ParsingError() {
        GameManager gm = new GameManager();
        String[][] eventsSub = { {"0", "Um", "5"} }; // Subtipo não numérico
        assertFalse(gm.createInitialBoard(validPlayers, 10, eventsSub));

        String[][] eventsPos = { {"0", "1", "Cinco"} }; // Posição não numérica
        assertFalse(gm.createInitialBoard(validPlayers, 10, eventsPos));
    }

    // 6. Teste: Falha - Limites de Subtipo Abismo (0 a 9)
    @Test
    public void testCreateBoardEvents_AbyssBounds() {
        GameManager gm = new GameManager();

        // Teste Limite Inferior (< 0)
        String[][] eventsUnder = { {"0", "-1", "5"} };
        assertFalse(gm.createInitialBoard(validPlayers, 10, eventsUnder));

        // Teste Limite Superior (> 9)
        String[][] eventsOver = { {"0", "10", "5"} };
        assertFalse(gm.createInitialBoard(validPlayers, 10, eventsOver));
    }

    // 7. Teste: Falha - Limites de Subtipo Ferramenta (0 a 5)
    @Test
    public void testCreateBoardEvents_ToolBounds() {
        GameManager gm = new GameManager();

        // Teste Limite Inferior (< 0)
        String[][] eventsUnder = { {"1", "-1", "5"} };
        assertFalse(gm.createInitialBoard(validPlayers, 10, eventsUnder));

        // Teste Limite Superior (> 5)
        String[][] eventsOver = { {"1", "6", "5"} };
        assertFalse(gm.createInitialBoard(validPlayers, 10, eventsOver));
    }

    // 8. Teste: Falha - Posição Inválida (Fora do Tabuleiro)
    @Test
    public void testCreateBoardEvents_PositionBounds() {
        GameManager gm = new GameManager();
        int worldSize = 10;

        // Posição < 1
        String[][] eventsUnder = { {"0", "1", "0"} };
        assertFalse(gm.createInitialBoard(validPlayers, worldSize, eventsUnder));

        // Posição > worldSize
        String[][] eventsOver = { {"0", "1", "11"} };
        assertFalse(gm.createInitialBoard(validPlayers, worldSize, eventsOver));
    }

    // 9. Teste: Falha - Falha na criação base (Jogadores Inválidos)
    @Test
    public void testCreateBoardEvents_BaseCreationFails() {
        GameManager gm = new GameManager();
        String[][] invalidPlayers = { {"1", "Ana", "Java", "Blue"} }; // Só 1 jogador
        String[][] validEvents = { {"0", "1", "5"} };

        // Mesmo com eventos válidos, se os jogadores falharem, tudo falha
        assertFalse(gm.createInitialBoard(invalidPlayers, 10, validEvents),
                "Deve falhar se a criação base do tabuleiro falhar");
    }

    // 10. Teste: Falha - Board rejeita evento (IllegalArgumentException)
    // Nota: Este cenário é difícil de atingir porque o GameManager faz as mesmas validações que o Board.
    // No entanto, se o Board tiver uma regra que o GM não tem (ex: tipo desconhecido que passou de alguma forma),
    // o bloco try-catch deve apanhar.
    // Vamos simular um caso limite se possível, ou confiar na cobertura lógica anterior.
    // Como o código do GM replica a lógica do Board, o 'catch' é defensivo.
}