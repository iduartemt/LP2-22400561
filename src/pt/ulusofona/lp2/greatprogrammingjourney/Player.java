package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

import java.util.*;

public class Player {
    private final int id;
    private final String name;
    private final String language;
    private final ArrayList<Tool> tools;
    private final String color;
    //Dados
    private PlayerState state;
    private int lastDiceValue;
    private int previousPosition;
    private int positionTwoMovesAgo;
    private boolean lastMoveIsValid = true;

    public Player(int id, String name, String language, String color) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.color = color;
        this.tools = new ArrayList<>();
        this.state = PlayerState.EM_JOGO;
    }

    public Player(int id, String name, String language, String color,
                  PlayerState state, int lastDiceValue, int previousPosition,
                  int positionTwoMovesAgo) {

        this.id = id;
        this.name = name;
        this.language = language;
        this.color = color;
        this.tools = new ArrayList<>();
        this.state = state;
        this.lastDiceValue = lastDiceValue;
        this.previousPosition = previousPosition;
        this.positionTwoMovesAgo = positionTwoMovesAgo;
    }

    public static Integer isValidId(String idStr) {
        if (idStr == null || idStr.isEmpty()) {
            return null;
        }
        try {
            int id = Integer.parseInt(idStr);
            if (id < 0) {
                return null;
            }
            return id;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    //=====================================================GETTERS======================================================
    public int getId() {
        return id;
    }

    public void setLastMoveIsValid(boolean lastMoveIsValid) {
        this.lastMoveIsValid = lastMoveIsValid;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public ArrayList<Tool> getTools() {
        return tools;
    }

    public int getPositionTwoMovesAgo() {
        return positionTwoMovesAgo;
    }

    public String getColor() {
        return color;
    }

    public PlayerState getState() {
        return state;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public int getLastDiceValue() {
        return lastDiceValue;
    }

    //=====================================================SETTERS=====================================================

    public void setLastDiceValue(int value) {
        this.lastDiceValue = value;
    }


    public void setPreviousPosition(int previousPosition) {
        this.previousPosition = previousPosition;
    }


    public void setPositionTwoMovesAgo(int positionTwoMovesAgo) {
        this.positionTwoMovesAgo = positionTwoMovesAgo;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }    //=====================================================METODOS======================================================

    public List<String> getSortedLanguages(String languageStr) {
        List<String> languages = new ArrayList<>(List.of(languageStr.split(";")));
        for (int i = 0; i < languages.size(); i++) {
            languages.set(i, languages.get(i).trim());
        }
        Collections.sort(languages);
        return languages;
    }

    public static String isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return name;
    }

    public static String isValidLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            return null;
        }
        return language.trim();
    }

    public boolean hasTool(Tool tool) {
        for (Tool t : tools) {
            if (t.getId() == tool.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean canMove(int nrSpaces) {
        // Podes limpar a string da linguagem aqui ou no construtor
        String mainLang = this.language.split(";")[0].trim();

        if (mainLang.equalsIgnoreCase("Assembly") && nrSpaces > 2) {
            return false;
        }
        if (mainLang.equalsIgnoreCase("C") && nrSpaces > 3) {
            return false;
        }
        return true;
    }

}
