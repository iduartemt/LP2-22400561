package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype.*;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype.Exception;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.subtypes.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public List<Slot> slots = new ArrayList<>();

    public Board(List<Player> players, int worldSize) {
        addSlotList(worldSize);
        addPlayerSlotFirstSlot(players);
    }

    public Board(List<Player> players, int worldSize, String[][] abyssesAndTools) {
        addSlotList(worldSize);
        addPlayerSlotFirstSlot(players);
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
            if (slot.nrSlot == nrSlot) {
                return slot;
            }
        }
        return null;
    }

    public boolean hasPlayerOnLastSlot() {
        if (slots.isEmpty()) {
            return false;
        }
        int lastIndex = slots.size() - 1;
        return !slots.get(lastIndex).players.isEmpty();
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
                switch (subTypeStr) {
                    case "0" -> slot.addEvent(new Inheritance(positionInt));
                    case "1" -> slot.addEvent(new FunctionalProgramming(positionInt));
                    case "2" -> slot.addEvent(new UnitTests(positionInt));
                    case "3" -> slot.addEvent(new ExceptionHandling(positionInt));
                    case "4" -> slot.addEvent(new Ide(positionInt));
                    case "5" -> slot.addEvent(new TeacherHelp(positionInt));
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
