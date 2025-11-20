package pt.ulusofona.lp2.greatprogrammingjourney.Abyss.subtype;

import pt.ulusofona.lp2.greatprogrammingjourney.Abyss.Abyss;
import pt.ulusofona.lp2.greatprogrammingjourney.tool.Tool;

public class SegmentationFault extends Abyss {
    public SegmentationFault(int position) {
        super("Segmentation Fault", 9, position, null, "core-dumped.png");
    }

    @Override
    public void playerInteraction() {

    }
}
