package net.dynamic.tooling.items.standardtools;

import net.dynamic.tooling.DynamicTooling;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import java.util.List;

public class MinersDelightItem extends PickaxeItem  {

    public MinersDelightItem(int miningLevel, float attackSpeed, ToolMaterial material, Settings settings ) {

        super(material, 2, attackSpeed, settings);


        addPropertyGetter(new Identifier("dynamictooling:is_broken"), (stack, world, entity) -> {

            return ((BROKEN)) ? 1.0f : 0.0f;
        });

//        FishingRodItem
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

        return ToolUtils.mineBlocksAOE( blockState, world, pos, player, 1, 3, this);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext) {

        Vec3d vec3d_1 = itemUsageContext.getPlayer().getCameraPosVec(1);
        Vec3d vec3d_2 = itemUsageContext.getPlayer().getRotationVec(1);
        int range = 4;
        Vec3d vec3d_3 = vec3d_1.add(vec3d_2.x * range, vec3d_2.y * range, vec3d_2.z * range);

        BlockHitResult hitResult = itemUsageContext.getWorld().rayTrace(new RayTraceContext(vec3d_1, vec3d_3, RayTraceContext.ShapeType.OUTLINE, true ? RayTraceContext.FluidHandling.ANY : RayTraceContext.FluidHandling.NONE, itemUsageContext.getPlayer()));

        Direction dir = hitResult.getSide();

        BlockPos pos = hitResult.getBlockPos().offset(dir);

        if(itemUsageContext.getWorld().getBlockState(pos).isAir()) {

            /**
             * If the direction is up,  we're placing a torch block on the floor,
             * else we must be placing a wall torch somewhere on a wall surface.
             */
            if( dir == Direction.UP ) {

                itemUsageContext.getWorld().setBlockState(pos, Blocks.TORCH.getDefaultState());
            }
            else if( dir != Direction.DOWN ) {

                BlockRotation rotation = (dir == Direction.EAST) ? BlockRotation.CLOCKWISE_90 : ( dir == Direction.WEST) ? BlockRotation.COUNTERCLOCKWISE_90 : ( dir == Direction.SOUTH) ? BlockRotation.CLOCKWISE_180 : BlockRotation.NONE;
                itemUsageContext.getWorld().setBlockState(pos, Blocks.WALL_TORCH.getDefaultState().rotate(rotation));
            }

            itemUsageContext.getPlayer().getMainHandStack().damage(1, itemUsageContext.getPlayer(), (playerEntity_1x) -> {

                playerEntity_1x.sendToolBreakStatus(itemUsageContext.getPlayer().getActiveHand());
            });
        }

        return ActionResult.SUCCESS;
    }


    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> list, TooltipContext tooltipContext) {

        list.addAll(ToolUtils.createTooltips(itemStack, this.getMaterial()));
        list.add(new TranslatableText("text.dynamictooling.md_tooltip"));
    }


}
