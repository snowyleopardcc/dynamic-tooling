package net.dynamic.tooling.blocks.container;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.text.Text;

public class WPaddedLabel extends WLabel {

    public WPaddedLabel(Text text, int color) {
        super(text, color);
    }


    protected int width = 8;
    protected int height = 8;

    protected int paddingVertical = 4;
    protected int paddingHorizontal= 0;


    @Override
    public int getWidth() { return width + (paddingHorizontal * 2); }

    @Override
    public int getHeight() { return  height + (paddingVertical * 2); }


}
