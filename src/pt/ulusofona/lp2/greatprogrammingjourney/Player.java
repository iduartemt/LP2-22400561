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
    private boolean isAlive = true;
    private boolean isTrapped = false; // Novo atributo
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
    }

    public Player(int id, String name, String language, String color,
                  boolean isAlive, int lastDiceValue,
                  int previousPosition, int positionTwoMovesAgo) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.color = color;
        this.tools = new ArrayList<>();
        this.isAlive = isAlive;
        this.lastDiceValue = lastDiceValue;
        this.previousPosition = previousPosition;
        this.positionTwoMovesAgo = positionTwoMovesAgo;
    }

    //=====================================================GETTERS======================================================
    public int getId() {
        return id;
    }

    public boolean isLastMoveIsValid() {
        return lastMoveIsValid;
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

    public boolean getIsAlive() {
        return isAlive;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public int getLastDiceValue() {
        return lastDiceValue;
    }

    public List<String> getSortedLanguages(String languageStr) {
        List<String> languages = new ArrayList<>(List.of(languageStr.split(";")));
        for (int i = 0; i < languages.size(); i++) {
            languages.set(i, languages.get(i).trim());
        }
        Collections.sort(languages);
        return languages;
    }

    //=====================================================SETTERS=====================================================

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void setLastDiceValue(int value) {
        this.lastDiceValue = value;
    }


    public void setPreviousPosition(int previousPosition) {
        this.previousPosition = previousPosition;
    }


    public void setPositionTwoMovesAgo(int positionTwoMovesAgo) {
        this.positionTwoMovesAgo = positionTwoMovesAgo;
    }

    public void setTrapped(boolean trapped) {
        isTrapped = trapped;
    }
    //=====================================================METODOS======================================================

    public boolean addTool(Tool tool) {
        return this.tools.add(tool);
    }

    public boolean removeTool(Tool tool) {
        return this.tools.remove(tool);
    }

    public static String isValidName(String name, HashSet<String> validName) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        //duplicados
        if (!validName.add(name)) {
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

    public boolean isTrapped() {
        return isTrapped;
    }

    public String playerLanguageInfo(List<String> sortLanguage) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sortLanguage.size(); i++) {
            sb.append(sortLanguage.get(i));
            if (i != sortLanguage.size() - 1) {
                sb.append("; ");
            }
        }
        return sb.toString();
    }


}
