package pt.ulusofona.lp2.greatprogrammingjourney;

public abstract class Event {
    private final String name;
    private final int id;
    private final int position;
    private final String image;
    private final EventType type;  // ABYSS ou TOOL

    public Event(String name, int id, int position, String image, EventType type) {
        this.name = name;
        this.id = id;
        this.position = position;
        this.image = image;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public String getImage() {
        return image;
    }

    public EventType getType() {
        return type;
    }

    public abstract void playerInteraction(Player player, Board board);

}
