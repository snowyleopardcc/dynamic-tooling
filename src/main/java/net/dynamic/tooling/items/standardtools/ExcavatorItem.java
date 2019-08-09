package net.dynamic.tooling.items.standardtools;

import net.dynamic.tooling.DynamicTooling;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.KnockbackEnchantment;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ExcavatorItem extends ShovelItem {


    private final Set<Block> PATHABLE_BLOCKS;

    public ExcavatorItem(ToolMaterial material, float attackSpeed, float attackDamage, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);

        this.PATHABLE_BLOCKS = EffectiveBlocksUtils.PATHABLE_BLOCKS;

        addPropertyGetter(new Identifier(DynamicTooling.MOD_ID, "is_broken"), (stack, world, entity) -> {

            return ((BROKEN)) ? 1.0f : 0.0f;
        });
    }

    public static Boolean BROKEN = false;

    @Override
    public boolean canMine(BlockState blockState, World world, BlockPos pos, PlayerEntity player) {

        /**
         * TODO:  Refactor all of the important bits and pieces out into its own util class!
         */


        if (world.isClient)
            return true;

        ItemStack mainHandStack = player.getMainHandStack();
        if (player.isSneaking() || !mainHandStack.isEffectiveOn(blockState)) { return true; }

        ToolUtils.mineBlocksAOE(blockState, world, pos, player, 3, 3, this);

        return true;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext) {

        World world = itemUsageContext.getWorld();

        if(world.isClient) { return ActionResult.PASS; }

        if (itemUsageContext.getSide() == Direction.UP) {

            PlayerEntity player = itemUsageContext.getPlayer();
            ItemStack mainHandStack = player.getMainHandStack();


            BlockHitResult hitResult = ToolUtils.getHitResult(player, itemUsageContext.getWorld());

            BlockPos pos = hitResult.getBlockPos();

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    BlockPos newPos = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
                    BlockState blockState = world.getBlockState(newPos);

                    /**
                     * TODO:  Implement durability system...
                     * TODO:  Add exception for grass along with air block check...
                     */
                    //if (mainHandStack.getDamage() <= 0 ) { return ActionResult.PASS; }

                    if ( !world.getBlockState(newPos.up()).isAir() || !PATHABLE_BLOCKS.contains(blockState.getBlock()) ) { continue; }

                    world.setBlockState(newPos, Blocks.GRASS_PATH.getDefaultState(), 11);

                    world.playSound(player, newPos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    mainHandStack.damage(1, player, (playerEntity_1x) -> {

                        playerEntity_1x.sendToolBreakStatus(player.getActiveHand());
                    });
                }
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean isEffectiveOn(BlockState blockState) {

        /**
         * What are ya doing to us,  Mojang?!
         */
        return EffectiveBlocksUtils.EXCAVATOR_EFFECTIVE_BLOCKS.contains(blockState.getBlock());
    }


    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> list, TooltipContext tooltipContext) {

        ToolMaterial mat = this.getMaterial();
        list.addAll(ToolUtils.createTooltips(itemStack, mat));
    }
}
