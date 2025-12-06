package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class GameManager {

    //================================================VARIÁVEIS=========================================================
    private Board board;
    private int currentPlayerId; // guarda o ID do jogador atual
    private int turnCount = 1;   // conta o número de jogadas
    private int lastMovedPlayerId = -1;
    //==================================================================================================================

    //================================================GETTERS/SETTERS===================================================

    public Board getBoard() {
        return board;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public int getLastMovedPlayerId() {
        return lastMovedPlayerId;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public void setLastMovedPlayerId(int lastMovedPlayerId) {
        this.lastMovedPlayerId = lastMovedPlayerId;
    }
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
        if (validLine == null || validLine.length < 4) {
            return false;
        }
        return true;
    }

    private Integer isValidPlayer(String idString, HashSet<String> validId) {
        if (idString == null || idString.isEmpty()) {
            return null;
        }
        if (!validId.add(idString)) {
            return null;
        }
        try {
            int id = Integer.parseInt(idString);
            if (id < 0) {
                return null;
            }

            return id;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String isValidColor(String color, HashSet<String> validColor) {
        if (color == null || color.isEmpty()) {
            return null;
        }

        if (!validColors(color)) {
            return null;
        }

        color = color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase();

        if (!validColor.add(color)) {
            return null;
        }
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

            String idStr = validLine[0].trim();
            String nameStr = validLine[1].trim();
            String langStr = validLine[2].trim();
            String colorStr = validLine[3].trim();

            Integer id = isValidPlayer(idStr, validId);
            if (id == null) {
                return null;
            }

            String name = Player.isValidName(nameStr, validName);
            if (name == null) {
                return null;
            }

            String language = Player.isValidLanguage(langStr);
            if (language == null) {
                return null;
            }

            String color = isValidColor(colorStr, validColor); // Usa a versão corrigida abaixo
            if (color == null) {
                return null;
            }

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

        turnCount = 1;

        // escolhe o jogador com ID mais baixo para começar
        currentPlayerId = findLowestPlayerId(validPlayers);

        return true;
    }

    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {
        // ========================LANGUAGE RESTRICTIONS ==============================
        //falha aqui, pode entrar logo aqui e nao no primeiro create
        if (abyssesAndTools == null) {
            return createInitialBoard(playerInfo, worldSize);
        }

        //validar a linha
        for (String[] line : abyssesAndTools) {
            if (line == null || line.length != 3) {
                return false;
            }

            String type = line[0].trim();
            String subTypeStr = line[1].trim();
            String boardPositionStr = line[2].trim();

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

        try {
            board.addEventsToSlot(abyssesAndTools);
        } catch (IllegalArgumentException e) {
            return false;
        }

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
                            if (t != null) {
                                toolNames.add(t.getName());
                            }
                        }
                        Collections.sort(toolNames);
                        infoPlayer[5] = String.join(";", toolNames);
                    }

                    if (!player.getIsAlive()) {
                        infoPlayer[6] = "Derrotado";
                    } else if (player.isTrapped()) {
                        infoPlayer[6] = "Preso";
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
        if (!isValidPlayerId(id)) {
            return null;
        }

        Player player = findPlayerById(id);
        if (player == null) {
            return null;
        }

        Slot playerSlot = findSlotWithPlayer(player);
        if (playerSlot == null) {
            return null;
        }

        String toolsInfo = formatPlayerTools(player);
        String languageInfo = getPlayerLanguageInfo(player);
        String statusInfo = getPlayerStatusText(player);

        return buildFormattedPlayerInfo(id, player, playerSlot, toolsInfo, languageInfo, statusInfo);
    }

    private boolean isValidPlayerId(int id) {
        return board != null && id >= 1;
    }

    private String getPlayerLanguageInfo(Player player) {
        List<String> sortedLanguages = player.getSortedLanguages(player.getLanguage());
        return player.playerLanguageInfo(sortedLanguages);
    }

    private String getPlayerStatusText(Player player) {
        if (!player.getIsAlive()) {
            return "Derrotado";
        } else if (player.isTrapped()) {
            return "Preso";
        } else {
            return "Em Jogo";
        }
    }

    private String buildFormattedPlayerInfo(int id, Player player, Slot slot, String toolsInfo,
                                           String languageInfo, String statusInfo) {
        return id + " | " + player.getName() + " | " + slot.getNrSlot() + " | " + toolsInfo + " | " +
                languageInfo + " | " + statusInfo;
    }

    public String getProgrammersInfo() {
        if (board == null) {
            return "";
        }

        List<Player> alivePlayers = collectAlivePlayers();

        if (alivePlayers.isEmpty()) {
            return "";
        }

        sortPlayersById(alivePlayers);

        return buildAlivePlayersInfo(alivePlayers);
    }

    private List<Player> collectAlivePlayers() {
        List<Player> alivePlayers = new ArrayList<>();

        for (Slot slot : board.getSlots()) {
            for (Player player : slot.getPlayers()) {
                if (player.getIsAlive()) {
                    alivePlayers.add(player);
                }
            }
        }

        return alivePlayers;
    }

    private void sortPlayersById(List<Player> players) {
        players.sort(Comparator.comparingInt(Player::getId));
    }

    private String buildAlivePlayersInfo(List<Player> alivePlayers) {
        StringBuilder sb = new StringBuilder();
        int playerNr = 0;

        for (Player player : alivePlayers) {
            appendPlayerInfo(sb, player, playerNr < alivePlayers.size() - 1);
            playerNr++;
        }

        return sb.toString();
    }

    private void appendPlayerInfo(StringBuilder sb, Player player, boolean hasMorePlayers) {
        sb.append(player.getName()).append(" : ");
        sb.append(formatPlayerTools(player));

        if (hasMorePlayers) {
            sb.append(" | ");
        }
    }

    private String formatPlayerTools(Player player) {
        List<Tool> tools = player.getTools();
        if (tools == null || tools.isEmpty()) {
            return "No tools";
        }

        List<String> toolNames = getValidToolNames(tools);
        if (toolNames.isEmpty()) {
            return "No tools";
        }

        Collections.sort(toolNames);
        return String.join(";", toolNames);
    }

    private List<String> getValidToolNames(List<Tool> tools) {
        List<String> toolNames = new ArrayList<>();
        for (Tool tool : tools) {
            if (tool != null) {
                toolNames.add(tool.getName());
            }
        }
        return toolNames;
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

    private void passTurnToNextPlayer() {
        boolean nextPlayerIsValid = false;
        List<Player> sortedPlayers = board.getPlayers();
        sortedPlayers.sort(Comparator.comparingInt(Player::getId));
        while (!nextPlayerIsValid) {
            int currentIndex = findAtualPlayerIndex(board.getPlayers());
            if (currentIndex != -1) {
                int nextIndex = (currentIndex + 1) % board.getPlayers().size();
                Player currentPlayer = board.getPlayers().get(nextIndex);
                currentPlayerId = currentPlayer.getId();
                if (currentPlayer.isLastMoveIsValid() && currentPlayer.getIsAlive()) {
                    nextPlayerIsValid = true;
                }
            }
        }
        for (Player player : sortedPlayers) {
            player.setLastMoveIsValid(true);
        }
        turnCount++;
    }

    // Move o jogador atual pelo tabuleiro
    public boolean moveCurrentPlayer(int nrSpaces) {
        if (!isValidMoveRequest(nrSpaces)) {
            return false;
        }

        Player currentPlayer = findCurrentPlayer();
        Slot originSlot = findPlayerSlot(currentPlayer);

        if (currentPlayer == null || originSlot == null) {
            return false; // jogador não encontrado
        }

        if (currentPlayer.isTrapped()) {
            handleTrappedPlayer(currentPlayer);
            return false;
        }

        if (!isValidMoveForLanguage(currentPlayer, nrSpaces)) {
            currentPlayer.setLastMoveIsValid(false);
            return false;
        }

        currentPlayer.setLastMoveIsValid(true);

        lastMovedPlayerId = currentPlayer.getId();
        updatePlayerHistory(currentPlayer, originSlot.getNrSlot());
        currentPlayer.setLastDiceValue(nrSpaces);

        int destination = calculateDestination(originSlot.getNrSlot(), nrSpaces);

        Slot destinationSlot = board.encontraSlot(destination);
        if (destinationSlot == null) {
            return false;
        }

        movePlayerBetweenSlots(currentPlayer, originSlot, destinationSlot);

        if (gameIsOver()) {
            currentPlayerId = currentPlayer.getId();
        }

        return true;
    }

    private boolean isValidMoveRequest(int nrSpaces) {
        return board != null && nrSpaces >= 1 && nrSpaces <= 6 && !gameIsOver();
    }

    private Player findCurrentPlayer() {
        for (Slot s : board.getSlots()) {
            Player p = s.findPlayerByID(getCurrentPlayerID());
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    private Slot findPlayerSlot(Player player) {
        if (player == null) {
            return null;
        }

        for (Slot s : board.getSlots()) {
            if (s.findPlayerByID(player.getId()) != null) {
                return s;
            }
        }
        return null;
    }

    private void handleTrappedPlayer(Player player) {
        System.out.println(player.getName() + " está preso e não se pode mover.");
    }

    private boolean isValidMoveForLanguage(Player player, int nrSpaces) {
        String[] splitLanguages = player.getLanguage().split(";");
        String firstLanguage = splitLanguages[0].trim();

        if (firstLanguage.equals("Assembly") && nrSpaces > 2) {
            return false;
        } else if (firstLanguage.equals("C") && nrSpaces > 3) {
            return false;
        }
        return true;
    }

    private void updatePlayerHistory(Player player, int currentSlotNr) {
        player.setPositionTwoMovesAgo(player.getPreviousPosition());
        player.setPreviousPosition(currentSlotNr);
    }

    private int calculateDestination(int originPosition, int nrSpaces) {
        int lastSlot = board.getNrTotalSlots();
        int destination = originPosition + nrSpaces;

        if (destination > lastSlot) {
            int tillTheEnd = lastSlot - originPosition;
            int exceed = nrSpaces - tillTheEnd;
            destination = lastSlot - exceed;
        }

        return destination;
    }

    private void movePlayerBetweenSlots(Player player, Slot originSlot, Slot destinationSlot) {
        originSlot.removePlayer(player);
        destinationSlot.addPlayer(player);
    }

    public String reactToAbyssOrTool() {
        if (board == null || lastMovedPlayerId == -1) {
            return null;
        }

        Player currentPlayer = findPlayerById(lastMovedPlayerId);
        Slot currentSlot = findSlotWithPlayer(currentPlayer);

        if (currentPlayer == null || currentSlot == null) {
            return null;
        }

        Event event = currentSlot.getEvent();
        if (event == null) {
            passTurnToNextPlayer();
            return null;
        }

        return processEventInteraction(currentPlayer, currentSlot, event);
    }

    private Player findPlayerById(int playerId) {
        for (Slot s : board.getSlots()) {
            Player p = s.findPlayerByID(playerId);
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    private Slot findSlotWithPlayer(Player player) {
        if (player == null) {
            return null;
        }

        for (Slot s : board.getSlots()) {
            if (s.findPlayerByID(player.getId()) != null) {
                return s;
            }
        }
        return null;
    }

    private String processEventInteraction(Player player, Slot slot, Event event) {
        int toolsBeforeInteraction = player.getTools().size();

        event.playerInteraction(player, board);

        passTurnToNextPlayer();

        if (isBlueScreenOfDeathEvent(event, player)) {
            return "O jogador caiu no " + event.getName() + " e perdeu o jogo :(";
        }

        if (event.getType() == EventType.ABYSS) {
            return processAbyssResult(event, toolsBeforeInteraction, player);
        }

        return "Jogador agarrou " + event.getName();
    }

    private boolean isBlueScreenOfDeathEvent(Event event, Player player) {
        return event.getName().equals("Blue Screen of Death") && !player.getIsAlive();
    }

    private String processAbyssResult(Event event, int toolsBefore, Player player) {
        if (player.getTools().size() < toolsBefore) {
            return "O abismo " + event.getName() + " foi anulado por uma!";
        } else {
            return "Caiu no abismo " + event.getName();
        }
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

            // Cria cópia e ordena por ID
            List<Player> sortedPlayers = new ArrayList<>(slot.getPlayers());
            sortedPlayers.sort(Comparator.comparingInt(Player::getId));

            for (Player player : sortedPlayers) {
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
        results.add((turnCount) + "");
        results.add("");
        results.add("VENCEDOR");
        results.add(winnerName);
        results.add("");
        results.add("RESTANTES");
        results.addAll(lastPlayers);
        return results;
    }


    public void loadGame(File file) throws InvalidFileException, FileNotFoundException {
        if (file == null) {
            return;
        }

        GameManager loadedGameManager = GamePersistenceManager.loadGame(file);

        // Copy the loaded state to this instance
        this.board = loadedGameManager.board;
        this.currentPlayerId = loadedGameManager.currentPlayerId;
        this.turnCount = loadedGameManager.turnCount;
        this.lastMovedPlayerId = loadedGameManager.lastMovedPlayerId;
    }

    public boolean saveGame(File file) {
        return GamePersistenceManager.saveGame(this, file);
    }

    public JPanel getAuthorsPanel() {
        return null;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }
}