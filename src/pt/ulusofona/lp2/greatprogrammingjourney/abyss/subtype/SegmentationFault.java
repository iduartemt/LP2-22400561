package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Board;
import pt.ulusofona.lp2.greatprogrammingjourney.EventType;
import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class SegmentationFault extends Abyss {
    public SegmentationFault(int position) {
        super("Segmentation Fault", 9, position, null, "core-dumped.png", EventType.ABYSS);
    }

    @Override
    public void playerInteraction(Player player, Board board) {

    }
}
