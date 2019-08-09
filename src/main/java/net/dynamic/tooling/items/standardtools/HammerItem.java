package net.dynamic.tooling.items.standardtools;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class HammerItem extends PickaxeItem {

    public HammerItem( int miningLevel, float attackSpeed, ToolMaterial material, Settings settings ) {
        super(material, miningLevel, attackSpeed,  settings);


    }

    @Override
    public boolean canMine(BlockState blockState, World world, BlockPos pos, PlayerEntity player) {

        /**
         * TODO:  Refactor all of the important bits and pieces out into its own util class!
         */

        if (world.isClient)
            return true;

        ItemStack mainHandStack = player.getMainHandStack();
        if (player.isSneaking() || !mainHandStack.isEffectiveOn(blockState)) { return true; }

        return ToolUtils.mineBlocksAOE( blockState, world, pos, player, 3, 3, this);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> list, TooltipContext tooltipContext) {

        ToolMaterial mat = this.getMaterial();

        int durability = mat.getDurability();
        int miningLevel = mat.getMiningLevel();
        float miningSpeed = mat.getMiningSpeed();

        list.add(new TranslatableText("text.dynamictooling.durability", durability));
        list.add(new TranslatableText("text.dynamictooling.mining_level", miningLevel));


    }
}
