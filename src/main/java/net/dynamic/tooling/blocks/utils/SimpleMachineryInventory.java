package net.dynamic.tooling.blocks.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.math.Direction;

import java.util.stream.IntStream;

public class SimpleMachineryInventory extends BasicInventory implements SidedInventory, RecipeInputProvider {

    public SimpleMachineryInventory(int topSlots, int bottomSlots) {

        super(topSlots + bottomSlots);

        this.INPUT_SLOTS_RANGE = IntStream.range(0, topSlots).toArray();
        this.OUTPUT_SLOTS_RANGE = IntStream.range(INPUT_SLOTS_RANGE.length, (INPUT_SLOTS_RANGE.length + bottomSlots )).toArray();
    }

    private final int[] INPUT_SLOTS_RANGE;
    private final int[] OUTPUT_SLOTS_RANGE;

    public int[] getInputSlots() { return INPUT_SLOTS_RANGE; }
    public int[] getOutputSlots() { return  OUTPUT_SLOTS_RANGE; }

    /**
     * Top slot accepts one stack,
     * The bottom inv is 6 slots (3 x 2).
     * We're only inserting into the top,
     * and extracting out the bottom...
     */


    public int getOutputSlot(ItemStack output) {

        int emptySlot = -1;
        int matchedSlot = -1;
        /**
         *  Cycle through each of the output slots,
         *  taking note of the first empty one,
         *  while looking for the first possible
         *  match to the output stack / item / ingredient.
         */
        for (int slot : getOutputSlots()) {

            ItemStack current = getInvStack(slot);

            if (!current.isEmpty()) {

                if ((current.getItem().equals(output.getItem()))) {

                    matchedSlot = slot;
                    break;
                }
            }
            else {

                if( emptySlot == -1) { emptySlot = slot; }
            }
        }

        return  (matchedSlot != -1) ? matchedSlot : emptySlot;
    }



    @Override
    public boolean canPlayerUseInv(PlayerEntity playerEntity_1) { return true; }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {

        if(direction == Direction.UP) { return INPUT_SLOTS_RANGE; }
        else if( direction == Direction.DOWN ) { return OUTPUT_SLOTS_RANGE; }
        else { return new int[0]; }
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, Direction direction) {

        return (direction == Direction.UP);
    }

    @Override
    public boolean canExtractInvStack(int i, ItemStack itemStack, Direction direction) {

        return (direction == Direction.DOWN);
    }

    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder_1) {

        recipeFinder_1.addNormalItem(getInvStack(0));
    }
}
