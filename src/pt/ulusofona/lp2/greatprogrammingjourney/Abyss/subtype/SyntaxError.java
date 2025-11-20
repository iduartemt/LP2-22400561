package pt.ulusofona.lp2.greatprogrammingjourney.Abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Abyss.Abyss;

public class SyntaxError extends Abyss {

    public SyntaxError(int position) {
        super("Erro de sintaxe", 0, position, null
                , "syntax.png");
    }

    @Override
    public void playerInteraction() {

    }
}
