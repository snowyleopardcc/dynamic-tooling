package net.dynamic.tooling.blocks.utils;

import net.minecraft.container.Container;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.util.DefaultedList;

import java.util.Iterator;

public class FakeInventory extends CraftingInventory {

    public FakeInventory() {
        super(null, 3, 3);

        this.fake = DefaultedList.ofSize(9, ItemStack.EMPTY);
    }

    protected DefaultedList<ItemStack> fake;

    @Override
    public ItemStack getInvStack(int int_1) {

        return int_1 >= this.getInvSize() ? ItemStack.EMPTY : (ItemStack)this.fake.get(int_1);
    }

    @Override
    public ItemStack takeInvStack(int int_1, int int_2) {

        ItemStack stack = Inventories.splitStack(fake, int_1, int_2);

        return stack;
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {

        fake.set(slot, stack);
    }

    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {

        Iterator itr = fake.iterator();

        while(itr.hasNext()) {
            ItemStack itemStack_1 = (ItemStack)itr.next();
            recipeFinder.addNormalItem(itemStack_1);
        }
    }

    @Override
    public void clear() {

        for(int idx = 0; idx < fake.size(); idx ++) {

            fake.set(idx, ItemStack.EMPTY);
        }
    }
}
