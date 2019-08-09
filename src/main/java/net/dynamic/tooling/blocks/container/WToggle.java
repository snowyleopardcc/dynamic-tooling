package net.dynamic.tooling.blocks.container;

import io.github.cottonmc.cotton.gui.client.LibGuiClient;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class WToggle extends WWidget {

    public WToggle(Identifier onImage, Identifier offImage) {

        this.onImage = onImage;
        this.offImage = offImage;
    }


    protected final Identifier onImage;
    protected final Identifier offImage;

    private boolean isOn = false;

    private Runnable onToggle;

    protected int color = WLabel.DEFAULT_TEXT_COLOR;
    protected int darkmodeColor = WLabel.DEFAULT_DARKMODE_TEXT_COLOR;

    @Environment(EnvType.CLIENT)
    @Override
    public void paintBackground(int x, int y) {

        ScreenDrawing.rect(isOn ? onImage : offImage, x, y, 16, 16, 0xFFFFFFFF);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onClick(int x, int y, int button) {
        super.onClick(x, y, button);

        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F));
        this.isOn = !this.isOn;

        onToggle(this.isOn);
    }

    public void onToggle(boolean on) {

        if(onToggle != null) { onToggle.run(); }
    }

    public boolean isOn() { return isOn; }
    public void setToggle(boolean on) {
        this.isOn = on;
    }


    public void setOnToggle(Runnable r) { this.onToggle = r; }
}
