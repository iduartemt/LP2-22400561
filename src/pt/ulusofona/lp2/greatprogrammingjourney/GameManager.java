package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.util.*;

enum Color {
    BROWN,
    PURPLE,
    GREEN,
    BLUE
}

public class GameManager {

    //================================================VARIÁVEIS=========================================================

    Board board;
    int currentPlayerId; // guarda o ID do jogador atual
    int turnCount = 0;   // conta o número de jogadas

    //==================================================================================================================

    // Verifica se o número de jogadores está entre 2 e 4
    private boolean nrValidPlayers(String[][] playerInfo) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }
        return true;
    }

    boolean isValidColor(String cor) {
        for (Color c : Color.values()) {
            if (c.name().equalsIgnoreCase(cor)) {
                return true;
            }
        }
        return false;
    }

    boolean isValidLine(String[] validLine) {

        // Cada linha contém a informação de um jogador
        if (validLine == null || validLine.length == 0) {
            return false;
        }
        return true;
    }

    private Integer isValidPlayer(String idString, HashSet<String> validId) {
        if (idString == null || idString.isEmpty()) {
            return null;
        }

        int id = Integer.parseInt(idString);

        if (id < 0) {
            return null;
        }

        //Verifica os duplicados
        if (!validId.add(idString)) {
            return null;
        }
        return id;
    }

    String isValidName(String name, HashSet<String> validName) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        //duplicados
        if (!validName.add(name)) {
            return null;
        }
        return name;
    }

    // Valida as informações de cada jogador e devolve uma lista de jogadores validos
    private List<Player> infoValidPlayers(String[][] playerInfo) {
        List<Player> validPlayers = new ArrayList<>();
        HashSet<String> validId = new HashSet<>();
        HashSet<String> validName = new HashSet<>();
        HashSet<String> validColor = new HashSet<>();

        for (int i = 0; i < playerInfo.length; i++) {

            String[] validLine = playerInfo[i];

            if (!isValidLine(validLine)) {
                return null;
            }

            int id;
            String name;
            String language;
            String color;

            //=============================================VALIDAR ID===================================================
            id = isValidPlayer(validLine[0], validId);
            //============================================VALIDAR NOME==================================================
            name = isValidName(validLine[1], validName);

            // ===== VALIDAR LINGUAGEM =====
            if (validLine[2] == null) {
                return null;
            }
            language = validLine[2];

            // ===== VALIDAR COR =====
            if (validLine[3] == null || validLine[3].isEmpty()) {
                return null;
            }

            if (!isValidColor(validLine[3])) {
                return null;
            }
            // impede cores repetidas
            if (!validColor.add(validLine[3])) {
                return null;
            }
            color = validLine[3];
            color = color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase();

            // adiciona jogador válido à lista
            validPlayers.add(new Player(id, name, language, color));
        }

        return validPlayers;
    }

    // Cria o tabuleiro inicial e define o jogador que começa
    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        // verifica número de jogadores
        if (!nrValidPlayers(playerInfo)) {
            return false;
        }

        // tamanho do tabuleiro deve ser pelo menos o dobro do número de jogadores
        if (worldSize < playerInfo.length * 2) {
            return false;
        }

        // valida os jogadores e cria a lista
        List<Player> validPlayers = infoValidPlayers(playerInfo);
        if (validPlayers == null) {
            return false;
        }

        // cria o tabuleiro com os jogadores e slots
        board = new Board(validPlayers, worldSize);
        turnCount = 0;

        // escolhe o jogador com ID mais baixo para começar
        currentPlayerId = findLowestPlayerId(validPlayers);

        return true;
    }

    int findLowestPlayerId(List<Player> validPlayers) {
        int lowerId = validPlayers.get(0).id;
        for (Player player : validPlayers) {
            if (player.id < lowerId) {
                lowerId = player.id;
            }
        }
        return lowerId;
    }

    // Devolve o nome da imagem associada a uma casa específica
    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > board.getNrTotalSlots()) {
            return null;
        }
        if (nrSquare == board.getNrTotalSlots()) {
            return "glory.png"; // última casa do tabuleiro
        }
        return null;
    }

    // Retorna as informações de um programador (player) num array de strings
    public String[] getProgrammerInfo(int id) {
        if (board == null || id < 1) {
            return null;
        }

        // percorre todas as slots e jogadores
        for (Slot slot : board.slots) {
            for (Player player : slot.players) {
                if (player.id == id) {
                    // cria array com informações do jogador
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

    // Retorna informações formatadas de um jogador em forma de string
    public String getProgrammerInfoAsStr(int id) {
        if (board == null || id < 1) {
            return null;
        }

        for (Slot slot : board.slots) {
            for (Player player : slot.players) {
                if (id == player.id) {
                    // separa e ordena linguagens por ordem alfabética
                    ArrayList<String> sortLanguage = new ArrayList<>(List.of(player.language.split(";")));
                    for (int i = 0; i < sortLanguage.size(); i++) {
                        sortLanguage.set(i, sortLanguage.get(i).trim());
                    }
                    sortLanguage.sort(String::compareTo);

                    // constrói string final com info do jogador
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

    // Retorna os IDs dos jogadores presentes numa determinada slot
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

    // Devolve o ID do jogador atual
    public int getCurrentPlayerID() {
        return currentPlayerId;
    }

    // Move o jogador atual pelo tabuleiro
    public boolean moveCurrentPlayer(int nrSpaces) {
        if (board == null || nrSpaces < 1 || nrSpaces > 6) {
            return false;
        }
        if (gameIsOver()) {
            return false;
        }

        Player currentPlayer = null;
        Slot originSlot = null;
        boolean found = false;

        // Encontra o jogador atual e a casa onde está
        for (Slot slot : board.slots) {
            Player p = slot.findPlayerByID(currentPlayerId);
            if (p != null) {
                currentPlayer = p;
                originSlot = slot;
                found = true;
                break;
            }
        }

        if (!found) {
            return false; // jogador não encontrado
        }

        // Calcula destino e trata se passar do fim
        int lastSlot = board.getNrTotalSlots();
        int destination = originSlot.nrSlot + nrSpaces;

        // se ultrapassar o final volta para trás
        if (destination > lastSlot) {
            int tillTheEnd = lastSlot - originSlot.nrSlot;
            int exceed = nrSpaces - tillTheEnd;
            destination = lastSlot - exceed;
        }

        // encontra a slot de destino
        Slot destinationSlot = encontraSlot(destination);

        if (destinationSlot == null) {
            return false;
        }

        // move o jogador da casa atual para a nova
        originSlot.removePlayer(currentPlayer);
        destinationSlot.addPlayer(currentPlayer);

        turnCount++; // aumenta o número de jogadas

        // Se o jogo acabou, o jogador atual é o vencedor
        if (gameIsOver()) {
            currentPlayerId = currentPlayer.id;
            return true;
        }

        // Determina o próximo jogador (ordem crescente de ID)
        List<Player> allPlayers = new ArrayList<>();
        for (Slot slot : board.slots) {
            allPlayers.addAll(slot.players);
        }

        // ordena jogadores por ID
        allPlayers.sort(Comparator.comparingInt(p -> p.id));

        // encontra o índice do jogador atual
        int currentIndex = encontraIndiceJogadorActual(allPlayers);

        if (currentIndex == -1) {
            return false;
        }

        // calcula o próximo jogador
        int nextIndex = (currentIndex + 1) % allPlayers.size();
        currentPlayerId = allPlayers.get(nextIndex).id;
        return true;
    }

    Slot encontraSlot(int nrSlot) {
        for (Slot slot : board.slots) {
            if (slot.nrSlot == nrSlot) {
                return slot;
            }
        }
        return null;
    }

    int encontraIndiceJogadorActual(List<Player> allPlayers) {
        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).id == currentPlayerId) {
                return i;
            }
        }
        return -1;
    }


    // Verifica se o jogo terminou
    public boolean gameIsOver() {
        if (board == null) {
            return false;
        }
        for (Slot slot : board.slots) {
            if (slot.nrSlot == board.getNrTotalSlots() && !slot.players.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Gera um relatório com os resultados finais do jogo
    public ArrayList<String> getGameResults() {
        ArrayList<String> results = new ArrayList<>();

        if (board == null) {
            return results;
        }

        // encontra o vencedor
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
        ArrayList<String> lastPlayers = new ArrayList<>();

        // lista dos restantes jogadores e suas posições
        for (int i = board.getNrTotalSlots() - 1; i >= 0; i--) {
            Slot slot = board.slots.get(i);
            for (Player player : slot.players) {
                if (!player.name.equals(winner)) {
                    lastPlayers.add(player.name + " " + slot.nrSlot);
                }
            }
        }

        results.add("THE GREAT PROGRAMMING JOURNEY");
        results.add("");
        results.add("NR. DE TURNOS");
        results.add((turnCount + 1) + "");
        results.add("");
        results.add("VENCEDOR");
        results.add(winner);
        results.add("");
        results.add("RESTANTES");
        results.addAll(lastPlayers);
        return results;
    }

    public JPanel getAuthorsPanel() {
        return null;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }
}
