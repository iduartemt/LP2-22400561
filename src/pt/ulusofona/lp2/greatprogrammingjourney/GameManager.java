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
    //==================================================================================================================

    public Board getBoard() {
        return board;
    }

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

    private String validateColor(String color, HashSet<String> validColor) {
        if (color == null) {
            return null;
        }

        if (color.equals("RANDOM")) {
            for (Color c : Color.values()) {
                String corStr = c.toString().substring(0, 1).toUpperCase() + c.toString().substring(1).toLowerCase();

                if (!validColor.contains(corStr)) {
                    System.out.println("Atribui a cor: " + corStr);
                    return corStr;
                }
            }
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
    private List<Player> validateAndCreatePlayersByLine(String[][] playerInfo) {
        List<Player> validPlayers = new ArrayList<>();
        HashSet<String> usedIds = new HashSet<>();
        HashSet<String> usedNames = new HashSet<>();
        HashSet<String> usedColors = new HashSet<>();

        for (int i = 0; i < playerInfo.length; i++) {
            String[] playerLine = playerInfo[i];

            if (playerLine == null || playerLine.length < 2 || playerLine.length > 4) {
                return null;
            }

            String idStr = playerLine[0].trim();
            String nameStr = playerLine[1].trim();
            String langStr = playerLine[2].trim();
            String colorStr = "RANDOM";
            if (playerLine.length == 4) {
                colorStr = playerLine[3].trim();
            }

            Integer id = Player.isValidId(idStr);
            if (id == null || usedIds.contains(idStr)) {
                System.out.println("funcao infoValidPlayers: id == null");
                return null;
            }

            String name = Player.isValidName(nameStr);
            if (name == null || usedNames.contains(nameStr)) {
                System.out.println("funcao infoValidPlayers: name == null");
                return null;
            }

            String language = Player.isValidLanguage(langStr);
            if (language == null) {
                System.out.println("funcao infoValidPlayers: language == null");
                return null;
            }

            String color = validateColor(colorStr, usedColors);
            if (color == null) {
                System.out.println("funcao infoValidPlayers: color == null");
                return null;
            }

            validPlayers.add(new Player(id, name, language, color));
            usedIds.add(idStr);
            usedNames.add(name);
            usedColors.add(color);
        }

        return validPlayers;
    }


    // Cria o tabuleiro inicial e define o jogador que começa
    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        // verifica número de jogadores
        if (!nrValidPlayers(playerInfo)) {
            System.out.println("numero de player invalido");
            return false;
        }

        // tamanho do tabuleiro deve ser pelo menos o dobro do número de jogadores
        if (worldSize < playerInfo.length * 2) {
            System.out.println("worldsize invalido");
            return false;
        }

        // valida os jogadores e cria a lista
        List<Player> validPlayers = validateAndCreatePlayersByLine(playerInfo);
        if (validPlayers == null) {
            System.out.println("playerInfo invalida");
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
        return board.getImagePng(nrSquare);
    }

    // Retorna as informações de um programador (player) num array de strings
    public String[] getProgrammerInfo(int id) {
        if (board == null || id < 1) {
            return null;
        }

        Slot slot = board.getSlotOfPlayer(id); // Busca direta
        if (slot == null) {
            return null;
        }

        Player player = slot.findPlayerByID(id);

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

        if (player.getState() == PlayerState.DERROTADO) {
            infoPlayer[6] = "Derrotado";
        } else if (player.getState() == PlayerState.PRESO) {
            infoPlayer[6] = "Preso";
        } else {
            infoPlayer[6] = "Em jogo";
        }

        return infoPlayer;
    }


    // Retorna informações formatadas de um jogador em forma de string
    public String getProgrammerInfoAsStr(int id) {
        if (board == null || id < 1) {
            return null;
        }

        Slot slot = board.getSlotOfPlayer(id);
        if (slot == null) {
            return null;
        }

        Player player = slot.findPlayerByID(id);

        List<String> sortedLanguages = player.getSortedLanguages(player.getLanguage());
        String languagesInfo = String.join("; ", sortedLanguages);

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

        String playerState;
        switch (player.getState()) {
            case DERROTADO:
                playerState = "Derrotado";
                break;
            case PRESO:
                playerState = "Preso";
                break;
            default:
                playerState = "Em Jogo";
                break;
        }

        return id + " | " + player.getName() + " | " + slot.getNrSlot() + " | " + toolsStr + " | " +
                languagesInfo + " | " + playerState;
    }

    public String getProgrammersInfo() {
        if (board == null) {
            return "";
        }

        List<Player> alivePlayers = new ArrayList<>();

        // Percorre os slots para manter a ordem do tabuleiro (Slot 1, Slot 2...)
        for (Player player : board.getPlayers()) {
            if (player.getState() != PlayerState.DERROTADO) {
                alivePlayers.add(player);
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
        return board.getSlotInfo(position);
    }

    // Devolve o ID do jogador atual
    public int getCurrentPlayerID() {
        return currentPlayerId;
    }

    private void passTurnToNextPlayer() {
        List<Player> sortedPlayers = new ArrayList<>(board.getPlayers());
        sortedPlayers.sort(Comparator.comparingInt(Player::getId));

        int currentIndex = -1;
        for (int i = 0; i < sortedPlayers.size(); i++) {
            if (sortedPlayers.get(i).getId() == currentPlayerId) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1) {
            return; // Should not happen in a valid game
        }

        // Start looking for the next valid player from the one after the current one
        for (int i = 1; i <= sortedPlayers.size(); i++) {
            int nextIndex = (currentIndex + i) % sortedPlayers.size();
            Player nextPlayer = sortedPlayers.get(nextIndex);

            if (nextPlayer.getState() != PlayerState.DERROTADO) {
                currentPlayerId = nextPlayer.getId();
                turnCount++;

                // Reset move validation for all players for the new turn
                for (Player player : board.getPlayers()) {
                    player.setLastMoveIsValid(true);
                }
                return; // Found the next player, so exit
            }
        }
        turnCount++;
    }

    // Move o jogador atual pelo tabuleiro
    // Em GameManager.java
    public boolean moveCurrentPlayer(int nrSpaces) {
        if (board == null || nrSpaces < 1 || nrSpaces > 6 || gameIsOver()) {
            return false;
        }

        Player currentPlayer = board.getSlotOfPlayer(getCurrentPlayerID()).findPlayerByID(getCurrentPlayerID());
        if (currentPlayer == null) {
            return false;
        }

        if (currentPlayer.getState() == PlayerState.PRESO) {
            System.out.println(currentPlayer.getName() + " está preso e não se pode mover.");
            return false;
        }

        if (!currentPlayer.canMove(nrSpaces)) {
            currentPlayer.setLastMoveIsValid(false);
            System.out.println("linguagem invalida");
            return false;
        }

        currentPlayer.setLastMoveIsValid(true);
        currentPlayer.setLastDiceValue(nrSpaces);

        // A chamada agora é delegada ao Board
        boolean moved = board.movePlayer(currentPlayer, nrSpaces);

        if (moved && gameIsOver()) {
            currentPlayerId = currentPlayer.getId();
        }

        return moved;
    }

    public String reactToAbyssOrTool() {
        // 1. Validações iniciais (Guard Clauses)
        if (board == null || currentPlayerId == -1) {
            return null;
        }

        // Alterado: usa currentPlayerId
        Slot currentSlot = board.getSlotOfPlayer(currentPlayerId);
        if (currentSlot == null) {
            return null;
        }


        // Como já temos o slot, buscar o objeto Player é direto
        Player currentPlayer = currentSlot.findPlayerByID(currentPlayerId);

        if (currentPlayer.getState() == PlayerState.PRESO) {
            passTurnToNextPlayer();
            return "Ciclo Infinito! O jogador ficou preso na casa.";
        }

        // 3. Verificar se existe evento (Abismo ou Ferramenta)
        Event event = currentSlot.getEvent();
        String message = null;

        if (event != null) {

            // Se houver evento, interage e define a mensagem de retorno
            message = event.playerInteraction(currentPlayer, board);

            System.out.println(message);
        }

        // 4. Passar o turno
        // Como isto acontece SEMPRE (tendo evento ou não), basta chamar uma vez no final
        passTurnToNextPlayer();

        return message;
    }

    // Verifica se o jogo terminou
    public boolean gameIsOver() {
        if (board == null) {
            return false;
        }
        return board.hasPlayerOnLastSlot();
    }

    private Slot findWinner() {
        return board.findWinner();
    }

    private ArrayList<String> findLastPlayers(String winnerName) {
        return board.findLastPlayers(winnerName);
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
      /*  if (file == null) {
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
                String stateStr = playerInfo[5]; // Lê a string "EM_JOGO", "PRESO", etc.
                PlayerState state = PlayerState.valueOf(stateStr);
                int playerLastDiceValue = Integer.parseInt(playerInfo[6]);
                int playerPreviousPosition = Integer.parseInt(playerInfo[7]);
                int positionTwoMovesAgo = Integer.parseInt(playerInfo[8]);
                Player player = new Player(playerId, playerName, playerLanguage,
                        playerColor, state, playerLastDiceValue, // <--- Passas o Enum 'state' aqui
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
            String turnCount = currentGame.info[1];

            this.currentPlayerId = Integer.parseInt(currentPlayerId);
            this.turnCount = Integer.parseInt(turnCount);

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
        }*/

    }

    public boolean saveGame(File file) {
       /* try {
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
                    writer.write(player.getState().name() + ":");
                    writer.write(player.getLastDiceValue() + ":");
                    writer.write(player.getPreviousPosition() + ":");
                    writer.write(player.getPositionTwoMovesAgo() + ":");
                    writer.write(",");
                }
            }
            writer.write("\n");

            // escrever jogador atual e numero do turno (jogAtual:nrTurno)
            writer.write(currentPlayerId + ":" + turnCount);
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
*/
        return true;
    }

    public JPanel getAuthorsPanel() {
        JPanel a = new JPanel();
        return a;
    }

    public HashMap<String, String> customizeBoard() {
        HashMap<String, String> a = new HashMap<String, String>();
        a.put("a", "a");
        return a;
    }
}
