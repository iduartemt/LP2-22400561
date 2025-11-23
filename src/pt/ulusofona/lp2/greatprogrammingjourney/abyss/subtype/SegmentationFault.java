package pt.ulusofona.lp2.greatprogrammingjourney.abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.Player;

public class SegmentationFault extends Abyss {
    public SegmentationFault(int position) {
        super("Segmentation Fault", 9, position, null, "core-dumped.png");
    }

    @Override
    public void playerInteraction(Player player) {

    }
}
