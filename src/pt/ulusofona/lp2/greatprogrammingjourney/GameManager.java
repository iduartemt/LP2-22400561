package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.util.*;

enum Cores {
    BLUE,
    BROWN,
    GREEN,
    PURPLE
}

public class GameManager {
    Board board;

    //Se tem esta entre 2 e 4 players
    public boolean nrValidPlayers(String[][] playerInfo) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }
        return true;
    }

    //Se cada jogador tem a informacao valida(id,nome,cor)
    public List<Player> infoValidPlayers(String[][] playerInfo) {
        List<Player> validPlayers = new ArrayList<>();
        HashSet<String> validId = new HashSet<>();
        HashSet<String> validName = new HashSet<>();
        HashSet<String> validColor = new HashSet<>();

        for (int i = 0; i < playerInfo.length; i++) {

            //Linha
            String[] validLine = playerInfo[i];
            if (validLine == null || validLine.length == 0) {
                return null;
            }

            //Colunas
            //validar id
            int id;
            String name;
            String language;
            String color;
            if (validLine[0] == null || validLine[0].isEmpty()) {
                return null;
            }
            if (!validId.add(validLine[0])) {
                return null;
            }
            id = Integer.parseInt(validLine[0]);

            //validar nome
            if (validLine[1] == null || validLine[1].isEmpty()) {
                return null;
            }
            if (!validName.add(validLine[1])) {
                return null;
            }
            name = validLine[1];

            //validar cor
            if (validLine[3] == null || validLine[3].isEmpty()) {
                return null;
            }
            if (!validColor.add(validLine[3])) {
                return null;
            }
            color = validLine[3];
            language = validLine[2];

            validPlayers.add(new Player(id, name, language, color));
        }

        return validPlayers;
    }

    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        if (nrValidPlayers(playerInfo)) {
            return false;
        }
        //Se o tamanho é o dobro dos players em jogo
        if (worldSize < playerInfo.length * 2) {
            return false;
        }

        List<Player> validPlayers = infoValidPlayers(playerInfo);
        if (validPlayers == null) {
            return false;
        }

        board = new Board(validPlayers, worldSize);
        return true;
    }

    public String getImagePng(int nrSquare) {
        return "";
    }

    public String[] getProgrammerInfo(int id) {
        return new String[]{"a", "b", "c", "d"};
    }

    public String getProgrammerInfoAsStr(int id) {
        return "";
    }

    public String[] getSlotInfo(int position) {
        return null;
    }

    public int getCurrentPlayerID() {
        return 0;
    }

    public boolean moveCurrentPlayer(int nrSpaces) {
        return false;
    }

    public boolean gameIsOver() {
        return false;
    }

    public ArrayList<String> getGameResults() {
        return null;
    }

    public JPanel getAuthorsPanel() {
        return null;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }
}
