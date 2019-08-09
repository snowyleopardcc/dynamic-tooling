package net.dynamic.tooling.blocks.guis;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import net.dynamic.tooling.blocks.container.ManualMillController;
import net.minecraft.entity.player.PlayerEntity;

public class ManualMillScreen extends CottonScreen<ManualMillController> {

    public ManualMillScreen(ManualMillController container, PlayerEntity player) {
        super(container, player);
    }
}
