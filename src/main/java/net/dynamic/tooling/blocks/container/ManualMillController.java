package net.dynamic.tooling.blocks.container;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ManualMillController extends CottonScreenController {

    public ManualMillController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        rootPanel = (WGridPanel) getRootPanel();

        Inventory stuff = getBlockInventory(context);

        rootPanel.add(new WPaddedLabel(new TranslatableText("block.dynamictooling.manual_mill_block"), 0xFFFFFFFF), 1, 0);

        WBar progress = new WBar(new Identifier("dynamictooling", "textures/gui/wbar_background.png"), new Identifier("dynamictooling", "textures/gui/wbar_foreground.png"), 0, 1, WBar.Direction.UP);

        inputSlot = WItemSlot.of(stuff, 0);
        millInv = new WItemSlot(stuff, 1, 3, 2, false, true);

        machinePanel = new WGridPanel();

        machinePanel.add(inputSlot, 0, 0);

        machinePanel.add(progress, 2, 0, 1, 2);

        machinePanel.add(millInv, 4, 0, 3,2);


        playerInv = this.createPlayerInventoryPanel();

        rootPanel.add(playerInv, 0, 4);
        rootPanel.add(machinePanel, 1, 1, 5, 2);
//        rootPanel.add(progress, 7, 1, 1, 2);


        rootPanel.validate(this);
    }

    WGridPanel rootPanel;
    WGridPanel machinePanel;

    WItemSlot inputSlot;

    WItemSlot millInv;

    WGridPanel playerInvPanel;
    WPlayerInvPanel playerInv;


    @Override
    public void addPainters() {

        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(0xFF555555, 0.20f));

        machinePanel.setBackgroundPainter(BackgroundPainter.createColorful(0xFF333333, 0.20f));

//        playerInvPanel.setBackgroundPainter(BackgroundPainter.createColorful(0xFF333333, 0.10f));
    }



    @Override
    public int getCraftingResultSlotIndex() {
        return -1;
    }
}
