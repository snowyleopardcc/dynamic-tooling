package net.dynamic.tooling.blocks.entities;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.dynamic.tooling.DynamicTooling;
import net.dynamic.tooling.blocks.utils.SimpleMachineryInventory;
import net.dynamic.tooling.recipes.DTRecipes;
import net.dynamic.tooling.recipes.ManualMillRecipe;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;

import java.util.HashMap;
import java.util.Map;

public class ManualMillEntity extends BlockEntity implements BlockEntityClientSerializable, InventoryProvider, PropertyDelegateHolder, RecipeInputProvider {

    public ManualMillEntity() {
        super(DynamicTooling.MANUAL_MILL_ENTITY);
//        BlockEntityRenderer
        recipes.put(Blocks.IRON_ORE.asItem(), Registry.ITEM.get(new Identifier("c:iron_dust")));
        recipes.put(Blocks.GOLD_ORE.asItem(), Registry.ITEM.get(new Identifier("c:gold_dust")));

    }

    public final SimpleMachineryInventory INVENTORY = new SimpleMachineryInventory(1, 6);

    public boolean isScheduledForWork = false;

    private int workTime = 0;
    private int workTimeTotal = 0;

    private ManualMillRecipe operation = null;

    public Map<Item, Item> recipes = new HashMap<Item, Item>();
    private ItemStack currentRecipe = ItemStack.EMPTY;

    @Override
    public SidedInventory getInventory(BlockState blockState, IWorld iWorld, BlockPos blockPos) {

        return INVENTORY;
    }


    @Override
    public CompoundTag toTag(CompoundTag tag) {

        tag = super.toTag(tag);

        int invSlots = INVENTORY.getInvSize();

        for(int i = 0; i < invSlots; i ++ ) {

            tag.put("inventory", INVENTORY.getInvStack(i).toTag(new CompoundTag()));
        }

        tag.putShort("workTime", (short)workTime);

        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {

        ListTag tags = tag.getList("inventory", new CompoundTag().getType());

        for( int i = 0; i < tags.size(); i++ ) {

            INVENTORY.setInvStack(i, ItemStack.fromTag(tags.getCompoundTag(i)));
        }

        workTime = (int)tag.getShort("workTime");
    }



    public void pulse() {

        int inc = 10;

        if(!world.isClient()) {

            // TODO:  Serious refactoring,  this is just me walking through the thought process...

            ItemStack input = INVENTORY.getInvStack(0);

            ManualMillRecipe recipe =  world.getRecipeManager().getFirstMatch(DTRecipes.MANUAL_MILL, INVENTORY, world).orElse(null);

            if(recipe != null) {

                this.workTimeTotal = recipe.getDuration();

                if(workTime > workTimeTotal && INVENTORY.getOutputSlot(recipe.getOutput()) != -1) {

                    for(int slot : INVENTORY.getOutputSlots()) {

                        if(!INVENTORY.getInvStack(slot).isEmpty()) {

                            if(INVENTORY.getInvStack(slot).getItem().getName().equals(recipe.getOutput().getItem().getName())) {

                                INVENTORY.getInvStack(slot).increment(recipe.getCount());
                                break;
                            }
                        }
                        else {

                            ItemStack result = recipe.getOutput();
                            result.setCount(recipe.getCount());
                            INVENTORY.setInvStack(slot, result);
                            break;
                        }
                    }

                    INVENTORY.takeInvStack(0, 1);
                    workTime = 0;
                    markDirty();
                }
                else {

                    workTime += inc;
                }
            }
        }
    }

    private int getOutputSlot(ItemStack output) {

        /** if we can't output,  we return -1 (default) */
        int result = -1;

        /**
         * 1.) check slots first to see if a matching item exists
         * 2.) if we find a match,  can we inc?  if not skip ahead to next slot
         */
        for(int slot : INVENTORY.getOutputSlots()) {

            ItemStack current = INVENTORY.getInvStack(slot);
            if(!current.isEmpty()) {

                if(INVENTORY.getInvStack(slot) == output) {

                    if(current.getCount() + 1 < current.getMaxCount()) {

                        result = slot;
                        break;
                    }
                }
            }
        }

        /** no existing item found,  find first available slot to stick output in*/
        if(result == -1) {


            for(int slot : INVENTORY.getOutputSlots()) {

                if(INVENTORY.getInvStack(slot).isEmpty()) {

                    result = slot;
                    break;
                }
            }
        }

        return result;
    }

    public void incWorkProgress() {


        isScheduledForWork = true;
    }


    @Override
    public PropertyDelegate getPropertyDelegate() {
        return new PropertyDelegate() {

            @Override
            public int get(int i) {
                switch (i) {

                    case 0:
                        return ManualMillEntity.this.workTime;
                    case 1:
                        return ManualMillEntity.this.workTimeTotal;
                    default: return 0;
                }
            }

            @Override
            public void set(int i, int value) {

                switch (i) {

                    case 0:

                        ManualMillEntity.this.workTime = value;
                        break;
                    case 1:
                        ManualMillEntity.this.workTimeTotal = value;
                        break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {

    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.fromTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return this.toTag(tag);
    }
}
