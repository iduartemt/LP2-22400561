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
                            toolNames.add(t.getName());
                        }
                        Collections.sort(toolNames);
                        infoPlayer[5] = String.join(";", toolNames);
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
                String languagesInfo = player.playerLanguageInfo(sortedLanguages);

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
                    toolsStr = String.join(";", toolNames);
                }

                String isAliveText;
                if (!player.getIsAlive()) {
                    isAliveText = "Derrotado";
                } else if (player.isTrapped()) { // <--- ADICIONA ISTO
                    isAliveText = "Preso";
                } else {
                    isAliveText = "Em Jogo";
                }
                return id + " | " + player.getName() + " | " + slot.getNrSlot() + " | " + toolsStr + " | " +
                        languagesInfo + " | " + isAliveText;

            }
        }

        return null;
    }

    public String getProgrammersInfo() {
        if (board == null) {
            return "";
        }

        List<Player> alivePlayers = new ArrayList<>();

        // Percorre os slots para manter a ordem do tabuleiro (Slot 1, Slot 2...)
        for (Slot s : board.getSlots()) {
            //List<Player> playersInSlot = new ArrayList<>();
            // Recolhe jogadores vivos neste slot
            for (Player p : s.getPlayers()) {
                if (p.getIsAlive()) {
                    alivePlayers.add(p);
                }
            }
        }
        // Se não houver jogadores vivos, devolve string vazia
        if (alivePlayers.isEmpty()) {
            return "";
        }

        // ORDENA POR ID (Critério de desempate dentro da mesma casa)
        alivePlayers.sort(Comparator.comparingInt(Player::getId));

        StringBuilder sb = new StringBuilder();
        int playerNr = 0;

        for (Player player : alivePlayers) {
            sb.append(player.getName()).append(" : ");

            List<Tool> tools = player.getTools();
            if (tools == null || tools.isEmpty()) {
                sb.append("No tools");
            } else {
                // Garante que as ferramentas também estão ordenadas alfabeticamente
                List<String> toolNames = new ArrayList<>();
                for (Tool t : tools) {
                    toolNames.add(t.getName());
                }
                Collections.sort(toolNames); // Ordena ferramentas

                sb.append(String.join(";", toolNames));
            }

            playerNr++;
            if (playerNr != alivePlayers.size()) {
                sb.append(" | ");
            }
        }

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
        //
        return currentPlayerId;
    }

    private void passTurnToNextPlayer() {
        List<Player> allPlayers = new ArrayList<>();
        for (Slot slot : board.getSlots()) {
            for (Player p : slot.getPlayers()) {
                if (p.getIsAlive() && !allPlayers.contains(p)) {
                    allPlayers.add(p);
                }
            }
        }
        allPlayers.sort(Comparator.comparingInt(Player::getId));
        int currentIndex = findAtualPlayerIndex(allPlayers);
        if (currentIndex != -1) {
            int nextIndex = (currentIndex + 1) % allPlayers.size();
            currentPlayerId = allPlayers.get(nextIndex).getId();
        }
        turnCount++;
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

        if (currentPlayer.isTrapped()) {
            // O jogador está preso no Infinite Loop.
            // Ele não se move, mas o turno conta-se como "jogado" (passa a vez).
            passTurnToNextPlayer();
            System.out.println(currentPlayer.getName() + " está preso e não se pode mover.");

            return true; // Retorna true porque a ação de "passar a vez preso" foi válida
        }

        String[] splitLanguages = currentPlayer.getLanguage().split(";");
        String firstLanguage = splitLanguages[0].trim();

        if (firstLanguage.equals("Assembly") && nrSpaces > 2) {
            passTurnToNextPlayer();
            return false;
        } else if (firstLanguage.equals("C") && nrSpaces > 3) {
            passTurnToNextPlayer();
            return false;
        }

        passTurnToNextPlayer();

        lastMovedPlayerId = currentPlayer.getId();
        currentPlayer.setLastDiceValue(nrSpaces);

        // Atualiza o histórico: O que era "anterior" passa a ser "há 2 jogadas"
        currentPlayer.setPositionTwoMovesAgo(currentPlayer.getPreviousPosition());
        // A posição onde estou agora (antes de me mover) passa a ser a "anterior"
        currentPlayer.setPreviousPosition(originSlot.getNrSlot());

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

        // Se o jogo acabou, o jogador atual é o vencedor
        if (gameIsOver()) {
            currentPlayerId = currentPlayer.getId();
            return true;
        }

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
            // 1. Guardar quantas ferramentas o jogador tem ANTES da interação
            int toolsBefore = currentPlayer.getTools().size();

            // 2. A interação acontece (ferramentas podem ser removidas aqui)
            event.playerInteraction(currentPlayer, board);

            // Verificação especial para BSOD (Morte)
            if (event.getName().equals("Blue Screen of Death") && !currentPlayer.getIsAlive()) {
                return "O jogador caiu no " + event.getName() + " e perdeu o jogo :(";
            }

            // 3. Lógica para ABISMOS
            if (event.getType() == EventType.ABYSS) {
                // Se tem MENOS ferramentas agora do que antes, é porque usou uma para se salvar
                if (currentPlayer.getTools().size() < toolsBefore) {
                    return "O abismo " + event.getName() + " foi anulado por uma!";
                } else {
                    // Se o número é igual, sofreu a penalidade
                    return "Caiu no abismo " + event.getName();
                }
            }

            // Caso seja uma Ferramenta (que apenas se apanha)
            return "Jogador agarrou " + event.getName();
        }

        //devia estar no move ( so da pass caso haja react e se o evento nao for null)
        //   passTurnToNextPlayer();

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

        try {
            Scanner scanner = new Scanner(file);
            if (!scanner.hasNextLine()) {
                throw new InvalidFileException();
            }
            int worldSize = Integer.parseInt(scanner.nextLine());
            if (!scanner.hasNextLine()) {
                throw new InvalidFileException();
            }
            List<String[]> abyssesAndTools = new ArrayList<>();
            String[] events = scanner.nextLine().split(",");
            for (String event : events) {

                String[] eventLines = event.split(":");
                String[] formatedEventLines = {eventLines[1], eventLines[2], eventLines[0]};
                abyssesAndTools.add(formatedEventLines);
            }
            if (!scanner.hasNextLine()) {
                throw new InvalidFileException();
            }
            String[] playersStr = scanner.nextLine().split(",");
            Map<Integer, Player> players = new HashMap<>();
            List<Player> playersList = new ArrayList<>();
            HashMap<Integer, String[]> toolsOfPlayers = new HashMap<>();
            for (String playerStr : playersStr) {
                System.out.println(playerStr);
                String[] playerInfo = playerStr.split(":");
                int playerId = Integer.parseInt(playerInfo[0]);
                String playerName = playerInfo[1];
                String playerLanguage = playerInfo[2];
                String[] playerTools = playerInfo[3].split(";");
                toolsOfPlayers.put(playerId, playerTools);
                String playerColor = playerInfo[4];
                boolean playerIsAlive = Boolean.parseBoolean(playerInfo[5]);
                int playerLastDiceValue = Integer.parseInt(playerInfo[6]);
                int playerPreviousPosition = Integer.parseInt(playerInfo[7]);
                int positionTwoMovesAgo = Integer.parseInt(playerInfo[8]);
                Player player = new Player(playerId, playerName, playerLanguage,
                        playerColor, playerIsAlive, playerLastDiceValue,
                        playerPreviousPosition, positionTwoMovesAgo
                );
                players.put(playerId, player);
                playersList.add(player);
            }
            // ler id current player e nrTurno
            String[][] abyssesAndToolsFormated = new String[abyssesAndTools.size()][3];
            for (int i = 0; i < abyssesAndTools.size(); i++) {
                abyssesAndToolsFormated[i] = abyssesAndTools.get(i);
            }
            board = new Board(playersList, worldSize, abyssesAndToolsFormated);
            for (Player player : board.getPlayers()) {
                for (String toolStr : toolsOfPlayers.get(player.getId())) {
                    Tool tool = board.getToolsHashMap().get(toolStr);
                    player.addTool(tool);
                }
            }
            if (!scanner.hasNext()) {
                throw new InvalidFileException();
            }
            String[] currentGameInfo = scanner.nextLine().split(":");
            String currentPlayerId = currentGameInfo[0];
            String turnCount = currentGameInfo[1];
            String lastMovedPlayerId = currentGameInfo[2];

            this.currentPlayerId = Integer.parseInt(currentPlayerId);
            this.turnCount = Integer.parseInt(turnCount);
            this.lastMovedPlayerId = Integer.parseInt(lastMovedPlayerId);

            if (!scanner.hasNextLine()) {
                throw new InvalidFileException();
            }
            String playersPositionStr = scanner.nextLine();
            for (String playersPosAndIdStr : playersPositionStr.split(",")) {
                String[] playerPosAndId = playersPosAndIdStr.split(":");
                int playerPos = Integer.parseInt(playerPosAndId[0]);
                int playerId = Integer.parseInt(playerPosAndId[1]);
                for (Slot slot : board.getSlots()) {
                    if (slot.getNrSlot() == playerPos) {
                        slot.addPlayer(players.get(playerId));
                    }
                }
            }

            scanner.close();
        } catch (InvalidFileException | FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new InvalidFileException();
        }

    }

    public boolean saveGame(File file) {
        try {
            FileWriter writer = new FileWriter(file);

            //primeira linha tamanho do board
            writer.write(board.getNrTotalSlots() + "\n");

            //por cada ferramenta/abisdmo do board
            //temos pos:abismo/ferramenta(0/1):tipo(0-5/0-9)
            for (Slot slot : board.getSlots()) {
                Event event = slot.getEvent();
                if (event != null) {
                    String eventType = event.getType() == EventType.ABYSS ? "0" : "1";
                    writer.write(slot.getNrSlot() + ":" + eventType + ":" + event.getId());
                    writer.write(",");
                }

            }
            writer.write("\n");

            //escrever info sobre os jogadores
            for (Slot slot : board.getSlots()) {
                List<Player> players = slot.getPlayers();
                for (Player player : players) {
                    writer.write(player.getId() + ":");
                    writer.write(player.getName() + ":");
                    writer.write(player.getLanguage() + ":");
                    for (Tool t : player.getTools()) {
                        writer.write(t.getName());
                        writer.write(";");
                    }
                    writer.write(":");
                    writer.write(player.getColor() + ":");
                    writer.write(player.getIsAlive() + ":");
                    writer.write(player.getLastDiceValue() + ":");
                    writer.write(player.getPreviousPosition() + ":");
                    writer.write(player.getPositionTwoMovesAgo() + ":");
                    writer.write(",");
                }
            }
            writer.write("\n");

            // escrever jogador atual e numero do turno (jogAtual:nrTurno)
            writer.write(currentPlayerId + ":" + turnCount + ":" + lastMovedPlayerId);
            writer.write("\n");

            for (Slot slot : board.getSlots()) {
                for (Player player : slot.getPlayers()) {
                    writer.write(slot.getNrSlot() + ":" + player.getId() + ",");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("sucesso a salvar em " + file.getAbsolutePath());

        return true;
    }

    public JPanel getAuthorsPanel() {
        return null;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }
}