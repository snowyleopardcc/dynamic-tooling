package net.dynamic.tooling.blocks.container;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;

public class SimpleStorageCrateController extends CottonScreenController {

    public SimpleStorageCrateController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel rootPanel = (WGridPanel) getRootPanel();

        Inventory stuff = getBlockInventory(context);

        rootPanel.add(new WLabel(new TranslatableText("block.dynamictooling.simple_storage_crate"), 0xFF00CCFF), 0, 0);

        crateInv = new WItemSlot(stuff, 0, 9, 9, false, true);

        rootPanel.add(crateInv, 0, 1);

        playerInv = this.createPlayerInventoryPanel();

        rootPanel.add(playerInv, 0, 11);

        rootPanel.validate(this);
    }

    WItemSlot crateInv;
    WPlayerInvPanel playerInv;

    @Override
    public void addPainters() {
         crateInv.setBackgroundPainter(BackgroundPainter.createColorful(0xFF333333, 0.20f));
        playerInv.setBackgroundPainter(BackgroundPainter.createColorful(0xFF333333, 0.20f));
    }


    @Override
    public int getCraftingResultSlotIndex() {

        return -1;
    }
}
