package net.dynamic.tooling.blocks.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.stream.IntStream;

public class CraftableStorageInventory implements SidedInventory, RecipeInputProvider {


    public CraftableStorageInventory(int inputSlots, int outputSlots) {
//        super(1 + inputSlots + outputSlots);

        stacks = DefaultedList.ofSize( 1 + inputSlots + outputSlots, ItemStack.EMPTY);

        /** See notes below... */

        INPUT_SLOTS_RANGE = IntStream.range(1, 1 + inputSlots).toArray();
        OUTPUT_SLOTS_RANGE = IntStream.range(1 + INPUT_SLOTS_RANGE.length, (1 + inputSlots + outputSlots) ).toArray();
    }

    /**
     *
     * Slots 0-8 are reserved for crafting input grid.
     * Slot 9 is reserved for the crafting results slot.
     *
     * The top slots index range is the possible slots
     * for imputing items in.
     *
     * The bottom slots index range is the possible slots
     * items can be extracted from.
     */


    protected final DefaultedList<ItemStack> stacks;
    public FakeInventory CRAFTING_INVENTORY = new FakeInventory();


    public final int[] INPUT_SLOTS_RANGE;
    public final int[] OUTPUT_SLOTS_RANGE;


    public DefaultedList<ItemStack> getStacks() { return stacks; }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {

        if ( direction == Direction.DOWN ) { return OUTPUT_SLOTS_RANGE; }
        else { return INPUT_SLOTS_RANGE; }
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, Direction direction) {

        return (direction != Direction.DOWN);
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack itemStack, Direction direction) {

        return (direction == Direction.DOWN );
    }


    @Override
    public int getInvSize() {
        return stacks.size();
    }

    @Override
    public boolean isInvEmpty() {
        boolean isEmpty = true;

        for(ItemStack stack : stacks) {

            if(!stack.isEmpty()) {

                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    @Override
    public ItemStack getInvStack(int slot) {

        return (slot >= 0 && slot < stacks.size()) ? (ItemStack)stacks.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack takeInvStack(int slot, int ammount) {

        ItemStack stack = Inventories.splitStack(stacks, slot, ammount);

        if (!stack.isEmpty()) {

            this.markDirty();
        }

        return stack;
    }

    @Override
    public ItemStack removeInvStack(int slot) {

        ItemStack stack = (ItemStack)stacks.get(slot);

        if (stack.isEmpty()) {

            return ItemStack.EMPTY;
        } else {

            stacks.set(slot, ItemStack.EMPTY);
            return stack;
        }
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {

        stacks.set(slot, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInvMaxStackAmount()) {

            stack.setCount(this.getInvMaxStackAmount());
        }

        this.markDirty();
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity playerEntity) {
        return true;
    }

    public void takeItemStack(ItemStack stack) {

        int total = stack.getCount();

        for(int slot : INPUT_SLOTS_RANGE) {

            ItemStack wtf = getInvStack(slot);

            if(!wtf.isEmpty() && (wtf.getItem() == stack.getItem())) {

                ItemStack current = getInvStack(slot);

                int count = current.getCount();

                if(count >= total) {

                    takeInvStack(slot, total);
                    break;
                }
                else {

                    total -= count;
                    takeInvStack(slot, total);
                }
                markDirty();
            }
        }
    }


    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {

        for(int idx = 0; idx < 9; idx++) {

            recipeFinder.addNormalItem( CRAFTING_INVENTORY.getInvStack(idx) );
        }
    }


    public boolean hasIngredient(ItemStack ingredient) {

        boolean isFound = false;

        for (int slot : INPUT_SLOTS_RANGE) {

            if((getInvStack(slot).getItem() == ingredient.getItem())  && (getInvStack(slot).getCount() >= ingredient.getCount())) {

                isFound = true;
                break;
            }
        }

        return  isFound;
    }


    public DefaultedList<ItemStack> getIngredients() {

        /** We don't have access to the recipe ingredients list,
         *  (fuck you for this MC) so we have to rely on the
         *  copy we make from the GUI...
         */
        DefaultedList<ItemStack> temp = DefaultedList.ofSize(9, ItemStack.EMPTY);

        /** go through crafting grid ingredients */
        for(int idx = 0; idx < 9; idx++) {

            /** get copy of current ingredient */
            ItemStack stack = CRAFTING_INVENTORY.getInvStack(idx).copy();

            if(!stack.isEmpty() || !(stack.getItem() == Items.AIR)) {

                /** scan slots adding and consolidating as we go */
                for (int i = 0; i < temp.size(); i++) {
                    if (temp.get(i).isEmpty()) {
                        temp.set(i, stack);
                        break;
                    } else if (temp.get(i).getItem() == stack.getItem()) {
                        temp.get(i).increment(1);
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }

        return  temp;
    }

    public void fromTag(CompoundTag tag) {

//        ListTag slots = tag.getList("slots", new CompoundTag().getType());
//
//        for(int idx = 0; idx < slots.size(); idx ++ ) {
//
//            setInvStack(idx, ItemStack.fromTag(slots.getCompoundTag(idx)));
//        }

        ListTag crafting = tag.getList("crafting", new CompoundTag().getType());

        for(int jdx = 0; jdx < crafting.size(); jdx ++ ) {

            CRAFTING_INVENTORY.setInvStack(jdx, ItemStack.fromTag(crafting.getCompoundTag(jdx)));
        }
    }

    public CompoundTag toTag(CompoundTag tag) {

//        ListTag slots = new ListTag();

//        for(int idx = 0; idx < getInvSize(); idx++) {
//
//            slots.add(getInvStack(idx).toTag(new CompoundTag()));
//        }

        ListTag crafting = new ListTag();

        for(int jdx = 0; jdx < CRAFTING_INVENTORY.getInvSize(); jdx++) {

            crafting.add(getInvStack(jdx).toTag(new CompoundTag()));
        }


//        tag.put("slots", slots);
        tag.put("crafting", crafting);

        return tag;
    }

    @Override
    public void clear() {

    }
}
