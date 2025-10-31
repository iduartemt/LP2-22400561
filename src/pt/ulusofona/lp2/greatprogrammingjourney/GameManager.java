package pt.ulusofona.lp2.greatprogrammingjourney;

import kotlin.jvm.internal.markers.KMutableMap;

import javax.swing.*;
import java.util.*;

enum Color {
    BROWN,
    PURPLE,
    GREEN,
    BLUE
}

public class GameManager {

    //======VARIAVEIS===============
    Board board;
    int currentPlayerId;
    int turnCount = 0;
    //==============================


    //Se tem esta entre 2 e 4 players
    private boolean nrValidPlayers(String[][] playerInfo) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }
        return true;
    }

    //Se cada jogador tem a informacao valida(id,nome,cor)
    private List<Player> infoValidPlayers(String[][] playerInfo) {
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
            if (validLine[0] == null || validLine[0].isEmpty() || Integer.parseInt(validLine[0]) < 0) {
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

            //Validar linguagem
            if (validLine[2] == null) {
                return null;
            }

            language = validLine[2];


            //validar cor
            if (validLine[3] == null || validLine[3].isEmpty()) {
                return null;
            }

            boolean ifColorValid = false;

            for (Color c : Color.values()) {
                if (c.name().equalsIgnoreCase(validLine[3])) {
                    ifColorValid = true;
                    break;
                }
            }
            if (!ifColorValid) {
                return null;
            }

            if (!validColor.add(validLine[3])) {
                return null;
            }
            color = validLine[3];
            color = color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase();

            validPlayers.add(new Player(id, name, language, color));
        }

        return validPlayers;
    }

    //Criar a board inicial
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

        int lowerId = validPlayers.get(0).id;

        for (Player player : validPlayers) {
            if (player.id < lowerId) {
                lowerId = player.id;
            }
        }
        currentPlayerId = lowerId;

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
        if (board == null || id < 1) {
            return null;
        }

        for (Slot slot : board.slots) {
            for (Player player : slot.players) {
                if (id == player.id) {
                    ArrayList<String> sortLanguage = new ArrayList<>(List.of(player.language.split(";")));
                    for (int i = 0; i < sortLanguage.size(); i++) {
                        sortLanguage.set(i, sortLanguage.get(i).trim());
                    }
                    sortLanguage.sort(String::compareTo);

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < sortLanguage.size(); i++) {
                        sb.append(sortLanguage.get(i));
                        if (i != sortLanguage.size() - 1) {
                            sb.append("; ");
                        }
                    }
                    return id + " | " + player.name + " | " + slot.nrSlot + " | " + sb + " | Em Jogo";
                }
            }
        }
        return null;
    }

    public String[] getSlotInfo(int position) {
        if (board == null || position < 1 || position > board.getNrTotalSlots()) {
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
        return currentPlayerId;
    }

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (board == null || nrSpaces < 1 || nrSpaces > 6) {
            return false;
        }

        Player currentPlayer = null;
        Slot originSlot = null;
        boolean found = false;

        //Encontrar o player atual e a slot em que esta
        for (int i = 0; i < board.slots.size(); i++) {
            Slot slot = board.slots.get(i);
            for (int j = 0; j < slot.players.size(); j++) {
                Player p = slot.players.get(j);
                if (p.id == currentPlayerId) {
                    currentPlayer = p;
                    originSlot = slot;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        if (!found) {
            return false; //nao encontrou player
        }

        //Destino e ultima casa
        int lastSlot = board.getNrTotalSlots();
        int destination = originSlot.nrSlot + nrSpaces;

        if (destination > lastSlot) {
            int tillTheEnd = lastSlot - originSlot.nrSlot;
            int exceed = nrSpaces - tillTheEnd;
            destination = lastSlot - exceed;
        }


        //ver qual é a proxima slot
        Slot destinationSlot = null;
        for (int i = 0; i < board.slots.size(); i++) {
            Slot slot = board.slots.get(i);
            if (slot.nrSlot == destination) {
                destinationSlot = slot;
                break;
            }
        }

        if (destinationSlot == null) {
            return false;
        }

        //remover e adicionar player
        originSlot.removePlayer(currentPlayer);
        destinationSlot.addPlayer(currentPlayer);
        turnCount++;

        //proximo player
        List<Player> allPlayers = new ArrayList<>();
        for (int i = 0; i < board.slots.size(); i++) {
            Slot slot = board.slots.get(i);
            for (int j = 0; j < slot.players.size(); j++) {
                allPlayers.add(slot.players.get(j));
            }
        }

        for (int i = 0; i < allPlayers.size() - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < allPlayers.size(); j++) {
                if (allPlayers.get(j).id < allPlayers.get(minIdx).id) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                Player tmp = allPlayers.get(i);
                allPlayers.set(i, allPlayers.get(minIdx));
                allPlayers.set(minIdx, tmp);
            }
        }

        int currentIndex = -1;
        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).id == currentPlayerId) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1) {
            return false;
        }

        int nextIndex = (currentIndex + 1) % allPlayers.size();
        currentPlayerId = allPlayers.get(nextIndex).id;
        return true;
    }

    public boolean gameIsOver() {
        if (board == null) {
            return false;
        }

        for (Slot slot : board.slots) {
            if (slot.nrSlot == board.getNrTotalSlots()) {
                if (!slot.players.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<String> getGameResults() {
        ArrayList<String> results = new ArrayList<>();

        if (board == null) {
            return results;
        }

        //Encontrar o vencedor
        Slot findWinner = null;
        for (Slot slot : board.slots) {
            if (slot.nrSlot == board.getNrTotalSlots()) {
                findWinner = slot;
                break;
            }
        }

        if (findWinner == null || findWinner.players.isEmpty()) {
            return results;
        }
        String winner = findWinner.players.get(0).name;

        //Encontras players restantes
        HashMap<Integer, String> lastPlayers = new HashMap<>();
        for (Slot slot : board.slots) {
            for (Player player : slot.players) {
                if (!player.name.equals(winner)) {
                    lastPlayers.put(slot.nrSlot, player.name + " " + slot.nrSlot);
                }
            }
        }

        Set<Map.Entry<Integer, String>> entries = lastPlayers.entrySet();

        ArrayList<Map.Entry<Integer, String>> ordenar = new ArrayList<>(entries);

        Collections.sort(ordenar, Comparator.comparing((Map.Entry<Integer, String> entry) -> entry.getKey()).reversed());

        results.add("THE GREAT PROGRAMMING JOURNEY");
        results.add("");
        results.add("NR. DE TURNOS");
        results.add(turnCount + "");
        results.add("");
        results.add("VENCEDOR");
        results.add(winner);
        results.add("");
        results.add("RESTANTES");
        for (Map.Entry<Integer, String> entry : ordenar) {
            results.add(entry.getValue());
        }
        return results;
    }

    public JPanel getAuthorsPanel() {
        return null;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }
}
