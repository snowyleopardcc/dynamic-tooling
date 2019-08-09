package net.dynamic.tooling;

import net.dynamic.tooling.blocks.*;
import net.dynamic.tooling.blocks.container.RedstoneCrafterController;
import net.dynamic.tooling.blocks.entities.LiquidFurnaceEntity;
import net.dynamic.tooling.blocks.entities.RedstoneCrafterEntity;
import net.dynamic.tooling.blocks.lawnblocks.LawnLightBlock;
import net.dynamic.tooling.blocks.container.ManualMillController;
import net.dynamic.tooling.blocks.entities.ManualMillEntity;
import net.dynamic.tooling.blocks.entities.SimpleStorageCrateBlockEntity;
import net.dynamic.tooling.blocks.container.SimpleStorageCrateController;
import net.dynamic.tooling.blocks.lawnblocks.LawnLightBlockEntity;
import net.dynamic.tooling.blocks.lawnblocks.LawnLightSlabBlock;
import net.dynamic.tooling.enchantments.EnchantsHandler;
import net.dynamic.tooling.items.ItemsHandler;
import net.dynamic.tooling.recipes.DTRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.BlockContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class DynamicTooling implements ModInitializer {


    public static final String MOD_ID = "dynamictooling";

    public static final ItemGroup DT_GROUP = FabricItemGroupBuilder.create(
            new Identifier(DynamicTooling.MOD_ID, "dynamic_tooling"))
            .icon(() -> new ItemStack(Blocks.SMITHING_TABLE))
            .build();


    /**
     * TODO:  Refactor into their own blocks handler at some point...
     */
    /**
     * Blocks
     */

    public static final Block SIMPLE_STORAGE_CRATE_BLOCK = new SimpleStorageCrateBlock(FabricBlockSettings.of(Material.WOOD).build());

    public static final Block MANUAL_MILL_BLOCK = new ManualMillBlock(FabricBlockSettings.of(Material.METAL).build());
    public static final Block FLY_WHEEL_BLOCK = new FlyWheelBlock(FabricBlockSettings.of(Material.WOOD).build());

    public static final Block LAWN_LIGHT_BLOCK = new LawnLightBlock(FabricBlockSettings.of(Material.GLASS).hardness(25.0f).build());
    public static final Block LAWN_LIGHT_SLAB_BLOCK = new LawnLightSlabBlock(FabricBlockSettings.of(Material.GLASS).hardness(25.0f).build());

    public static final Block REDSTONE_CRAFTER_BLOCK = new RedstoneCrafterBlock(FabricBlockSettings.of(Material.WOOD).build());

    public static final Block LIQUID_FURNACE_BLOCK = new LiquidFurnaceBlock();

    /**
     * Block Entities
     */

    public static BlockEntityType<SimpleStorageCrateBlockEntity> SIMPLE_STORAGE_CRATE_BLOCK_ENTITY;
    public static final Identifier CRATE_ID = new Identifier(DynamicTooling.MOD_ID, "simple_storage_crate");


    public static BlockEntityType<ManualMillEntity> MANUAL_MILL_ENTITY;
    public static final Identifier MILL_ID = new Identifier(DynamicTooling.MOD_ID, "manual_mill");


    public static BlockEntityType<LawnLightBlockEntity> LAWN_LIGHT_ENTITY;
    public static final Identifier LIGHT_ID = new Identifier(DynamicTooling.MOD_ID, "lawn_light");


    public static BlockEntityType<RedstoneCrafterEntity> REDSTONE_CRAFTER_ENTITY;
    public static final Identifier RSC_ID = new Identifier(DynamicTooling.MOD_ID,"rs_crafter");

    public static BlockEntityType<LiquidFurnaceEntity>  LIQUID_FURNACE_ENTITY;
    public static final Identifier FURNACE_ID = new Identifier(DynamicTooling.MOD_ID, "liquid_furnace");

    @Override
    public void onInitialize() {

        System.out.println("Hello Fabric world!");

        ItemsHandler.init();

        EnchantsHandler.init();


        /**
         * Register Blocks
         */

        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "simple_storage_crate_block"), SIMPLE_STORAGE_CRATE_BLOCK);

        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "manual_mill_block"), MANUAL_MILL_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "fly_wheel_block"), FLY_WHEEL_BLOCK);

        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "lawn_light_block"), LAWN_LIGHT_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "lawn_light_slab_block"), LAWN_LIGHT_SLAB_BLOCK);

        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "rs_crafter_block"), REDSTONE_CRAFTER_BLOCK);

        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "liquid_furnace_block"), LIQUID_FURNACE_BLOCK);


        /**
         * Register Block Items
         */

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "simple_storage_crate_block"), new BlockItem(SIMPLE_STORAGE_CRATE_BLOCK, new Item.Settings().group(DT_GROUP)));

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "manual_mill_block"), new BlockItem(MANUAL_MILL_BLOCK, new Item.Settings().group(DT_GROUP)));
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "fly_wheel_item"), new BlockItem(FLY_WHEEL_BLOCK, new Item.Settings().group(DT_GROUP)));

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "lawn_light_block_item"), new BlockItem(LAWN_LIGHT_BLOCK, new Item.Settings().group(DT_GROUP)));
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "lawn_light_slab_block_item"), new BlockItem(LAWN_LIGHT_SLAB_BLOCK, new Item.Settings().group(DT_GROUP)));

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "rs_crafter_block_item"), new BlockItem(REDSTONE_CRAFTER_BLOCK, new Item.Settings().group(DT_GROUP)));

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "liquid_furnace_block_item"), new BlockItem(LIQUID_FURNACE_BLOCK, new Item.Settings().group(DT_GROUP)));


        /**
         * Register Block Entities
         */

        SIMPLE_STORAGE_CRATE_BLOCK_ENTITY =  Registry.register(
                Registry.BLOCK_ENTITY,
                CRATE_ID,
                BlockEntityType.Builder.create(SimpleStorageCrateBlockEntity::new, SIMPLE_STORAGE_CRATE_BLOCK).build(null));


        MANUAL_MILL_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY,
                MILL_ID,
                BlockEntityType.Builder.create(ManualMillEntity::new, MANUAL_MILL_BLOCK).build(null)
        );

        LAWN_LIGHT_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY,
            LIGHT_ID,
            BlockEntityType.Builder.create(LawnLightBlockEntity::new, LAWN_LIGHT_BLOCK).build(null)
        );

        REDSTONE_CRAFTER_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY,
                RSC_ID,
                BlockEntityType.Builder.create(RedstoneCrafterEntity::new, REDSTONE_CRAFTER_BLOCK).build(null)
        );

        LIQUID_FURNACE_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY,
                FURNACE_ID,
                BlockEntityType.Builder.create(LiquidFurnaceEntity::new, LIQUID_FURNACE_BLOCK).build(null)
        );


        /**
         * Container Registry Stuff
         */

        ContainerProviderRegistry.INSTANCE.registerFactory(SimpleStorageCrateBlock.CRATE_ID, (syncId, id, player, buf) -> new SimpleStorageCrateController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));

        ContainerProviderRegistry.INSTANCE.registerFactory(MILL_ID, (syncId, id, player, buf) -> new ManualMillController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));

        ContainerProviderRegistry.INSTANCE.registerFactory(RSC_ID, (syncId, id, player, buf) -> new RedstoneCrafterController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));


        /**
         * Register Custom Recipes and providers.
         */

        DTRecipes.init();

        ServerSidePacketRegistry.INSTANCE.register(new Identifier("dynamictooling:server_packet_rs_crafter"), (context, buffer) -> {

            BlockPos pos = buffer.readBlockPos();

            World world = context.getPlayer().world;

            int index = buffer.readInt();

            if(world != null) {

                context.getTaskQueue().execute(() -> {

                    BlockEntity be = world.getBlockEntity(pos);

                    if(be instanceof RedstoneCrafterEntity) {

                        RedstoneCrafterEntity rsc = (RedstoneCrafterEntity)be;

                        switch (index) {

                            case 0: rsc.setHasPower(!rsc.getHasPower());
                        }

                        System.out.println("Has power: " + rsc.getHasPower());
                        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

                        rsc.markDirty();
                    }
                });

            }
        });


        /**
         *
         *
         * SimpleFixedItemInv for your actual item storage
         * then InventoryFixedWrapper to talk to vanilla and gui's
         *
         * then umm
         * setOwnerListener to call your blockentity's markDirty
         * so the inventory stays correct after you close and open the crate again
         *
         *
         * definitely do yours though too, we need new blood more than we need aether
         * redstone, multiply the floats by 255 and cast to int for your color components
         * then bitshift them left into their places in the int
         * every "character" of hex is 4 bits
         * so g in aarrggbb is 8 bits to the left
         */



    }
}
