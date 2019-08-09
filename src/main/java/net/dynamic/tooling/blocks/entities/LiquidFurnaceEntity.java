package net.dynamic.tooling.blocks.entities;

import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.filter.ExactFluidFilter;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.dynamic.tooling.DynamicTooling;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class LiquidFurnaceEntity extends BlockEntity  {
    public LiquidFurnaceEntity() {
        super(DynamicTooling.LIQUID_FURNACE_ENTITY);

    }


    private final int FLUID_CAPACITY = FluidVolume.BUCKET * 20;

    public final SimpleFixedFluidInv FLUID_INVENTORY = new SimpleFixedFluidInv(1/* 1 slot */, FLUID_CAPACITY ) {

        @Override
        public FluidFilter getFilterForTank(int tank) { return new ExactFluidFilter(FluidKeys.LAVA); }

        @Override
        public boolean isFluidValidForTank(int tank, FluidKey fluid) { return getFilterForTank(tank).matches(fluid); }
    };


    // Plus a call to offer the above fluid inv

    public boolean onActivate(PlayerEntity player, Hand hand) {

        return FluidVolumeUtil.interactWithTank(FLUID_INVENTORY, player, hand);
    }
}
