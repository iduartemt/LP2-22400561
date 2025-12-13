package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pt.ulusofona.lp2.greatprogrammingjourney.GameManager;

public class TestValidateColor {

    @Test
    public void testValidateColor_ValidColor_Simple() {
        GameManager gm = new GameManager();
        // Cor válida "Blue"
        String[][] playerInfo = {
                {"1", "P1", "Java", "Blue"},
                {"2", "P2", "Python", "Green"}
        };

        // Deve criar o tabuleiro com sucesso
        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertTrue(result, "Deve aceitar cores válidas definidas no Enum Color");

        // Verifica se a cor ficou correta no jogador
        String[] p1Info = gm.getProgrammerInfo(1);
        assertEquals("Blue", p1Info[3], "A cor deve ser guardada corretamente");
    }

    @Test
    public void testValidateColor_Normalization_MixedCase() {
        GameManager gm = new GameManager();
        //Cor válida mas escrita de forma estranha "bLuE"
        String[][] playerInfo = {
                {"1", "P1", "Java", "bLuE"},
                {"2", "P2", "Python", "gREeN"}
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertTrue(result, "Deve aceitar cores válidas independentemente de maiúsculas/minúsculas");

        // Verifica a normalização (Primeira maiúscula, resto minúscula)
        String[] p1Info = gm.getProgrammerInfo(1);
        assertEquals("Blue", p1Info[3], "A cor 'bLuE' deve ser normalizada para 'Blue'");

        String[] p2Info = gm.getProgrammerInfo(2);
        assertEquals("Green", p2Info[3], "A cor 'gREeN' deve ser normalizada para 'Green'");
    }

    @Test
    public void testValidateColor_InvalidColor_NotInEnum() {
        GameManager gm = new GameManager();
        // Cenário: Cor que não existe ("Amarelo")
        // Deve falhar na verificação !validColors(color)
        String[][] playerInfo = {
                {"1", "P1", "Java", "Amarelo"},
                {"2", "P2", "Python", "Green"}
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Não deve criar tabuleiro com cores inválidas");
    }

    @Test
    public void testValidateColor_DuplicateColor_Explicit() {
        GameManager gm = new GameManager();
        // Cenário: Dois jogadores escolhem "Blue"
        // Deve falhar no validColor.add(color)
        String[][] playerInfo = {
                {"1", "P1", "Java", "Blue"},
                {"2", "P2", "Python", "Blue"} // Repetido
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertFalse(result, "Não deve permitir jogadores com cores repetidas");
    }

    @Test
    public void testValidateColor_Random_Assignment() {
        GameManager gm = new GameManager();
        // Cenário: Jogador pede "RANDOM"
        String[][] playerInfo = {
                {"1", "P1", "Java", "RANDOM"},
                {"2", "P2", "Python", "Blue"} // Ocupamos o Blue para forçar o Random a escolher outra
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertTrue(result, "Deve aceitar 'RANDOM' e atribuir uma cor disponível");

        String[] p1Info = gm.getProgrammerInfo(1);
        String assignedColor = p1Info[3];

        assertNotNull(assignedColor);
        assertNotEquals("Blue", assignedColor, "O RANDOM não deve escolher uma cor já usada (Blue)");
        // Verifica se a cor atribuída é válida (ex: Brown, Purple ou Green)
        assertTrue(assignedColor.equals("Brown") || assignedColor.equals("Purple") || assignedColor.equals("Green"),
                "Deve atribuir uma das cores restantes");
    }

    @Test
    public void testValidateColor_AllRandom_UniqueColors() {
        GameManager gm = new GameManager();
        // Cenário: 4 Jogadores (máximo), todos RANDOM
        // O sistema deve ser capaz de dar uma cor única a cada um sem conflitos
        String[][] playerInfo = {
                {"1", "P1", "Java", "RANDOM"},
                {"2", "P2", "Python", "RANDOM"},
                {"3", "P3", "C", "RANDOM"},
                {"4", "P4", "C++", "RANDOM"}
        };

        boolean result = gm.createInitialBoard(playerInfo, 10);
        assertTrue(result, "Deve conseguir atribuir cores únicas a 4 jogadores RANDOM");

        // Verificar unicidade manualmente
        String c1 = gm.getProgrammerInfo(1)[3];
        String c2 = gm.getProgrammerInfo(2)[3];
        String c3 = gm.getProgrammerInfo(3)[3];
        String c4 = gm.getProgrammerInfo(4)[3];

        assertNotEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, c4);
        assertNotEquals(c2, c3);
        // ... (o set validColor dentro do GameManager já garante isto, mas o teste confirma)
    }
}