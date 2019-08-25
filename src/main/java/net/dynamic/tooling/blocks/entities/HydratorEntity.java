package net.dynamic.tooling.blocks.entities;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FixedFluidInvView;
import alexiil.mc.lib.attributes.fluid.FluidInvTankChangeListener;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.filter.ExactFluidFilter;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.dynamic.tooling.DynamicTooling;
import net.dynamic.tooling.blocks.utils.SimpleMachineryInventory;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.IWorld;

public class LiquidFurnaceEntity extends BlockEntity implements BlockEntityClientSerializable, InventoryProvider, PropertyDelegateHolder, Tickable {

    public LiquidFurnaceEntity() {
        super(DynamicTooling.LIQUID_FURNACE_ENTITY);

        FLUID_INVENTORY.addListener(fluidInvTankChangeListener, token);

    }

    private ListenerRemovalToken token = new ListenerRemovalToken() {
        @Override
        public void onListenerRemoved() {
             /** no - op,  merely required for impl of listener */
        }
    };

    private FluidInvTankChangeListener fluidInvTankChangeListener = new FluidInvTankChangeListener() {
        @Override
        public void onChange(FixedFluidInvView inv, int tank, FluidVolume previous, FluidVolume current) {

            System.out.println(current);
            System.out.println("max amt: " + FLUID_INVENTORY.getTank(0).getMaxAmount());
            System.out.println("current amt: " + FLUID_INVENTORY.getTank(0).get().getAmount());

            currentLiquidFuel = current.getAmount();

            markDirty();
        }
    };

    private final SimpleMachineryInventory INVENTORY = new SimpleMachineryInventory(4, 8);

    public final int FLUID_CAPACITY = FluidVolume.BUCKET * 20;

    public final SimpleFixedFluidInv FLUID_INVENTORY = new SimpleFixedFluidInv(1/* 1 slot */, FLUID_CAPACITY ) {

        @Override
        public FluidFilter getFilterForTank(int tank) { return new ExactFluidFilter(FluidKeys.LAVA); }

        @Override
        public boolean isFluidValidForTank(int tank, FluidKey fluid) { return getFilterForTank(tank).matches(fluid); }
    };

    public int currentLiquidFuel = 0;

    @Override
    public void fromTag(CompoundTag compoundTag) {
         super.fromTag(compoundTag);

        FLUID_INVENTORY.fromTag(compoundTag);
        currentLiquidFuel = FLUID_INVENTORY.getTank(0).get().getAmount();
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag_1) {
        CompoundTag tag = super.toTag(compoundTag_1);

        FLUID_INVENTORY.toTag(tag);
        return tag;
    }


    // Plus a call to offer the above fluid inv

    public boolean onActivate(PlayerEntity player, Hand hand) {

        return FluidVolumeUtil.interactWithTank(FLUID_INVENTORY, player, hand);
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return delegate;
    }

    private PropertyDelegate delegate = new PropertyDelegate() {

        @Override
        public int get(int i) {

            switch (i) {

                case 0:
                    return LiquidFurnaceEntity.this.currentLiquidFuel;
//                    return LiquidFurnaceEntity.this.FLUID_INVENTORY.getTank(0).get().getAmount();
                case 1:
                    return LiquidFurnaceEntity.this.FLUID_INVENTORY.getTank(0).getMaxAmount();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int i, int value) {

            switch (i) {

                case 0:
                    LiquidFurnaceEntity.this.currentLiquidFuel = value;

            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    @Override
    public SidedInventory getInventory(BlockState blockState, IWorld iWorld, BlockPos blockPos) {
        return INVENTORY;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {

        super.fromTag(tag);

        this.fromTag(tag);
//        markDirty();
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        CompoundTag tag2 = super.toTag(tag);

        this.toTag(tag2);

        return tag2;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }


    @Override
    public void tick() {

        if(!world.isClient) {

//            SmeltingRecipe


        }
    }
}
