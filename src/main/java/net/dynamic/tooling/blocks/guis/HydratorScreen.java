package net.dynamic.tooling.blocks.guis;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import net.dynamic.tooling.blocks.container.LiquidFurnaceController;
import net.minecraft.entity.player.PlayerEntity;

public class LiquidFurnaceScreen extends CottonScreen<LiquidFurnaceController> {

    public LiquidFurnaceScreen(LiquidFurnaceController container, PlayerEntity player) {
        super(container, player);
    }
}
