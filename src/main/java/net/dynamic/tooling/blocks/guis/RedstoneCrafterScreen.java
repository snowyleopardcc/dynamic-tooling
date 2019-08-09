package net.dynamic.tooling.blocks.guis;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import net.dynamic.tooling.blocks.container.ManualMillController;
import net.dynamic.tooling.blocks.container.RedstoneCrafterController;
import net.minecraft.entity.player.PlayerEntity;

public class RedstoneCrafterScreen extends CottonScreen<RedstoneCrafterController> {

    public RedstoneCrafterScreen(RedstoneCrafterController container, PlayerEntity player) {
        super(container, player);
    }
}
