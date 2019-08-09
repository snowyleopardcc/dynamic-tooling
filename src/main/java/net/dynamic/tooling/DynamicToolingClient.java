package net.dynamic.tooling;

import net.dynamic.tooling.blocks.SimpleStorageCrateBlock;
import net.dynamic.tooling.blocks.container.ManualMillController;
import net.dynamic.tooling.blocks.container.RedstoneCrafterController;
import net.dynamic.tooling.blocks.container.SimpleStorageCrateController;
import net.dynamic.tooling.blocks.guis.ManualMillScreen;
import net.dynamic.tooling.blocks.guis.RedstoneCrafterScreen;
import net.dynamic.tooling.blocks.guis.SimpleStorageCrateScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.container.BlockContext;
import net.minecraft.util.Identifier;

public class DynamicToolingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ColorProviderRegistry.BLOCK.register( (block, pos, world, layer) -> {

            BlockColorProvider provider = ColorProviderRegistry.BLOCK.get(Blocks.GRASS);

            return (provider == null) ? -1 : provider.getColor(block, pos, world, layer);
        },
          DynamicTooling.LAWN_LIGHT_BLOCK
        );

        ColorProviderRegistry.BLOCK.register( (block, pos, world, layer) -> {

            BlockColorProvider provider = ColorProviderRegistry.BLOCK.get(Blocks.GRASS);

            return (provider == null) ? -1 : provider.getColor(block, pos, world, layer);
        },
          DynamicTooling.LAWN_LIGHT_SLAB_BLOCK
        );

        ScreenProviderRegistry.INSTANCE.registerFactory(SimpleStorageCrateBlock.CRATE_ID, (syncId, identifier, player, buf) -> new SimpleStorageCrateScreen(new SimpleStorageCrateController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(DynamicTooling.MILL_ID, (syncId, id, player, buf) -> new ManualMillScreen(new ManualMillController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(DynamicTooling.RSC_ID, (syncId, id, player, buf) -> new RedstoneCrafterScreen(new RedstoneCrafterController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));


//        ClientSidePacketRegistry.INSTANCE.register(new Identifier());
    }
}
