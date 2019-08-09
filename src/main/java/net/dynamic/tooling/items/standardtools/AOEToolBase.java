package net.dynamic.tooling.items.standardtools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public abstract class AOEToolBase extends MiningToolItem {

    protected AOEToolBase(
            float attackDamage,
            float attackSpeed,
            ToolMaterial material,
            Set<Block> effectiveBlocks,
            Settings settings
    ) {
        super(attackDamage, attackSpeed, material, effectiveBlocks ,settings);


        addPropertyGetter(new Identifier("dynamictooling:is_broken"), (stack, world, entity) -> {

            return ((BROKEN)) ? 1.0f : 0.0f;
        });
    }
    public static Boolean BROKEN = false;

}
