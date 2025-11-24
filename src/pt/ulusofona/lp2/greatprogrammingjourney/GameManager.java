package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class GameManager {

    //================================================VARIÁVEIS=========================================================
    private Board board;
    private int currentPlayerId; // guarda o ID do jogador atual
    private int turnCount = 0;   // conta o número de jogadas
    private int lastMovedPlayerId = -1;
    //==================================================================================================================

    // Verifica se o número de jogadores está entre 2 e 4
    private boolean nrValidPlayers(String[][] playerInfo) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }
        return true;
    }

    private boolean validColors(String cor) {
        for (Color c : Color.values()) {
            if (c.name().equalsIgnoreCase(cor)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidLine(String[] validLine) {

        // Cada linha contém a informação de um jogador
        if (validLine == null || validLine.length != 4) {
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

    private String isValidColor(String color, HashSet<String> validColor) {
        if (color == null || color.isEmpty()) {
            return null;
        }

        if (!validColors(color)) {
            return null;
        }

        if (!validColor.add(color)) {
            return null;
        }
        color = color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase();
        return color;
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
            name = Player.isValidName(validLine[1], validName);
            //==========================================VALIDAR LINGUAGEM===============================================
            language = Player.isValidLanguage(validLine[2]);
            //=============================================VALIDAR COR==================================================
            color = isValidColor(validLine[3], validColor);

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

    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {
        if (abyssesAndTools == null) {
            return false;
        }

        if (!nrValidPlayers(playerInfo)) {
            return false;
        }

        if (worldSize < playerInfo.length * 2) {
            return false;
        }

        //validar a linha
        for (String[] line : abyssesAndTools) {
            if (line == null || line.length != 3) {
                return false;
            }

            String type = line[0];
            String subTypeStr = line[1];
            String boardPositionStr = line[2];

            if (!"0".equals(type) && !"1".equals(type)) {
                return false;
            }

            int subType;
            int boardPosition;

            try {
                subType = Integer.parseInt(subTypeStr);
                boardPosition = Integer.parseInt(boardPositionStr);
            } catch (NumberFormatException e) {
                return false;
            }

            if (type.equals("0")) {
                if (subType < 0 || subType > 9) {
                    return false;
                }
            }

            if (type.equals("1")) {
                if (subType < 0 || subType > 5) {
                    return false;
                }
            }

            if (boardPosition < 1 || boardPosition > worldSize) {
                return false;
            }
        }

        if (!createInitialBoard(playerInfo, worldSize)) {
            return false;
        }

        board.addEventsToSlot(abyssesAndTools);
        return true;
    }

    private int findLowestPlayerId(List<Player> validPlayers) {
        int lowerId = validPlayers.get(0).getId();
        for (Player player : validPlayers) {
            if (player.getId() < lowerId) {
                lowerId = player.getId();
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

        Slot thisSlot = board.encontraSlot(nrSquare);

        if (thisSlot.getEvent() != null) {
            return thisSlot.getEvent().getImage();
        }
        return null;
    }

    // Retorna as informações de um programador (player) num array de strings
    public String[] getProgrammerInfo(int id) {
        if (board == null || id < 1) {
            return null;
        }
        // percorre todas as slots e jogadores

        for (Slot slot : board.getSlots()) {
            for (Player player : slot.getPlayers()) {
                if (player.getId() == id) {
                    String[] infoPlayer = new String[7];
                    infoPlayer[0] = String.valueOf(player.getId());
                    infoPlayer[1] = player.getName();
                    infoPlayer[2] = player.getLanguage();
                    infoPlayer[3] = player.getColor();
                    infoPlayer[4] = String.valueOf(slot.getNrSlot());

                    List<Tool> tools = player.getTools();
                    if (tools == null || tools.isEmpty()) {
                        infoPlayer[5] = "No tools";
                    } else {
                        List<String> toolNames = new ArrayList<>();
                        for (Tool t : tools) {
                            toolNames.add(t.getName());
                        }
                        Collections.sort(toolNames);
                        infoPlayer[5] = String.join(",", toolNames);
                    }

                    if (!player.getIsAlive()) {
                        infoPlayer[6] = "Derrotado";
                    } else {
                        infoPlayer[6] = "Em jogo";
                    }
                    return infoPlayer;
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

        for (Slot slot : board.getSlots()) {
            Player player = slot.findPlayerByID(id);

            if (player != null) {
                List<String> sortedLanguages = player.getSortedLanguages(player.getLanguage());
                StringBuilder languagesInfo = player.playerLanguageInfo(sortedLanguages);

                List<Tool> playerTools = player.getTools();
                String toolsStr;

                if (playerTools == null || playerTools.isEmpty()) {
                    toolsStr = "No tools";
                } else {
                    List<String> toolNames = new ArrayList<>();
                    for (Tool t : playerTools) {
                        toolNames.add(t.getName());
                    }
                    Collections.sort(toolNames);
                    toolsStr = String.join(", ", toolNames);
                }

                return id + " | " + player.getName() + " | " + slot.getNrSlot() + " | " + toolsStr + " | " +
                        languagesInfo + " | Em Jogo";
            }
        }

        return null;
    }

    public String getProgrammersInfo() {
        if (board == null) {
            return "";
        }

        List<Player> alivePlayers = new ArrayList<>();

        for (Slot s : board.getSlots()) {
            for (Player p : s.getPlayers()) {
                if (p.getIsAlive() && !alivePlayers.contains(p)) {
                    alivePlayers.add(p);
                }
            }
        }

        // Se não houver jogadores vivos, devolve string vazia
        if (alivePlayers.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();


        return sb.toString();
    }

    // Retorna os IDs dos jogadores presentes numa determinada slot
    public String[] getSlotInfo(int position) {
        if (board == null) {
            return null;
        }

        Slot slot = board.encontraSlot(position);
        if (slot == null) {
            return null;
        }

        String playersStr = slot.buildPlayerIds(); // ids dos jogadores na casa
        String eventName = "";
        String eventTypeStr = "";

        Event event = slot.getEvent();
        if (event != null) {
            eventName = event.getName();

            if (event.getType() == EventType.ABYSS) {
                eventTypeStr = "A:" + event.getId();
            } else if (event.getType() == EventType.TOOL) {
                eventTypeStr = "T:" + event.getId();
            }
        }

        return new String[]{playersStr, eventName, eventTypeStr};
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
        for (Slot s : board.getSlots()) {
            Player p = s.findPlayerByID(getCurrentPlayerID());
            if (p != null) {
                currentPlayer = p;
                originSlot = s;
                found = true;
                break;
            }
        }

        if (!found) {
            return false; // jogador não encontrado
        }


        lastMovedPlayerId = currentPlayer.getId();
        currentPlayer.setLastDiceValue(nrSpaces);

        // Calcula destino e trata se passar do fim
        int lastSlot = board.getNrTotalSlots();
        int destination = originSlot.getNrSlot() + nrSpaces;

        // se ultrapassar o final volta para trás
        if (destination > lastSlot) {
            int tillTheEnd = lastSlot - originSlot.getNrSlot();
            int exceed = nrSpaces - tillTheEnd;
            destination = lastSlot - exceed;
        }

        // encontra a slot de destino
        Slot destinationSlot = board.encontraSlot(destination);

        if (destinationSlot == null) {
            return false;
        }

        // move o jogador da casa atual para a nova
        originSlot.removePlayer(currentPlayer);
        destinationSlot.addPlayer(currentPlayer);

        turnCount++; // aumenta o número de jogadas

        // Se o jogo acabou, o jogador atual é o vencedor
        if (gameIsOver()) {
            currentPlayerId = currentPlayer.getId();
            return true;
        }

        // Determina o próximo jogador (ordem crescente de ID)
        List<Player> allPlayers = new ArrayList<>();
        for (Slot slot : board.getSlots()) {
            for (Player p : slot.getPlayers()) {
                if (p.getIsAlive() && !allPlayers.contains(p)) {
                    allPlayers.add(p);
                }
            }
        }

        // ordena jogadores por ID
        allPlayers.sort(Comparator.comparingInt(p -> p.getId()));

        // encontra o índice do jogador atual
        int currentIndex = findAtualPlayerIndex(allPlayers);

        if (currentIndex == -1) {
            return false;
        }

        // calcula o próximo jogador
        int nextIndex = (currentIndex + 1) % allPlayers.size();
        currentPlayerId = allPlayers.get(nextIndex).getId();
        return true;
    }

    public String reactToAbyssOrTool() {
        if (board == null || lastMovedPlayerId == -1) {
            return null;
        }

        Player currentPlayer = null;
        Slot currentSlot = null;

        for (Slot s : board.getSlots()) {
            Player p = s.findPlayerByID(lastMovedPlayerId);
            if (p != null) {
                currentPlayer = p;
                currentSlot = s;
                break;
            }
        }

        if (currentPlayer == null || currentSlot == null) {
            return null;
        }

        Event event = currentSlot.getEvent();

        if (event != null) {

            event.playerInteraction(currentPlayer, board);
            return event.getName();
        }
        return null;
    }

    private int findAtualPlayerIndex(List<Player> allPlayers) {
        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).getId() == currentPlayerId) {
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
        return board.hasPlayerOnLastSlot();
    }

    private Slot findWinner() {
        if (board == null) {
            return null;
        }

        for (Slot slot : board.getSlots()) {
            if (slot.getNrSlot() == board.getNrTotalSlots()) {
                return slot;
            }
        }
        return null;
    }

    private ArrayList<String> findLastPlayers(String winnerName) {
        ArrayList<String> lastPlayers = new ArrayList<>();

        if (board == null) {
            return null;
        }

        for (int i = board.getNrTotalSlots() - 1; i >= 0; i--) {
            Slot slot = board.getSlots().get(i);
            for (Player player : slot.getPlayers()) {
                if (!player.getName().equals(winnerName)) {
                    lastPlayers.add(player.getName() + " " + slot.getNrSlot());
                }
            }
        }
        return lastPlayers;
    }

    // Gera um relatório com os resultados finais do jogo
    public ArrayList<String> getGameResults() {
        ArrayList<String> results = new ArrayList<>();

        if (board == null) {
            return results;
        }

        // encontra o vencedor
        Slot winnerSlot = findWinner();

        if (winnerSlot == null || winnerSlot.getPlayers().isEmpty()) {
            return results;
        }

        // nome do vencedor
        String winnerName = winnerSlot.getPlayers().get(0).getName();
        // nome dos outros jogadores
        ArrayList<String> lastPlayers = findLastPlayers(winnerName);

        results.add("THE GREAT PROGRAMMING JOURNEY");
        results.add("");
        results.add("NR. DE TURNOS");
        results.add((turnCount + 1) + "");
        results.add("");
        results.add("VENCEDOR");
        results.add(winnerName);
        results.add("");
        results.add("RESTANTES");
        results.addAll(lastPlayers);
        return results;
    }


    public boolean loadGame(File file) throws InvalidFileException, FileNotFoundException {

        return false;

    }

    public boolean saveGame(File file) {
        return false;
    }

    public JPanel getAuthorsPanel() {
        return null;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }
}