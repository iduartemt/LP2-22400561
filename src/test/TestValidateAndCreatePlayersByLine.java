package test;

import org.junit.jupiter.api.Test;
import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;
import static org.junit.jupiter.api.Assertions.*;

public class TestValidateAndCreatePlayersByLine {

    // 1. Teste: Linha com número de campos correto (4 campos)
    @Test
    public void testValidatePlayers_ValidFullInfo() {
        GameManager gm = new GameManager();
        String[][] playerInfo = {
                {"1", "Ana", "Java", "Blue"},
                {"2", "Rui", "Python", "Green"}
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertTrue(result, "Deve aceitar linhas com 4 campos válidos");
    }

    // 2. Teste: Linha com 3 campos (Falta a cor -> Deve assumir RANDOM)
    @Test
    public void testValidatePlayers_ValidMissingColor() {
        GameManager gm = new GameManager();
        // O código diz: if (playerLine.length == 4) ... else colorStr = "RANDOM"
        String[][] playerInfo = {
                {"1", "Ana", "Java"} // Apenas 3 campos
        };
        // Nota: createInitialBoard pede min 2 jogadores, adicionamos um dummy válido
        String[][] fullInfo = {
                {"1", "Ana", "Java"},
                {"2", "Rui", "Python", "Green"}
        };

        boolean result = gm.createInitialBoard(fullInfo, 10);
        assertTrue(result, "Deve aceitar linhas com 3 campos e atribuir cor aleatória");

        // Verifica se atribuiu uma cor válida
        String[] info = gm.getProgrammerInfo(1);
        assertNotNull(info[3]); // A cor não pode ser null
        assertFalse(info[3].equals("RANDOM")); // Deve ter sido substituído por uma cor real (ex: Blue)
    }

    // 3. Teste: Linha com campos a menos (< 2)
    @Test
    public void testValidatePlayers_InvalidLength_TooShort() {
        GameManager gm = new GameManager();
        String[][] playerInfo = {
                {"1"}, // Só ID, inválido
                {"2", "Rui", "Python", "Green"}
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Deve falhar se a linha tiver menos de 2 campos");
    }

    // 4. Teste: Linha com campos a mais (> 4)
    @Test
    public void testValidatePlayers_InvalidLength_TooLong() {
        GameManager gm = new GameManager();
        String[][] playerInfo = {
                {"1", "Ana", "Java", "Blue", "ExtraField"}, // 5 campos
                {"2", "Rui", "Python", "Green"}
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Deve falhar se a linha tiver mais de 4 campos");
    }

    // 5. Teste: ID Inválido (Não numérico)
    @Test
    public void testValidatePlayers_InvalidId_NotNumber() {
        GameManager gm = new GameManager();
        // Player.isValidId retorna null se não for int
        String[][] playerInfo = {
                {"Um", "Ana", "Java", "Blue"},
                {"2", "Rui", "Python", "Green"}
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Deve falhar se o ID não for um número");
    }

    // 6. Teste: ID Duplicado
    @Test
    public void testValidatePlayers_DuplicateId() {
        GameManager gm = new GameManager();
        // O código verifica: if (usedIds.contains(idStr)) return null
        String[][] playerInfo = {
                {"1", "Ana", "Java", "Blue"},
                {"1", "Rui", "Python", "Green"} // ID 1 repetido
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Deve falhar se houver IDs duplicados");
    }

    // 7. Teste: Nome Duplicado
    @Test
    public void testValidatePlayers_DuplicateName() {
        GameManager gm = new GameManager();
        // O código verifica: if (usedNames.contains(nameStr)) return null
        String[][] playerInfo = {
                {"1", "Ana", "Java", "Blue"},
                {"2", "Ana", "Python", "Green"} // Nome "Ana" repetido
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Deve falhar se houver Nomes duplicados");
    }

    // 8. Teste: Linguagem Inválida (Vazia ou Null)
    @Test
    public void testValidatePlayers_InvalidLanguage() {
        GameManager gm = new GameManager();
        // Player.isValidLanguage retorna null se for vazio
        String[][] playerInfo = {
                {"1", "Ana", "", "Blue"}, // Linguagem vazia
                {"2", "Rui", "Python", "Green"}
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Deve falhar se a linguagem for vazia");
    }

    // 9. Teste: Linha a Null (Defesa extra)
    @Test
    public void testValidatePlayers_NullLine() {
        GameManager gm = new GameManager();
        // Simular uma linha null no array
        String[][] playerInfo = new String[2][];
        playerInfo[0] = new String[]{"1", "Ana", "Java", "Blue"};
        playerInfo[1] = null; // Linha inválida

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Deve falhar se uma das linhas do array for null");
    }
}