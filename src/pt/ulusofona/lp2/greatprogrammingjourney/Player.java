package pt.ulusofona.lp2.greatprogrammingjourney;

public class Player {
    int id;
    String name;
    String color;

    public Player(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getColor() {
        return color;
    }
}
