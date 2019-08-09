package net.dynamic.tooling.items;

import net.dynamic.tooling.DynamicTooling;
import net.dynamic.tooling.items.standardtools.AdzeItem;
import net.dynamic.tooling.items.standardtools.ExcavatorItem;
import net.dynamic.tooling.items.standardtools.HammerItem;
import net.dynamic.tooling.items.standardtools.MinersDelightItem;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemsHandler {


    /**
     * Items
     *
     * 1.) Excavators
     * 2.) Miner's Delight
     */
    public static final Item DIAMOND_EXCAVATOR_ITEM = new ExcavatorItem( ToolMaterials.DIAMOND, 8.0f, 8.0f,  new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item GOLD_EXCAVATOR_ITEM    = new ExcavatorItem( ToolMaterials.GOLD,    6.0f, 6.0f,  new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item IRON_EXCAVATOR_ITEM    = new ExcavatorItem( ToolMaterials.IRON,    5.0f, 5.0f,  new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item STONE_EXCAVATOR_ITEM   = new ExcavatorItem( ToolMaterials.STONE,   4.0f, 4.0f,  new Item.Settings().group(DynamicTooling.DT_GROUP));


    public static final Item DIAMOND_MINERS_DELIGHT_ITEM    = new MinersDelightItem( 3, 8.0f, ToolMaterials.DIAMOND, new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item GOLD_MINERS_DELIGHT_ITEM       = new MinersDelightItem( 2, 6.0f, ToolMaterials.GOLD,    new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item IRON_MINERS_DELIGHT_ITEM       = new MinersDelightItem( 2, 5.0f, ToolMaterials.IRON,    new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item STONE_MINERS_DELIGHT_ITEM      = new MinersDelightItem( 1, 4.0f, ToolMaterials.STONE,   new Item.Settings().group(DynamicTooling.DT_GROUP));


    public static final Item DIAMOND_HAMMER_ITEM    = new HammerItem( 3, 8.0f, ToolMaterials.DIAMOND,   new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item GOLD_HAMMER_ITEM       = new HammerItem( 2, 8.0f, ToolMaterials.GOLD,      new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item IRON_HAMMER_ITEM       = new HammerItem( 2, 8.0f, ToolMaterials.IRON,      new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item STONE_HAMMER_ITEM      = new HammerItem( 1, 8.0f, ToolMaterials.STONE,     new Item.Settings().group(DynamicTooling.DT_GROUP));


    public static final Item DIAMOND_ADZE_ITEM    = new AdzeItem( 3, 8.0f, ToolMaterials.DIAMOND,   new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item GOLD_ADZE_ITEM       = new AdzeItem( 2, 8.0f, ToolMaterials.GOLD,      new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item IRON_ADZE_ITEM       = new AdzeItem( 2, 8.0f, ToolMaterials.IRON,      new Item.Settings().group(DynamicTooling.DT_GROUP));
    public static final Item STONE_ADZE_ITEM      = new AdzeItem( 1, 8.0f, ToolMaterials.STONE,     new Item.Settings().group(DynamicTooling.DT_GROUP));


    public  static final Item HARDWOOD_HANDLE = new Item(new Item.Settings().group(DynamicTooling.DT_GROUP));


    public static void init() {

        /**
         * Register Items
         */

        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "diamond_excavator_item"),       DIAMOND_EXCAVATOR_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "gold_excavator_item"),          GOLD_EXCAVATOR_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "iron_excavator_item"),          IRON_EXCAVATOR_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "stone_excavator_item"),         STONE_EXCAVATOR_ITEM);

        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "diamond_miners_delight_item"),  DIAMOND_MINERS_DELIGHT_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "gold_miners_delight_item"),     GOLD_MINERS_DELIGHT_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "iron_miners_delight_item"),     IRON_MINERS_DELIGHT_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "stone_miners_delight_item"),    STONE_MINERS_DELIGHT_ITEM);

        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "diamond_hammer_item"),      DIAMOND_HAMMER_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "gold_hammer_item"),         GOLD_HAMMER_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "iron_hammer_item"),         IRON_HAMMER_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "stone_hammer_item"),        STONE_HAMMER_ITEM);

        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "diamond_adze_item"),        DIAMOND_ADZE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "gold_adze_item"),           GOLD_ADZE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "iron_adze_item"),           IRON_ADZE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "stone_adze_item"),          STONE_ADZE_ITEM);

        Registry.register(Registry.ITEM, new Identifier(DynamicTooling.MOD_ID, "hardwood_handle_item"), HARDWOOD_HANDLE);
    }
}
