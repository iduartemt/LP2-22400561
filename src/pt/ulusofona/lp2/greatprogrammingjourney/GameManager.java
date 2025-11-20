package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.io.*;
import java.util.*;

enum Color {
    BROWN,
    PURPLE,
    GREEN,
    BLUE
}

enum EventType {
    ABYSS,
    TOOL
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
            Player player = slot.findPlayerByID(id);
            if (player != null) {
                List<String> sortedLanguages = player.getSortedLanguages(player.language);
                StringBuilder languagesInfo = player.playerLanguageInfo(sortedLanguages);

                return id + " | " + player.name + " | " +
                        slot.nrSlot + " | " + languagesInfo + " | Em Jogo";
            }
        }

        return null;
    }

    public String getProgrammersInfo() {
        if (board == null) {
            return "";
        }

        List<Player> alivePlayers = new ArrayList<>();

        for (Slot s : board.slots) {
            for (Player p : s.players) {
                if (p.isAlive && !alivePlayers.contains(p)) {
                    alivePlayers.add(p);
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < alivePlayers.size(); i++) {
            Player p = alivePlayers.get(i);
            if (i > 0) {
                sb.append(" | ");
            }

            sb.append(p.getName()).append(" : ");

            String tools = p.getTools().toString();

            if (tools == null || tools.isEmpty()) {
                sb.append("No tools");
            } else {
                sb.append((tools));
            }
        }
        return sb.toString();
    }

    // Retorna os IDs dos jogadores presentes numa determinada slot
    public String[] getSlotInfo(int position) {
        Slot slot = board.encontraSlot(position);
        if (slot == null) {
            return null;
        }
        return new String[]{slot.buildPlayerIds(), "", ""};
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
        for (Slot s : board.slots) {
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
        int currentIndex = findAtualPlayerIndex(allPlayers);

        if (currentIndex == -1) {
            return false;
        }

        // calcula o próximo jogador
        int nextIndex = (currentIndex + 1) % allPlayers.size();
        currentPlayerId = allPlayers.get(nextIndex).id;
        return true;
    }

    public String reactToAbyssOrTool() {

        Player currentPlayer = null;
        Slot currentSlot = null;
        boolean foundPlayer = false;

        for (Slot s : board.slots) {
            Player p = s.findPlayerByID(getCurrentPlayerID());
            if (p != null) {

                currentPlayer = p;
                currentSlot = s;
                foundPlayer = true;
                break;
            }
        }

        if (!foundPlayer) {
            return null;
        }

        Event event = currentSlot.getEvent();

        if (event != null) {

            if (event.isTool()) {

                currentPlayer.tools.add(event);
                currentSlot.removeEvent();
                return "Recolheu a ferramenta " + event.subtype;
            } else if (event.isAbyss()) {

                boolean hasTool = false;
                Event fixTool = null;

                for (Event tool : currentPlayer.tools) {
                    if (tool.subtype.equals(event.subtype)) {

                        hasTool = true;
                        fixTool = tool;
                        break;
                    }
                }

                if (hasTool) {
                    currentSlot.removeEvent();
                    return "Abismo resolvido com a ferramenta " + fixTool.subtype;
                }

                currentPlayer.isAlive = false;
                currentSlot.removePlayer(currentPlayer);
                gameIsOver();
                return "Derrotado pelo abismo " + event.subtype;
            }
        }
        return null;
    }

    private int findAtualPlayerIndex(List<Player> allPlayers) {
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
        return board.hasPlayerOnLastSlot();
    }

    private Slot findWinner() {
        if (board == null) {
            return null;
        }

        for (Slot slot : board.slots) {
            if (slot.nrSlot == board.getNrTotalSlots()) {
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
            Slot slot = board.slots.get(i);
            for (Player player : slot.players) {
                if (!player.name.equals(winnerName)) {
                    lastPlayers.add(player.name + " " + slot.nrSlot);
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

        if (winnerSlot == null || winnerSlot.players.isEmpty()) {
            return results;
        }

        // nome do vencedor
        String winnerName = winnerSlot.players.get(0).name;
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

    public void loadGame(File file) throws InvalidFileException, FileNotFoundException {

        // 1) Validações básicas do ficheiro
        if (file == null) {
            throw new InvalidFileException();
        }
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!file.canRead()) {
            throw new InvalidFileException();
        }

        // 2) Ler todas as linhas do ficheiro
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) { // ignorar linhas vazias
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new InvalidFileException();
        }

        if (lines.size() < 4) { // claramente não chega para nada
            throw new InvalidFileException();
        }

        int index = 0;

        // 3) Ler worldSize
        int worldSize;
        try {
            worldSize = Integer.parseInt(lines.get(index++));
        } catch (NumberFormatException e) {
            throw new InvalidFileException();
        }

        if (worldSize < 2) {
            throw new InvalidFileException();
        }

        // 4) Ler número de jogadores
        int nrPlayers;
        try {
            nrPlayers = Integer.parseInt(lines.get(index++));
        } catch (NumberFormatException e) {
            throw new InvalidFileException();
        }

        if (nrPlayers < 2 || nrPlayers > 4) {
            throw new InvalidFileException();
        }

        if (lines.size() < index + nrPlayers) {
            throw new InvalidFileException();
        }

        // Arrays para criar o tabuleiro inicial
        String[][] playerInfo = new String[nrPlayers][4];
        int[] playerPositions = new int[nrPlayers];
        String[] playerStates = new String[nrPlayers];

        // 5) Ler linhas de jogadores
        for (int i = 0; i < nrPlayers; i++) {
            String playerLine = lines.get(index++);
            String[] parts = playerLine.split(";", -1);

            // Esperamos: id;nome;linguagem;cor;posicao;estado
            if (parts.length < 6) {
                throw new InvalidFileException();
            }

            String idStr = parts[0].trim();
            String name = parts[1].trim();
            String language = parts[2].trim();
            String color = parts[3].trim();
            String posStr = parts[4].trim();
            String state = parts[5].trim();

            int position;
            try {
                position = Integer.parseInt(posStr);
            } catch (NumberFormatException e) {
                throw new InvalidFileException();
            }

            if (position < 1 || position > worldSize) {
                throw new InvalidFileException();
            }

            playerInfo[i][0] = idStr;
            playerInfo[i][1] = name;
            playerInfo[i][2] = language;
            playerInfo[i][3] = color;

            playerPositions[i] = position;
            playerStates[i] = state;
        }

        // 6) Ler número de eventos
        if (lines.size() <= index) {
            throw new InvalidFileException();
        }

        int nrEvents;
        try {
            nrEvents = Integer.parseInt(lines.get(index++));
        } catch (NumberFormatException e) {
            throw new InvalidFileException();
        }

        if (nrEvents < 0) {
            throw new InvalidFileException();
        }

        if (lines.size() < index + nrEvents + 2) { // +2: currentPlayerId e turnCount
            throw new InvalidFileException();
        }

        String[][] abyssesAndTools = new String[nrEvents][3];

        // 7) Ler eventos: tipo;subtipo;posicao
        for (int i = 0; i < nrEvents; i++) {
            String eventLine = lines.get(index++);
            String[] parts = eventLine.split(";", -1);

            if (parts.length < 3) {
                throw new InvalidFileException();
            }

            String typeStr = parts[0].trim();
            String subTypeStr = parts[1].trim();
            String posStr = parts[2].trim();

            if (!"0".equals(typeStr) && !"1".equals(typeStr)) {
                throw new InvalidFileException();
            }

            int pos;
            int subType;
            try {
                subType = Integer.parseInt(subTypeStr);
                pos = Integer.parseInt(posStr);
            } catch (NumberFormatException e) {
                throw new InvalidFileException();
            }

            if (pos < 1 || pos > worldSize) {
                throw new InvalidFileException();
            }

            if (typeStr.equals("0")) { // abismo
                if (subType < 0 || subType > 9) {
                    throw new InvalidFileException();
                }
            } else { // ferramenta
                if (subType < 0 || subType > 5) {
                    throw new InvalidFileException();
                }
            }

            abyssesAndTools[i][0] = typeStr;
            abyssesAndTools[i][1] = subTypeStr;
            abyssesAndTools[i][2] = posStr;
        }

        // 8) Ler currentPlayerId
        int loadedCurrentPlayerId;
        try {
            loadedCurrentPlayerId = Integer.parseInt(lines.get(index++));
        } catch (NumberFormatException e) {
            throw new InvalidFileException();
        }

        // 9) Ler turnCount
        int loadedTurnCount;
        try {
            loadedTurnCount = Integer.parseInt(lines.get(index++));
        } catch (NumberFormatException e) {
            throw new InvalidFileException();
        }

        // 10) Criar o tabuleiro base com jogadores e eventos
        boolean created = createInitialBoard(playerInfo, worldSize, abyssesAndTools);
        if (!created || board == null) {
            throw new InvalidFileException();
        }

        // 11) Reposicionar jogadores e restaurar estados
        // Construir mapa id -> Player
        Map<Integer, Player> playersById = new HashMap<>();
        for (Slot slot : board.slots) {
            for (Player p : slot.players) {
                playersById.put(p.id, p);
            }
        }

        for (int i = 0; i < nrPlayers; i++) {
            int id;
            try {
                id = Integer.parseInt(playerInfo[i][0]);
            } catch (NumberFormatException e) {
                throw new InvalidFileException();
            }

            Player p = playersById.get(id);
            if (p == null) {
                throw new InvalidFileException();
            }

            String state = playerStates[i];
            int desiredPos = playerPositions[i];

            // Encontrar slot atual deste jogador (a partir da posição inicial)
            Slot origin = null;
            for (Slot s : board.slots) {
                if (s.players.contains(p)) {
                    origin = s;
                    break;
                }
            }

            if (!"Derrotado".equalsIgnoreCase(state)) {
                // Jogador vivo/em jogo
                p.isAlive = true;

                // Mover para a posição correta, se necessário
                if (origin != null && origin.nrSlot != desiredPos) {
                    origin.removePlayer(p);
                    Slot destination = board.encontraSlot(desiredPos);
                    if (destination == null) {
                        throw new InvalidFileException();
                    }
                    destination.addPlayer(p);
                } else if (origin == null) {
                    // Não encontrado mas deveria estar em jogo
                    Slot destination = board.encontraSlot(desiredPos);
                    if (destination == null) {
                        throw new InvalidFileException();
                    }
                    destination.addPlayer(p);
                }
            } else {
                // Jogador derrotado
                p.isAlive = false;
                // Remover de qualquer slot onde esteja
                if (origin != null) {
                    origin.removePlayer(p);
                }
            }
        }

        // 12) Restaurar jogador atual e número de turnos
        if (!playersById.containsKey(loadedCurrentPlayerId)) {
            throw new InvalidFileException();
        }

        this.currentPlayerId = loadedCurrentPlayerId;
        this.turnCount = loadedTurnCount;
    }



// ...

    public void saveGame(File file) throws FileNotFoundException {
        if (board == null) {
            // Nada para guardar
            throw new IllegalStateException("Não existe jogo em curso para guardar.");
        }

        if (file == null) {
            throw new FileNotFoundException("Ficheiro inválido (null).");
        }

        // Recolher todos os jogadores (sem duplicados)
        List<Player> allPlayers = new ArrayList<>();
        for (Slot slot : board.slots) {
            for (Player p : slot.players) {
                if (!allPlayers.contains(p)) {
                    allPlayers.add(p);
                }
            }
        }

        // Nota: Pode haver jogadores "mortos" que já não estão em nenhuma slot.
        // Se guardas referências a esses jogadores noutro sítio, podes adicioná-los aqui.

        int worldSize = board.getNrTotalSlots();

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

            // 1) Tamanho do tabuleiro
            out.println(worldSize);

            // 2) Número de jogadores
            out.println(allPlayers.size());

            // 3) Linhas dos jogadores: id;nome;linguagem;cor;posicao;estado
            for (Player p : allPlayers) {

                // Encontrar posição atual do jogador no tabuleiro
                int position = 1; // valor por defeito
                boolean foundOnBoard = false;

                for (Slot slot : board.slots) {
                    if (slot.players.contains(p)) {
                        position = slot.nrSlot;
                        foundOnBoard = true;
                        break;
                    }
                }

                // Determinar estado textual
                String estado;
                if (!p.isAlive) {
                    estado = "Derrotado";
                    // Se o jogador estiver morto e não estiver em nenhuma casa,
                    // continuamos a guardar a posição "position" (por ex. 1).
                } else {
                    estado = "Em Jogo";
                }

                out.println(
                        p.id + ";" +
                                p.name + ";" +
                                p.language + ";" +
                                p.color + ";" +
                                position + ";" +
                                estado
                );
            }

            // 4) Eventos (abismos e ferramentas ainda no tabuleiro)
            // Vamos percorrer as slots e ver que eventos ainda lá estão
            List<String[]> eventos = new ArrayList<>();

            for (Slot slot : board.slots) {
                Event e = slot.getEvent();
                if (e != null) {
                    String tipo = e.isAbyss() ? "0" : "1";
                    // subtype foi originalmente um número (0..9 / 0..5)
                    String subtipo = String.valueOf(e.subtype);
                    String posicao = String.valueOf(slot.nrSlot);

                    eventos.add(new String[]{tipo, subtipo, posicao});
                }
            }

            // Número de eventos
            out.println(eventos.size());

            // Linhas dos eventos: tipo;subtipo;posicao
            for (String[] ev : eventos) {
                out.println(ev[0] + ";" + ev[1] + ";" + ev[2]);
            }

            // 5) ID do jogador atual
            out.println(currentPlayerId);

            // 6) Número de turnos
            out.println(turnCount);

        } catch (IOException e) {
            // Se der erro a escrever, aqui podes optar por lançar uma RuntimeException
            throw new RuntimeException("Erro ao guardar o jogo: " + e.getMessage(), e);
        }
    }


    public JPanel getAuthorsPanel() {
        return null;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }
}
