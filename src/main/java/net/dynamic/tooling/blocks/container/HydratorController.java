package net.dynamic.tooling.blocks.container;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.container.BlockContext;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class LiquidFurnaceController extends CottonScreenController {

    public LiquidFurnaceController(int syncId, PlayerInventory playerInventory, BlockContext context) {

        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        rootPanel = (WGridPanel) getRootPanel();
        inventory = blockInventory;

        rootPanel.add(new WPaddedLabel(new TranslatableText("block.dynamictooling.liquid_furnace_block"), 0xFFFFFFFF), 0, 0);

        fluidTankVolumeBar = new WBar(WBAR_BACKGROUND_IMAGE, WBAR_FOREGROUND_IMAGE, 0, 1, WBar.Direction.UP);

        inputSlots = new WItemSlot(inventory, 0, 4, 1, false, false);
        outputSlots = new WItemSlot(inventory, 4, 4, 2, false, false);

        rootPanel.add(fluidTankVolumeBar, 0, 1, 1, 4);

        rootPanel.add(inputSlots, 5, 1, 4, 1);
        rootPanel.add(outputSlots, 5, 3, 4, 2);

        rootPanel.add( createPlayerInventoryPanel(), 0, 6 );

        rootPanel.validate(this);
    }

    private WGridPanel rootPanel;
    private Inventory inventory;

    private WBar fluidTankVolumeBar;

    private WItemSlot inputSlots;
    private WItemSlot outputSlots;


    /** WBar Image Identifiers */
    private final Identifier WBAR_BACKGROUND_IMAGE = new Identifier("dynamictooling:textures/gui/wbar_background.png");
    private final Identifier WBAR_FOREGROUND_IMAGE = new Identifier("dynamictooling:textures/gui/wbar_foreground.png");
}
