package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Player {
    int id;
    String name;
    String language;
    ArrayList<Event> tools=new ArrayList<>();
    String color;
    boolean isAlive = true;

    public Player(int id, String name, String language, String color) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.color = color;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getLanguage() {
        return language;
    }

    ArrayList<Event> getTools() {
        return tools;
    }

    String getColor() {
        return color;
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
        if (language == null) {
            return null;
        }
        return language;
    }

    public StringBuilder playerLanguageInfo(List<String> sortLanguage) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sortLanguage.size(); i++) {
            sb.append(sortLanguage.get(i));
            if (i != sortLanguage.size() - 1) {
                sb.append("; ");
            }
        }
        return sb;
    }

    public List<String> getSortedLanguages(String languageStr) {
        List<String> languages = new ArrayList<>(List.of(languageStr.split(";")));
        for (int i = 0; i < languages.size(); i++) {
            languages.set(i, languages.get(i).trim());
        }
        Collections.sort(languages);
        return languages;
    }


}
