package net.dynamic.tooling.items.standardtools;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.dynamic.tooling.DynamicTooling;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdzeItem extends AxeItem {

    public AdzeItem( float attackDamage, float attackSpeed, ToolMaterial material,Settings settings) {
        super(material, attackSpeed, attackDamage, settings);


        addPropertyGetter(new Identifier(DynamicTooling.MOD_ID, "is_broken"), (stack, world, entity) -> {

            return ((BROKEN)) ? 1.0f : 0.0f;
        });
    }

    public static Boolean BROKEN = false;


    @Override
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext) {

        World world = itemUsageContext.getWorld();

        if(world.isClient) { return ActionResult.PASS; }

        boolean result = ToolUtils.stripBlocksAOE( 3, 3, itemUsageContext, this);
        return (result) ? (ActionResult.SUCCESS) : ActionResult.PASS;
    }


    public static final Map<Block, Block> STRIPPABLE_BLOCKS;

    static {

        STRIPPABLE_BLOCKS = (new ImmutableMap.Builder()).put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).build();
    }


    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> list, TooltipContext tooltipContext) {

        ToolMaterial mat = this.getMaterial();
        list.addAll(ToolUtils.createTooltips(itemStack, mat));
        list.add(new TranslatableText("text.dynamictooling.adze_tooltip"));
    }
}
