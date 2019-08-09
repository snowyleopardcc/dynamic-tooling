package net.dynamic.tooling.blocks.container;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.cottonmc.cotton.gui.widget.*;
import io.netty.buffer.Unpooled;
import net.dynamic.tooling.blocks.entities.RedstoneCrafterEntity;
import net.dynamic.tooling.blocks.utils.CraftableStorageInventory;
import net.dynamic.tooling.blocks.utils.FakeInventory;
import net.fabricmc.fabric.api.block.BlockSettingsExtensions;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.block.FabricMaterialBuilder;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.mixin.builders.BlockSettingsHooks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.container.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.BlueIceFeature;

public class RedstoneCrafterController extends CottonScreenController {

    public RedstoneCrafterController(int syncId, PlayerInventory playerInventory, BlockContext context) {

        super(RecipeType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        context.run((world, pos) -> {

            this.blockPosition = pos;
        });

        /** Setup stuff */
        stuff = (CraftableStorageInventory)getBlockInventory(context);
        CRAFTING = ((CraftableStorageInventory)stuff).CRAFTING_INVENTORY;

        rootPanel = (WGridPanel) getRootPanel();
        rootPanel.add(new WLabel(new TranslatableText("block.dynamictooling.rsc_block"), 0xFFFFFFFF), 0, 0, 8, 1);


        /** Crafting Grid Stuff */
        WItemSlot craftingGrid = new WItemSlot(CRAFTING, 0, 3, 3, false, true);
        resultSlot = new WDisplaySlot(stuff, RESULT_SLOT_START);

        /** Buttons for lock and power stuff. */

        lockButton = new WToggle(TOGGLE_ON_IMAGE, TOGGLE_OFF_IMAGE);
        powerButton = new WToggle(ON_IMAGE, OFF_IMAGE);

        /** Wbars for progress indicator */
        WBar progressBar = new WBar(WBAR_BACKGROUND_IMAGE, WBAR_FOREGROUND_IMAGE, 2, 3, WBar.Direction.UP);

        /** I/O Inventory Slots Stuff. */
        WItemSlot inputGrid = new WItemSlot(stuff, INPUT_SLOT_START, 4, 2, false, true);
        WItemSlot outputGrid = new WItemSlot(stuff, OUTPUT_SLOT_START, 4, 2, false, true);

        /** Player Inventory Stuff */
        playerInv = this.createPlayerInventoryPanel();

        /** Putting everything together stuff */

        rootPanel.add(craftingGrid, 0, 1);
        rootPanel.add(resultSlot, 5, 1);

//        rootPanel.add(lockButton, 5, 4);
        rootPanel.add(powerButton, 8, 0);
        rootPanel.add(progressBar, 8, 2, 1, 2);

        rootPanel.add(inputGrid, 0, 5);
        rootPanel.add(outputGrid, 5, 5);

        rootPanel.add(playerInv, 0, 8);
        rootPanel.validate(this);

        /**
         * Button Click Handlers
         * 0.) ON Property
         * 1.) Is Locked Property
         */
        lockButton.setOnToggle( ()-> {

            getPropertyDelegate().set(1, (lockButton.isOn()) ? 1 : 0 );
        });

        powerButton.setOnToggle( () -> {

            PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());

            buffer.writeBlockPos(this.blockPosition);
            buffer.writeInt(0);
            buffer.writeBoolean(powerButton.isOn());

            ClientSidePacketRegistry.INSTANCE.sendToServer(new Identifier("dynamictooling:server_packet_rs_crafter"), buffer);

//            getPropertyDelegate().set(0, (powerButton.isOn()) ? 1 : 0 );
        });
    }

    /**
     * GUI Image Identifiers
     */
    private final Identifier ON_IMAGE  = new Identifier("dynamictooling:textures/gui/on_power_button.png");
    private final Identifier OFF_IMAGE = new Identifier("dynamictooling:textures/gui/off_power_button.png");

    private final Identifier TOGGLE_ON_IMAGE  = new Identifier("libgui:widget/toggle_off.png");
    private final Identifier TOGGLE_OFF_IMAGE  = new Identifier("libgui:widget/toggle_on.png");

    private final Identifier WBAR_BACKGROUND_IMAGE = new Identifier("dynamictooling:textures/gui/wbar_background.png");
    private final Identifier WBAR_FOREGROUND_IMAGE = new Identifier("dynamictooling:textures/gui/wbar_foreground.png");


    WToggle lockButton;
    WToggle powerButton;


    CraftableStorageInventory stuff;
    BlockPos blockPosition;

    /** Slot Index fields
     *  Crafting is on a separate inventory now!
     */
    private final int RESULT_SLOT_START = 0;
    private final int INPUT_SLOT_START = 1;
    private final int INPUT_SLOT_COUNT = 8;
    private final int OUTPUT_SLOT_START = INPUT_SLOT_START + INPUT_SLOT_COUNT;

    WGridPanel rootPanel;

    WDisplaySlot resultSlot;

    WPlayerInvPanel playerInv;

    protected  final FakeInventory CRAFTING;

//    @Override
//    public void addPainters() {

//        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(0xFF555555, 0.20f));

//        inputPanel.setBackgroundPainter(BackgroundPainter.createColorful(0xFF333333, 0.20f));
//    }


    @Override
    public ItemStack onSlotClick(int slotNumber, int button, SlotActionType action, PlayerEntity player) {

        ItemStack cursor = player.inventory.getCursorStack();

        if(slotNumber < 0 || slotNumber >= slotList.size() ) { return ItemStack.EMPTY; }

        Slot slot = slotList.get(slotNumber);

        if( slot.inventory instanceof FakeInventory) {

            if (action == SlotActionType.PICKUP) {

                System.out.println("slot pickup action performed");
                ItemStack copy = cursor.copy();
                copy.setCount(1);

                slot.setStack(copy);

                CRAFTING.setInvStack(slotNumber, copy);

                CraftingRecipe test = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, CRAFTING, world).orElse(null);

                ItemStack output = (test != null) ? test.getOutput() : ItemStack.EMPTY;

                blockInventory.setInvStack(RESULT_SLOT_START, output);

                slot.markDirty();

                return cursor;
            }
            else {

                return cursor;
            }

        }
//        else if ( ( slot.inventory instanceof CraftableStorageInventory || slot.inventory instanceof PlayerInventory) && action != SlotActionType.QUICK_MOVE) {
//
//            return super.onSlotClick(slotNumber, button, action, player);
//        }
        else {

            return super.onSlotClick(slotNumber, button, action, player);

//            return cursor;
        }
    }


    @Override
    public int getCraftingResultSlotIndex() {
        return -1;
    }
}
