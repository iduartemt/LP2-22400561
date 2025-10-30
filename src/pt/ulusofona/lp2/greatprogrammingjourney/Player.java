package pt.ulusofona.lp2.greatprogrammingjourney;

public class Player {
    int id;
    String name;
    String language;
    String color;

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

    String getColor() {
        return color;
    }
}
