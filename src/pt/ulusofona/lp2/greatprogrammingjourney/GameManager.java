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

    public boolean validBoard() {
        return board != null;
    }

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

            int id;
            String name;
            String language;
            String color;

            //Colunas
            //validar id
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
        if (!nrValidPlayers(playerInfo)) {
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
        if (nrSquare < 1 || nrSquare > board.getNrTotalSlots()) {
            return null;
        }
        if (nrSquare == board.getNrTotalSlots()) {
            return "glory.png";
        }
        return null;
    }

    public String[] getProgrammerInfo(int id) {
        if (board == null || id < 1) {
            return null;
        }

        for (Slot slot : board.slots) {
            for (Player player : slot.players) {
                if (player.id == id) {
                    String[] foundedPlayer = new String[5];
                    foundedPlayer[0] = String.valueOf(player.id);
                    foundedPlayer[1] = player.name;
                    foundedPlayer[2] = player.language;
                    foundedPlayer[3] = player.color;
                    foundedPlayer[4] = String.valueOf(slot.nrSlot);
                    return foundedPlayer;
                }
            }
        }
        return null;
    }

    public String getProgrammerInfoAsStr(int id) {
        if (!validBoard() || id < 1) {
            return null;
        }

        for (Slot slot : board.slots) {
            for (Player player : slot.players) {
                if (id == player.id) {
                    return id + " | " + player.name + " | " + slot.nrSlot + " | " + player.language + " | Em jogo";
                }
            }
        }
        return null;
    }

    public String[] getSlotInfo(int position) {
        if (position < 1 || position > board.getNrTotalSlots()) {
            return null;
        }
        for (Slot slot : board.slots) {
            if (slot.nrSlot == position) {
                if (slot.players.isEmpty()) {
                    return new String[]{""};
                }
                StringBuilder sb = new StringBuilder();
                for (Player player : slot.players) {
                    if (!sb.isEmpty()) {
                        sb.append(",");
                    }
                    sb.append(player.id);
                }
                return new String[]{sb.toString()};
            }
        }
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
