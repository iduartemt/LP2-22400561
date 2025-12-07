package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype.*;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype.Exception;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board {
    private final List<Slot> slots = new ArrayList<>();
    private final HashMap<String, Tool> tools = new HashMap<>();
    private final List<Player> players;

    public Board(List<Player> players, int worldSize) {
        addSlotList(worldSize);
        addPlayerSlotFirstSlot(players);
        this.players = players;
    }

    public Board(List<Player> players, int worldSize, String[][] abyssesAndTools) {
        addSlotList(worldSize);
        addEventsToSlot(abyssesAndTools);
        addPlayerSlotFirstSlot(players);
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Tool> getTools() {
        return (List<Tool>) tools.values();
    }

    public HashMap<String, Tool> getToolsHashMap() {
        return tools;
    }

    //adiciona cada slot a lista de slots
    private void addSlotList(int worldSize) {
        for (int i = 0; i < worldSize; i++) {
            slots.add(new Slot(i + 1));
        }
    }

    //Adiciona jogadores na primeira casa
    private void addPlayerSlotFirstSlot(List<Player> players) {
        for (Player player : players) {
            slots.get(0).addPlayer(player);
        }
    }

    public int getNrTotalSlots() {
        return slots.size();
    }

    public Slot encontraSlot(int nrSlot) {
        for (Slot slot : slots) {
            if (slot.getNrSlot() == nrSlot) {
                return slot;
            }
        }
        return null;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public boolean hasPlayerOnLastSlot() {
        if (slots.isEmpty()) {
            return false;
        }
        int lastIndex = slots.size() - 1;
        return !slots.get(lastIndex).getPlayers().isEmpty();
    }

    public void addEventsToSlot(String[][] abyssesAndTools) {
        if (abyssesAndTools == null) {
            return;
        }

        for (String[] line : abyssesAndTools) {

            String typeStr = line[0];
            String subTypeStr = line[1];
            String position = line[2];

            int positionInt = Integer.parseInt(position);
            Slot slot = encontraSlot(positionInt);

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
            } else if (typeStr.equals("1")) {
                Tool tool ;
                switch (subTypeStr) {
                    case "0" -> tool = (new Inheritance(positionInt));
                    case "1" -> tool = (new FunctionalProgramming(positionInt));
                    case "2" -> tool = (new UnitTests(positionInt));
                    case "3" -> tool = (new ExceptionHandling(positionInt));
                    case "4" -> tool = (new Ide(positionInt));
                    case "5" -> tool = (new TeacherHelp(positionInt));
                    default -> throw new IllegalArgumentException("Tipo de ferramenta invalida");
                }
                slot.addEvent(tool);
                this.tools.put(tool.getName(), tool);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public Slot getSlotOfPlayer(int playerId) {
        for (Slot slot : slots) {
            if (slot.findPlayerByID(playerId) != null) {
                return slot;
            }
        }
        return null;
    }

}
