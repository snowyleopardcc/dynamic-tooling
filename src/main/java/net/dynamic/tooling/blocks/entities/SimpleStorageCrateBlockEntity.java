package net.dynamic.tooling.blocks.entities;

import alexiil.mc.lib.attributes.item.impl.SimpleFixedItemInv;
import net.dynamic.tooling.DynamicTooling;
import net.dynamic.tooling.common.SimpleInventoryComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class SimpleStorageCrateBlockEntity extends BlockEntity implements InventoryProvider {


    public SimpleStorageCrateBlockEntity() {
        super(DynamicTooling.SIMPLE_STORAGE_CRATE_BLOCK_ENTITY);

//        ChestBlockEntity

    }

    public final SimpleFixedItemInv inventory = new SimpleFixedItemInv(81);

    public final SimpleInventoryComponent invComponent = new SimpleInventoryComponent(inventory);

    @Override
    public CompoundTag toTag(CompoundTag tag) {

        tag = super.toTag(tag);
        tag.put("inventory", inventory.toTag());

        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        inventory.fromTag(tag.getCompound("inventory"));
    }

    @Override
    public SidedInventory getInventory(BlockState blockState, IWorld iWorld, BlockPos blockPos) {
        return invComponent;
    }
}
