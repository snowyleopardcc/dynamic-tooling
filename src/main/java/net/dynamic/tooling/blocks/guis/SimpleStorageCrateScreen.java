package net.dynamic.tooling.blocks.guis;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import net.dynamic.tooling.blocks.container.SimpleStorageCrateController;
import net.minecraft.entity.player.PlayerEntity;

public class SimpleStorageCrateScreen extends CottonScreen<SimpleStorageCrateController> {

    public SimpleStorageCrateScreen(SimpleStorageCrateController container, PlayerEntity player) {
        super(container, player);
    }
}
