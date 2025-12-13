package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype.*;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype.Exception;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Board {

    // Lista com todas as casas do tabuleiro
    private final List<Slot> slots = new ArrayList<>();
    // Lista com todos os jogadores do jogo
    private final List<Player> players;

    // Construtor: cria o tabuleiro apenas com jogadores
    public Board(List<Player> players, int worldSize) {
        addSlotList(worldSize);            // Cria todas as casas do tabuleiro
        addPlayerSlotFirstSlot(players);   // Coloca os jogadores na primeira casa
        this.players = new ArrayList<>(players); // Guarda cópia dos jogadores
    }

    // Construtor: cria o tabuleiro com jogadores + abismos + ferramentas
    public Board(List<Player> players, int worldSize, String[][] abyssesAndTools) {
        addSlotList(worldSize);               // Cria as casas
        addEventsToSlot(abyssesAndTools);     // Adiciona os eventos às casas
        addPlayerSlotFirstSlot(players);      // Coloca jogadores na primeira casa
        this.players = new ArrayList<>(players);
    }

    // Devolve a lista de jogadores
    public List<Player> getPlayers() {
        return players;
    }
    // Cria todas as casas do tabuleiro de 1 até worldSize
    private void addSlotList(int worldSize) {
        for (int i = 0; i < worldSize; i++) {
            slots.add(new Slot(i + 1)); // i + 1 porque as casas começam em 1
        }
    }

    // Adiciona todos os jogadores na primeira casa
    private void addPlayerSlotFirstSlot(List<Player> players) {
        for (Player player : players) {
            slots.get(0).addPlayer(player); // Casa 1
        }
    }

    // Devolve o número total de casas do tabuleiro
    public int getNrTotalSlots() {
        return slots.size();
    }

    // Devolve o slot com o número indicado
    public Slot encontraSlot(int nrSlot) {
        for (Slot slot : slots) {
            if (slot.getNrSlot() == nrSlot) {
                return slot;
            }
        }
        return null; // Se não encontrar nenhum
    }

    // Devolve a lista completa de slots
    public List<Slot> getSlots() {
        return slots;
    }

    // Verifica se existe pelo menos um jogador na última casa
    public boolean hasPlayerOnLastSlot() {
        if (slots.isEmpty()) {
            return false;
        }
        int lastIndex = slots.size() - 1;
        return !slots.get(lastIndex).getPlayers().isEmpty();
    }

    // Adiciona todos os abismos e ferramentas às respetivas casas
    public void addEventsToSlot(String[][] abyssesAndTools) {
        if (abyssesAndTools == null) {
            return;
        }

        for (String[] line : abyssesAndTools) {

            String typeStr = line[0];     // Tipo: 0 = abismo | 1 = ferramenta
            String subTypeStr = line[1];  // Subtipo
            String position = line[2];    // Posição no tabuleiro

            int positionInt = Integer.parseInt(position);
            Slot slot = encontraSlot(positionInt);

            // Caso seja um abismo
            if (typeStr.equals("0")) {
                switch (subTypeStr) {
                    case "0" -> slot.addEvent(new SyntaxError(positionInt));
                    case "1" -> slot.addEvent(new LogicError(positionInt));
                    case "2" -> slot.addEvent(new Exception(positionInt));
                    case "3" -> slot.addEvent(new FileNotFound(positionInt));
                    case "4" -> slot.addEvent(new Crash(positionInt));
                    case "5" -> slot.addEvent(new CodeDuplication(positionInt));
                    case "6" -> slot.addEvent(new SecundaryEffects(positionInt));
                    case "7" -> slot.addEvent(new BlueScreenOfDeath(positionInt));
                    case "8" -> slot.addEvent(new InfiniteLoop(positionInt));
                    case "9" -> slot.addEvent(new SegmentationFault(positionInt));
                }

                // Caso seja uma ferramenta
            } else if (typeStr.equals("1")) {
                Tool tool;
                switch (subTypeStr) {
                    case "0" -> tool = new Inheritance(positionInt);
                    case "1" -> tool = new FunctionalProgramming(positionInt);
                    case "2" -> tool = new UnitTests(positionInt);
                    case "3" -> tool = new ExceptionHandling(positionInt);
                    case "4" -> tool = new Ide(positionInt);
                    case "5" -> tool = new TeacherHelp(positionInt);
                    default -> throw new IllegalArgumentException("Tipo de ferramenta invalida");
                }

                slot.addEvent(tool);               // Coloca a ferramenta na casa

            } else {
                throw new IllegalArgumentException(); // Tipo inválido
            }
        }
    }

    // Devolve o slot onde se encontra um jogador com determinado ID
    public Slot getSlotOfPlayer(int playerId) {
        for (Slot slot : slots) {
            if (slot.findPlayerByID(playerId) != null) {
                return slot;
            }
        }
        return null;
    }

    // Devolve a imagem associada a uma casa
    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > getNrTotalSlots()) {
            return null;
        }

        // Se for a última casa, mostra a imagem de vitória
        if (nrSquare == getNrTotalSlots()) {
            return "glory.png";
        }

        Slot thisSlot = encontraSlot(nrSquare);

        // Se existir evento, devolve a imagem do evento
        if (thisSlot.getEvent() != null) {
            return thisSlot.getEvent().getImage();
        }
        return null;
    }

    // Devolve informação sobre uma casa específica
    public String[] getSlotInfo(int position) {
        Slot slot = encontraSlot(position);
        if (slot == null) {
            return null;
        }

        String playersStr = slot.buildPlayerIds(); // IDs dos jogadores na casa
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

    // Devolve o slot da última casa (onde está o vencedor)
    public Slot findWinner() {
        for (Slot slot : slots) {
            if (slot.getNrSlot() == getNrTotalSlots()) {
                return slot;
            }
        }
        return null;
    }

    // Devolve os jogadores restantes menos o vencedor
    public ArrayList<String> findLastPlayers(String winnerName) {
        ArrayList<String> lastPlayers = new ArrayList<>();

        // Percorre do fim para o início
        for (int i = getNrTotalSlots() - 1; i >= 0; i--) {
            Slot slot = getSlots().get(i);

            List<Player> sortedPlayers = new ArrayList<>(slot.getPlayers());

            // Ordena os jogadores por nome
            sortedPlayers.sort(Comparator.comparing(Player::getName));

            for (Player player : sortedPlayers) {
                if (!player.getName().equals(winnerName)) {
                    lastPlayers.add(player.getName() + " " + slot.getNrSlot());
                }
            }
        }
        return lastPlayers;
    }

    // Move um jogador no tabuleiro
    public boolean movePlayer(Player player, int nrSpaces) {

        // Procura a casa atual do jogador
        Slot originSlot = getSlotOfPlayer(player.getId());
        if (originSlot == null) {
            return false;
        }

        // Atualiza o histórico de posições do jogador
        player.setPositionTwoMovesAgo(player.getPreviousPosition());
        player.setPreviousPosition(originSlot.getNrSlot());

        int lastSlot = getNrTotalSlots();
        int destination = originSlot.getNrSlot() + nrSpaces;

        // Regra do ricochete (volta para trás se ultrapassar o fim)
        if (destination > lastSlot) {
            int tillTheEnd = lastSlot - originSlot.getNrSlot();
            int exceed = nrSpaces - tillTheEnd;
            destination = lastSlot - exceed;
        }

        Slot destinationSlot = encontraSlot(destination);
        if (destinationSlot == null) {
            return false;
        }

        // Remove o jogador da casa original e adiciona na nova casa
        originSlot.removePlayer(player);
        destinationSlot.addPlayer(player);
        return true;
    }

}
