package net.dynamic.tooling.items.standardtools;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class HardwoodMaterial implements ToolMaterial {
    @Override
    public int getDurability() {
        return 0;
    }

    @Override
    public float getMiningSpeed() {
        return 2.0f;
    }

    @Override
    public float getAttackDamage() {
        return 4.0f;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }
}
