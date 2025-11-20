package pt.ulusofona.lp2.greatprogrammingjourney.Abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class FileNotFound extends Abyss {
    public FileNotFound(int position) {
        super("File Not Found Exception", 3, position, null, "file-not-found-exception.png");
    }

    @Override
    public void playerInteraction() {

    }
}
