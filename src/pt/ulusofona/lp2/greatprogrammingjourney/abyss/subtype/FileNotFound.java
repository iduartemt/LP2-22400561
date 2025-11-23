package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class FileNotFound extends Abyss {
    public FileNotFound(int position) {
        super("File Not Found Exception", 3, position, null, "file-not-found-exception.png");
    }

    @Override
    public void playerInteraction(Player player) {

    }
}
