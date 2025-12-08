package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GameManager {
    //====================================================VARIAVEIS GLOBAIS=================================================
    // Tabuleiro de jogo
    private Board board;
    // ID do jogador que tem o turno atual
    private int currentPlayerId;
    // Número total de turnos decorridos (começa em 1)
    private int turnCount = 1;
    //======================================================================================================================

    // Verifica se o número de jogadores é válido
    //Tem de haver pelo menos 2 e no máximo 4 jogadores
    private boolean nrValidPlayers(String[][] playerInfo) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }
        return true;
    }

    // Verifica se uma determinada string corresponde a uma cor válida do enum Color.
    private boolean validColors(String cor) {
        for (Color c : Color.values()) {
            if (c.name().equalsIgnoreCase(cor)) {
                return true;
            }
        }
        return false;
    }

    //Valida e devolve a cor final para um jogador
    // Se for "RANDOM", escolhe a primeira cor ainda não usada
    // Se for uma cor inválida, devolve null
    // Se a cor já estiver a ser usada, devolve null
    private String validateColor(String color, HashSet<String> validColor) {
        if (color == null) {
            return null;
        }

        // Caso especial: cor aleatória
        if (color.equals("RANDOM")) {
            for (Color c : Color.values()) {
                String corStr = c.toString().substring(0, 1).toUpperCase() + c.toString().substring(1).toLowerCase();

                // Se ainda não foi usada, devolve essa cor
                if (!validColor.contains(corStr)) {
                    return corStr;
                }
            }
            // Se não houver mais cores disponíveis
            return null;
        }

        // Se a cor não existir no enum Color, é inválida
        if (!validColors(color)) {
            return null;
        }

        // Normaliza a cor,primeira letra maiúscula, resto minúsculas
        color = color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase();

        // Se não conseguir adicionar ao set (já existia), é inválida
        if (!validColor.add(color)) {
            return null;
        }
        return color;
    }


    // Valida cada linha de informação de jogador e cria a lista de Player
    // Verifica: formato de cada linha
    //id, nome e linguagem válidos e não repetidos
    //cor válida (ou RANDOM) e não repetida
    private List<Player> validateAndCreatePlayersByLine(String[][] playerInfo) {
        List<Player> validPlayers = new ArrayList<>();
        HashSet<String> usedIds = new HashSet<>();
        HashSet<String> usedNames = new HashSet<>();
        HashSet<String> usedColors = new HashSet<>();

        // Percorre todas as linhas de jogadores
        for (int i = 0; i < playerInfo.length; i++) {
            String[] playerLine = playerInfo[i];

            // Cada linha tem de ter entre 2 e 4 campos
            if (playerLine == null || playerLine.length < 2 || playerLine.length > 4) {
                return null;
            }

            String idStr = playerLine[0].trim();
            String nameStr = playerLine[1].trim();
            String langStr = playerLine[2].trim();
            String colorStr = "RANDOM"; // valor por omissão
            if (playerLine.length == 4) {
                colorStr = playerLine[3].trim();
            }

            // Validação de ID
            Integer id = Player.isValidId(idStr);
            if (id == null || usedIds.contains(idStr)) {
                return null;
            }

            // Validação de Nome
            String name = Player.isValidName(nameStr);
            if (name == null || usedNames.contains(nameStr)) {
                return null;
            }

            // Validação de Linguagem
            String language = Player.isValidLanguage(langStr);
            if (language == null) {
                return null;
            }

            // Validação de Cor
            String color = validateColor(colorStr, usedColors);
            if (color == null) {
                return null;
            }

            // Se tudo estiver válido, cria o jogador
            validPlayers.add(new Player(id, name, language, color));

            // Marca ID, nome e cor como usados
            usedIds.add(idStr);
            usedNames.add(name);
            usedColors.add(color);
        }

        return validPlayers;
    }


    // Cria o tabuleiro inicial apenas com jogadores,sem abismos nem ferramentas
    // Valida número de jogadores, tamanho do mundo e dados de cada jogador
    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        // Número de jogadores inválido
        if (!nrValidPlayers(playerInfo)) {
            return false;
        }

        // Tamanho do mundo tem de ser suficiente para o dobro do número de jogadores
        if (worldSize < playerInfo.length * 2) {
            return false;
        }

        // Valida e cria os jogadores
        List<Player> validPlayers = validateAndCreatePlayersByLine(playerInfo);
        if (validPlayers == null) {
            return false;
        }

        // Cria o tabuleiro
        board = new Board(validPlayers, worldSize);
        turnCount = 1;
        // O primeiro a jogar é o jogador com ID mais baixo
        currentPlayerId = findLowestPlayerId(validPlayers);

        return true;
    }


    // Cria o tabuleiro inicial com jogadores e também com abismos e ferramentas
    //Se o array de abismos/ferramentas for null
    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {
        // Caso não haja definição de abismos/ferramentas, usa só jogadores
        if (abyssesAndTools == null) {
            return createInitialBoard(playerInfo, worldSize);
        }

        // Validação das linhas de abismos/ferramentas
        for (String[] line : abyssesAndTools) {
            if (line == null || line.length != 3) {
                return false;
            }

            String type = line[0].trim();          // 0 = abismo, 1 = ferramenta
            String subTypeStr = line[1].trim();    // subtipo do abismo/ferramenta
            String boardPositionStr = line[2].trim(); // posição no tabuleiro

            // type só pode ser "0" ou "1"
            if (!"0".equals(type) && !"1".equals(type)) {
                return false;
            }

            int subType;
            int boardPosition;

            try {
                subType = Integer.parseInt(subTypeStr);
                boardPosition = Integer.parseInt(boardPositionStr);
            } catch (NumberFormatException e) {
                // Se não forem números válidos
                return false;
            }

            // Validações específicas do subtipo para cada tipo
            if (type.equals("0")) { // abismo
                if (subType < 0 || subType > 9) {
                    return false;
                }
            }

            if (type.equals("1")) { // ferramenta
                if (subType < 0 || subType > 5) {
                    return false;
                }
            }

            // Posição no tabuleiro tem de ser válida
            if (boardPosition < 1 || boardPosition > worldSize) {
                return false;
            }
        }

        // Cria primeiro o tabuleiro apenas com jogadores
        if (!createInitialBoard(playerInfo, worldSize)) {
            return false;
        }

        // Depois adiciona os eventos (abismos e ferramentas)
        try {
            board.addEventsToSlot(abyssesAndTools);
        } catch (IllegalArgumentException e) {
            // Se o board detectar algum problema, retorna false
            return false;
        }

        return true;
    }

    //Devolve o ID mais baixo de uma lista de jogadores.
    //Usado para decidir quem começa a jogar.
    private int findLowestPlayerId(List<Player> validPlayers) {
        int lowerId = validPlayers.get(0).getId();
        for (Player player : validPlayers) {
            if (player.getId() < lowerId) {
                lowerId = player.getId();
            }
        }
        return lowerId;
    }


    // Devolve o caminho da imagem PNG associada a uma casa do tabuleiro
    public String getImagePng(int nrSquare) {
        return board.getImagePng(nrSquare);
    }

    /*
      Devolve informação detalhada de um programador com um determinado ID.
      Formato do array de retorno (7 posições):
      [0] id
      [1] nome
      [2] linguagem
      [3] cor
      [4] posição no tabuleiro
      [5] ferramentas (ordenadas por nome, separadas por ';') ou "No tools"
      [6] estado (Derrotado / Preso / Em jogo)
     */
    public String[] getProgrammerInfo(int id) {
        if (board == null || id < 1) {
            return null;
        }

        // Encontra o slot onde está o jogador
        Slot slot = board.getSlotOfPlayer(id);
        if (slot == null) {
            return null;
        }

        // Encontra o jogador pelo ID dentro desse slot
        Player player = slot.findPlayerByID(id);

        String[] infoPlayer = new String[7];
        infoPlayer[0] = String.valueOf(player.getId());
        infoPlayer[1] = player.getName();
        infoPlayer[2] = player.getLanguage();
        infoPlayer[3] = player.getColor();
        infoPlayer[4] = String.valueOf(slot.getNrSlot());

        // Trata da informação das ferramentas
        List<Tool> tools = player.getTools();
        if (tools == null || tools.isEmpty()) {
            infoPlayer[5] = "No tools";
        } else {
            List<String> toolNames = new ArrayList<>();
            for (Tool t : tools) {
                toolNames.add(t.getName());
            }
            // Ordena alfabeticamente os nomes das ferramentas
            Collections.sort(toolNames);
            infoPlayer[5] = String.join(";", toolNames);
        }

        // Converte o estado do jogador para string
        if (player.getState() == PlayerState.DERROTADO) {
            infoPlayer[6] = "Derrotado";
        } else if (player.getState() == PlayerState.PRESO) {
            infoPlayer[6] = "Preso";
        } else {
            infoPlayer[6] = "Em jogo";
        }

        return infoPlayer;
    }

    // Devolve a informação do programador num formato de string formatado
    // Formato: id | nome | posição | ferramentas | linguagens | estado
    public String getProgrammerInfoAsStr(int id) {
        if (board == null || id < 1) {
            return null;
        }

        Slot slot = board.getSlotOfPlayer(id);
        if (slot == null) {
            return null;
        }

        Player player = slot.findPlayerByID(id);

        // Linguagens ordenadas
        List<String> sortedLanguages = player.getSortedLanguages(player.getLanguage());
        String languagesInfo = String.join("; ", sortedLanguages);

        // Ferramentas do jogador
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

        // Estado do jogador em texto
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

        // String final formatada
        return id + " | " + player.getName() + " | " + slot.getNrSlot() + " | " + toolsStr + " | " +
                languagesInfo + " | " + playerState;
    }

    // Devolve informação dos programadores em jogo (não derrotados),
    // no formato: "Nome1 : ferramentas | Nome2 : ferramentas | ..."
    public String getProgrammersInfo() {
        if (board == null) {
            return "";
        }

        List<Player> alivePlayers = new ArrayList<>();

        // Filtra apenas jogadores não derrotados
        for (Player player : board.getPlayers()) {
            if (player.getState() != PlayerState.DERROTADO) {
                alivePlayers.add(player);
            }
        }

        if (alivePlayers.isEmpty()) {
            return "";
        }

        // Ordena por ID (crescente)
        alivePlayers.sort(Comparator.comparingInt(Player::getId));

        StringBuilder sb = new StringBuilder();
        int playerNr = 0;

        // Constrói a string para cada jogador
        for (Player player : alivePlayers) {
            sb.append(player.getName()).append(" : ");

            List<Tool> tools = player.getTools();
            if (tools == null || tools.isEmpty()) {
                sb.append("No tools");
            } else {
                List<String> toolNames = new ArrayList<>();
                for (Tool t : tools) {
                    toolNames.add(t.getName());
                }
                Collections.sort(toolNames);

                sb.append(String.join(";", toolNames));
            }

            playerNr++;
            if (playerNr != alivePlayers.size()) {
                sb.append(" | ");
            }
        }
        return sb.toString();
    }

    // Devolve informação sobre uma casa (slot) específica do tabuleiro.
    // O detalhe depende da implementação em Board.getSlotInfo().
    public String[] getSlotInfo(int position) {
        return board.getSlotInfo(position);
    }

    //Devolve o ID do jogador que tem o turno atual.
    public int getCurrentPlayerID() {
        return currentPlayerId;
    }


    // Passa o turno para o próximo jogador ainda em jogo (não derrotado).
    // Ordena os jogadores por ID e encontra o seguinte na lista circular.
    // Também incrementa o número de turnos e repõe o estado de "último movimento válido".
    private void passTurnToNextPlayer() {
        // Cria uma cópia da lista de jogadores
        List<Player> sortedPlayers = new ArrayList<>(board.getPlayers());
        // Ordena por ID
        sortedPlayers.sort(Comparator.comparingInt(Player::getId));

        int currentIndex = -1;
        // Encontra o índice do jogador atual na lista ordenada
        for (int i = 0; i < sortedPlayers.size(); i++) {
            if (sortedPlayers.get(i).getId() == currentPlayerId) {
                currentIndex = i;
                break;
            }
        }

        // Se por algum motivo não encontrar, não faz nada
        if (currentIndex == -1) {
            return;
        }

        // Procura o próximo jogador não derrotado numa forma circular
        for (int i = 1; i <= sortedPlayers.size(); i++) {
            int nextIndex = (currentIndex + i) % sortedPlayers.size();
            Player nextPlayer = sortedPlayers.get(nextIndex);

            if (nextPlayer.getState() != PlayerState.DERROTADO) {
                // Atualiza o jogador atual para o próximo encontrado
                currentPlayerId = nextPlayer.getId();
                // Incrementa o número de turnos
                turnCount++;

                // Marca o último movimento de todos como válido (reset)
                for (Player player : board.getPlayers()) {
                    player.setLastMoveIsValid(true);
                }
                return;
            }
        }
        // Se não encontrar ninguém (caso extremo), apenas incrementa o turno
        turnCount++;
    }

    /*
      Move o jogador atual um certo número de casas (1 a 6).
      Regras de validação:jogo tem de existir,valor do dado entre 1 e 6,jogo não pode já ter acabado,jogador
      não pode estar preso,verifica se o jogador pode mover esse número de casas
     */
    public boolean moveCurrentPlayer(int nrSpaces) {
       /* if (board == null || nrSpaces < 1 || nrSpaces > 6 || gameIsOver()) {
            return false;
        }

        // Encontra o jogador atual pelo ID
        Player currentPlayer = board.getSlotOfPlayer(getCurrentPlayerID()).findPlayerByID(getCurrentPlayerID());
        if (currentPlayer == null) {
            return false;
        }

        // Se o jogador estiver preso, não se pode mover
        if (currentPlayer.getState() == PlayerState.PRESO) {
            return false;
        }

        // Verifica se o movimento é permitido pelo jogador (regras internas da classe Player)
        if (!currentPlayer.canMove(nrSpaces)) {
            currentPlayer.setLastMoveIsValid(false);
            return false;
        }

        // Marca movimento válido e guarda o valor do dado
        currentPlayer.setLastMoveIsValid(true);
        currentPlayer.setLastDiceValue(nrSpaces);

        // Move o jogador no tabuleiro
        boolean moved = board.movePlayer(currentPlayer, nrSpaces);

        // Se o movimento terminou o jogo, garante que o currentPlayerId fica no vencedor
        if (moved && gameIsOver()) {
            currentPlayerId = currentPlayer.getId();
        }

        return moved;*/
        return false;
    }

    /*
      Faz a reação do jogador atual ao abismo ou ferramenta (se existir) na casa onde ele está.
      Também trata de passar o turno para o próximo jogador.
     Regras especiais: se o jogador estiver preso, apenas passa o turno e devolve mensagem de ciclo infinito.
     */
    public String reactToAbyssOrTool() {
        if (board == null || currentPlayerId == -1) {
            return null;
        }

        // Encontra o slot onde está o jogador atual
        Slot currentSlot = board.getSlotOfPlayer(currentPlayerId);
        if (currentSlot == null) {
            return null;
        }

        // Encontra o jogador nesse slot
        Player currentPlayer = currentSlot.findPlayerByID(currentPlayerId);

        // Se estiver preso, passa já o turno e avisa
        if (currentPlayer.getState() == PlayerState.PRESO) {
            passTurnToNextPlayer();
            return "Ciclo Infinito! O jogador ficou preso na casa.";
        }

        // Vai buscar o evento (abismo ou ferramenta) da casa
        Event event = currentSlot.getEvent();
        String message = null;

        // Se existir evento, aplica interação do jogador com o evento
        if (event != null) {
            message = event.playerInteraction(currentPlayer, board);
        }

        // Depois da interação, passa o turno
        passTurnToNextPlayer();
        // Devolve a mensagem gerada pelo evento (se houver)
        return message;
    }

    public boolean gameIsOver() {
        if (board == null) {
            return false;
        }
        return board.hasPlayerOnLastSlot();
    }

    //Encontra o slot onde está o vencedor (implementação delegada no Board).
    private Slot findWinner() {
        return board.findWinner();
    }

    // Devolve a lista de nomes dos restantes jogadores (não vencedores),
    //possivelmente ordenados ou com algum critério (implementado em Board)
    private ArrayList<String> findLastPlayers(String winnerName) {
        return board.findLastPlayers(winnerName);
    }

    //Devolve os resultados finais do jogo numa lista de strings.
    // A lista tem o título, número de turnos, vencedor e restantes jogadores
    public ArrayList<String> getGameResults() {
        ArrayList<String> results = new ArrayList<>();

        if (board == null) {
            return results;
        }

        Slot winnerSlot = findWinner();

        // Se por algum motivo não houver vencedor, retorna lista vazia
        if (winnerSlot == null || winnerSlot.getPlayers().isEmpty()) {
            return results;
        }

        // Primeiro jogador na casa vencedora é o vencedor
        String winnerName = winnerSlot.getPlayers().get(0).getName();
        ArrayList<String> lastPlayers = findLastPlayers(winnerName);

        // Constrói o relatório dos resultados
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
