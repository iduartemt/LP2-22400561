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

    private List<Player> players = new ArrayList<>();

    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {

        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }

        if (worldSize < playerInfo.length * 2) {
            return false;
        }

        HashSet<String> validId = new HashSet<>();
        HashSet<String> validName = new HashSet<>();
        HashSet<String> validColor = new HashSet<>();

        for (int i = 0; i < playerInfo.length; i++) {
            String[] validLine = playerInfo[i];
            if (validLine == null || validLine.length == 0) {
                return false;
            }
            //validar id
            if (validLine[0] == null || validLine[0].isEmpty()) {
                return false;
            }
            if (!validId.add(validLine[0])) {
                return false;
            }
            //validar nome
            if (validLine[1] == null || validLine[1].isEmpty()) {
                return false;
            }
            if (!validName.add(validLine[1])) {
                return false;
            }
            //validar cor
            if (validLine[3] == null || validLine[3].isEmpty()) {
                return false;
            }
            if (!validColor.add(validLine[3])) {
                return false;
            }
        }
        return true;
    }

    public String getImagePng(int nrSquare) {
        return "";
    }

    public String[] getProgrammerInfo(int id) {
        return null;
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
        return null;
    }
}
