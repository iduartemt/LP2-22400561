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
            for (Player p : slot.players) {
                if (p.isAlive && !allPlayers.contains(p)) {
                    allPlayers.add(p);
                }
            }
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

        validateFile(file);

        List<String> lines = loadLines(file);
        int index = 0;

        int worldSize = parseWorldSize(lines, index++);
        int nrPlayers = parseNrPlayers(lines, index++);

        PlayerData pd = parsePlayers(lines, index, nrPlayers, worldSize);
        index += nrPlayers;

        EventData ed = parseEvents(lines, index, worldSize);
        index += ed.nrEvents + 1;

        int current = parseCurrentPlayerId(lines, index++);
        int turns = parseTurnCount(lines, index++);

        rebuildGame(worldSize, pd, ed, current, turns);
    }


    private void validateFile(File file) throws InvalidFileException, FileNotFoundException {
        if (file == null) throw new InvalidFileException();
        if (!file.exists()) throw new FileNotFoundException();
        if (!file.canRead()) throw new InvalidFileException();
    }

    private List<String> loadLines(File file) throws InvalidFileException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) lines.add(line);
            }
        } catch (IOException e) {
            throw new InvalidFileException();
        }
        return lines;
    }

    private int parseWorldSize(List<String> lines, int index) throws InvalidFileException {
        try {
            int ws = Integer.parseInt(lines.get(index));
            if (ws < 2) throw new InvalidFileException();
            return ws;
        } catch (Exception e) {
            throw new InvalidFileException();
        }
    }

    private int parseNrPlayers(List<String> lines, int index) throws InvalidFileException {
        try {
            int n = Integer.parseInt(lines.get(index));
            if (n < 2 || n > 4) throw new InvalidFileException();
            return n;
        } catch (Exception e) {
            throw new InvalidFileException();
        }
    }

    private static class PlayerData {
        String[][] info;
        int[] positions;
        String[] states;

        PlayerData(String[][] i, int[] p, String[] s) {
            info = i;
            positions = p;
            states = s;
        }
    }

    private PlayerData parsePlayers(List<String> lines, int index, int nrPlayers, int worldSize)
            throws InvalidFileException {

        String[][] info = new String[nrPlayers][4];
        int[] pos = new int[nrPlayers];
        String[] states = new String[nrPlayers];

        for (int i = 0; i < nrPlayers; i++) {

            String[] parts = lines.get(index + i).split(";", -1);
            if (parts.length < 6) throw new InvalidFileException();

            info[i][0] = parts[0].trim();
            info[i][1] = parts[1].trim();
            info[i][2] = parts[2].trim();
            info[i][3] = parts[3].trim();

            try {
                pos[i] = Integer.parseInt(parts[4].trim());
            } catch (Exception e) {
                throw new InvalidFileException();
            }

            if (pos[i] < 1 || pos[i] > worldSize)
                throw new InvalidFileException();

            states[i] = parts[5].trim();
        }

        return new PlayerData(info, pos, states);
    }

    private static class EventData {
        int nrEvents;
        String[][] events;

        EventData(int n, String[][] e) {
            nrEvents = n;
            events = e;
        }
    }

    private EventData parseEvents(List<String> lines, int index, int worldSize)
            throws InvalidFileException {

        int nrEvents;
        try {
            nrEvents = Integer.parseInt(lines.get(index));
        } catch (Exception e) {
            throw new InvalidFileException();
        }

        String[][] arr = new String[nrEvents][3];

        for (int i = 0; i < nrEvents; i++) {
            String[] parts = lines.get(index + 1 + i).split(";", -1);
            if (parts.length < 3) throw new InvalidFileException();

            String typeStr = parts[0].trim();
            String stStr = parts[1].trim();
            String posStr = parts[2].trim();

            if (!typeStr.equals("0") && !typeStr.equals("1"))
                throw new InvalidFileException();

            int pos, subtype;
            try {
                subtype = Integer.parseInt(stStr);
                pos = Integer.parseInt(posStr);
            } catch (Exception e) {
                throw new InvalidFileException();
            }

            if (pos < 1 || pos > worldSize) throw new InvalidFileException();

            if (typeStr.equals("0") && (subtype < 0 || subtype > 9))
                throw new InvalidFileException();

            if (typeStr.equals("1") && (subtype < 0 || subtype > 5))
                throw new InvalidFileException();

            arr[i][0] = typeStr;
            arr[i][1] = stStr;
            arr[i][2] = posStr;
        }

        return new EventData(nrEvents, arr);
    }

    private int parseCurrentPlayerId(List<String> lines, int index) throws InvalidFileException {
        try {
            return Integer.parseInt(lines.get(index));
        } catch (Exception e) {
            throw new InvalidFileException();
        }
    }

    private int parseTurnCount(List<String> lines, int index) throws InvalidFileException {
        try {
            return Integer.parseInt(lines.get(index));
        } catch (Exception e) {
            throw new InvalidFileException();
        }
    }

    private void rebuildGame(int worldSize, PlayerData pd, EventData ed, int currentPlayerId, int turnCount) throws InvalidFileException {
        // cria o tabuleiro base com os eventos
        if (!createInitialBoard(pd.info, worldSize, ed.events))
            throw new InvalidFileException();

        // coloca jogadores nas posições corretas
        Map<Integer, Player> map = new HashMap<>();

        for (Slot s : board.slots)
            for (Player p : s.players)
                map.put(p.id, p);

        for (int i = 0; i < pd.info.length; i++) {

            int id = Integer.parseInt(pd.info[i][0]);
            Player p = map.get(id);

            if (p == null) throw new InvalidFileException();

            // origem
            Slot origin = null;
            for (Slot s : board.slots) {
                if (s.players.contains(p)) {
                    origin = s;
                    break;
                }
            }

            if (pd.states[i].equalsIgnoreCase("Derrotado")) {
                p.isAlive = false;
                if (origin != null) origin.removePlayer(p);
                continue;
            }

            // mover jogador
            int desired = pd.positions[i];

            if (origin != null && origin.nrSlot != desired) {
                origin.removePlayer(p);
                board.encontraSlot(desired).addPlayer(p);
            }
        }

        this.currentPlayerId = currentPlayerId;
        this.turnCount = turnCount;
    }


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
