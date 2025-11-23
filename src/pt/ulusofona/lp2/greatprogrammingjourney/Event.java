package pt.ulusofona.lp2.greatprogrammingjourney;

public abstract class Event {
    private final String name;
    private final int id;
    private final int position;
    private final String image;

    public Event(String name, int id, int position, String image) {
        this.name = name;
        this.id = id;
        this.position = position;
        this.image = image;
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

    public String getTypeCode() {
        String pkg = getClass().getPackageName();
        String prefix = "A"; // default: abismo

        if (pkg.contains(".tool.")) {
            prefix = "T";
        } else if (pkg.contains(".abyss.")) {
            prefix = "A";
        }
        return prefix + ":" + id;
    }

    public abstract void playerInteraction(Player player, Board board);

}
