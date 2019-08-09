package net.dynamic.tooling.blocks.entities;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.dynamic.tooling.DynamicTooling;
import net.dynamic.tooling.blocks.utils.CraftableStorageInventory;
import net.dynamic.tooling.blocks.utils.FakeInventory;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;

public class RedstoneCrafterEntity extends BlockEntity implements BlockEntityClientSerializable, InventoryProvider, PropertyDelegateHolder, Tickable {

    public RedstoneCrafterEntity() {

        super(DynamicTooling.REDSTONE_CRAFTER_ENTITY);

//        AbstractFurnaceBlockEntity
//        ShulkerBoxBlockEntity
    }


    private boolean hasPower = false;
    private boolean isLocked = false;

    /**
     * Work units are the units of measurement for each craft.
     * Each craft requires x(WU).
     * Total WU should determine time for each craft.
     * If `tick()` is called 20 x / sec,  3 seconds should be 60wu.
     * WU Progress tracks where we are in the current crafting op.
     */
    private int wuPerCraft = 3000;
    private int wuCurrentProgress = 0;
    private long lastOp = 0;

    public boolean getHasPower() { return hasPower; }

    public void setHasPower(boolean value) {

        this.hasPower = value;
        super.markDirty();
    }

    public final CraftableStorageInventory INVENTORY = new CraftableStorageInventory( 8, 8 );

    @Override
    public SidedInventory getInventory(BlockState blockState, IWorld iWorld, BlockPos blockPos) {
        return INVENTORY;
    }


    @Override
    public void fromTag(CompoundTag tag) {

        Inventories.fromTag(tag, INVENTORY.getStacks());

        this.setHasPower(tag.getBoolean("on"));

        this.setHasPower(tag.getBoolean("isLocked"));

        this.wuCurrentProgress = tag.getInt("currentProgress");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {

//        INVENTORY.toTag(tag);

        Inventories.toTag(tag, INVENTORY.getStacks());

        tag.putBoolean("on", this.hasPower);

        tag.putBoolean("isLocked", this.isLocked);

        tag.putInt("currentProgress", this.wuCurrentProgress);

        return tag;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {

        this.fromTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {

        return this.toTag(tag);
    }

    protected boolean canOutput(ItemStack output) {

        boolean result = false;
        int count = output.getCount();

        for(int idx = 9; idx < 17; idx++) {

            if(INVENTORY.getInvStack(idx).isEmpty()) {
                result = true;
            }
            else if(INVENTORY.getInvStack(idx).getCount() + output.getCount() < INVENTORY.getInvStack(idx).getItem().getMaxCount()) {

                result = true;
            }
            else { result = false; }
        }

        return result;
    }

    @Override
    public void tick() {

        if(!world.isClient) {

            if(this.hasPower) {

                FakeInventory temp = INVENTORY.CRAFTING_INVENTORY;

                RecipeManager mgr = world.getRecipeManager();
                CraftingRecipe recipe = mgr.getFirstMatch(RecipeType.CRAFTING, temp, world).orElse(null);

                if (recipe != null && canOutput(recipe.getOutput())) {

                    /**
                     *
                     *  1.)  Is there a valid recipe?
                     *  2.)  What ingredients do we need?
                     *          --  Shape is not important,  only Items and Quantities.
                     *  3.)  What is available in the input?
                     *  4.)  For each ingredient,  check if there is enough in input.
                     *  5.)  If there is enough to craft,  go through and subtract quantities.
                     *  6.)  Finally set output of recipe to first available output slot.
                     */

                    /** consolidated listing of ingredients --
                     * each Item Stack has correct count (or should...?)
                     */
                    DefaultedList<ItemStack> ingredients = INVENTORY.getIngredients();

                    /** Now we're checking each ingredients availability */
                    boolean isCraftable = false;

                    for (ItemStack ingredient : ingredients) {

                        if (!ingredient.isEmpty()) {

                            isCraftable = INVENTORY.hasIngredient(ingredient);
                        }
                    }

                    if (isCraftable) {

                        for (int idx = 0; idx < ingredients.size(); idx++) {

                            if (!ingredients.get(idx).isEmpty()) {

                                INVENTORY.takeItemStack(ingredients.get(idx));
                            }
                        }


                        /** get copy of recipe output,  the count
                         * can be a bit weird and glitchy,  so
                         * we're double checking it.
                         */
                        ItemStack output = recipe.getOutput().copy();
                        output.setCount(recipe.getOutput().getCount());


                        /** go through output slots and find first available */
                        for (int idx = 9; idx < 17; idx++) {

                            ItemStack current = INVENTORY.getInvStack(idx).copy();

                            if (current.isEmpty()) {
                                INVENTORY.setInvStack(idx, output);
                                markDirty();
                                break;
                            } else if (current.getItem() == output.getItem()) {

                                int c1 = current.getCount();
                                int c2 = output.getCount();
                                int c3 = current.getItem().getMaxCount();

                                if ((c1 + c2) <= c3) {

                                    /** some serious weirdness going on here ....*/

                                    current.increment(output.getCount());
                                    INVENTORY.setInvStack(idx, current);
                                    markDirty();
                                    break;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }


//                /** 3000 ms or 3 seconds total crafting time*/
//
//                long current = System.currentTimeMillis();
//                /** check if last op has been set,  if not use 0 delta */
//                long deltaTime = (lastOp != 0) ? (current - lastOp)  : 0;
//
//                lastOp = current;
//
//                this.wuCurrentProgress += (int)deltaTime;
//
//                if( this.wuCurrentProgress >= wuPerCraft) { this.wuCurrentProgress = 0; }
//
//                System.out.println("progress: " + this.wuCurrentProgress);
//                markDirty();
            }




//            markDirty();
        }
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {

        return this.delegate;
    }


    private PropertyDelegate delegate = new PropertyDelegate() {
        @Override
        public int get(int i) {

            switch (i) {

                /** Has Power Property */
                case 0: return (RedstoneCrafterEntity.this.hasPower) ? 1 : 0;

                /** IS_LOCKED */
                case 1: return (RedstoneCrafterEntity.this.isLocked) ? 1 : 0;

                /** Current Work Units / Progress */
                case 2: return RedstoneCrafterEntity.this.wuCurrentProgress;

                /** Total Work Units / Progress */
                case 3: return RedstoneCrafterEntity.this.wuPerCraft;

                default: return 0;
            }
        }

        @Override
        public void set(int i, int value) {

            switch (i) {

                case 0:
                    RedstoneCrafterEntity.this.hasPower = (value == 1);
                break;

                case 1:
                    RedstoneCrafterEntity.this.isLocked = (value == 1);

                case 2:
                    RedstoneCrafterEntity.this.wuCurrentProgress = value;

                    /**  There should be no valid reason for this ever being called... */
                case 3:
                    RedstoneCrafterEntity.this.wuPerCraft = value;

                break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };
}
