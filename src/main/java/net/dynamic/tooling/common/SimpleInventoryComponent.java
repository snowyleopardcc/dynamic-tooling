package net.dynamic.tooling.common;

import alexiil.mc.lib.attributes.item.FixedItemInv;
import alexiil.mc.lib.attributes.item.compat.InventoryFixedWrapper;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class SimpleInventoryComponent extends InventoryFixedWrapper implements SidedInventory {

    public SimpleInventoryComponent(FixedItemInv inv) {
        super(inv);
    }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[getInvSize()];
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public boolean canExtractInvStack(int i, ItemStack itemStack, Direction direction) {
        System.out.println("slot: " + i);
        return true;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public boolean isInvEmpty() {

        return super.isInvEmpty();
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity playerEntity) {
        return true;
    }
}
