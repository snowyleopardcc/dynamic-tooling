package net.dynamic.tooling.blocks.entities;

import net.dynamic.tooling.DynamicTooling;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public class PressBenchEntity extends BlockEntity {

    public PressBenchEntity() {
        super(DynamicTooling.PRESS_BENCH_ENTITY);
    }

    private ItemStack plate = ItemStack.EMPTY;

    public ItemStack getPlate() { return this.plate; }
    public void setPlate(ItemStack plate) {

        this.plate = plate;
        super.markDirty();
    }
}
