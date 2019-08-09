package net.dynamic.tooling.items.standardtools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ToolUtils {

    /**
     * @param instance -> Type of mining tool to use to compare set of effective blocks against.
     * @param pos      -> Initial Position to start from.
     * @param world    -> world instance.
     * @param player   -> player instance.
     * @return -> map of positions to blocks to use for tools to act on.
     */

    public static boolean mineBlocksAOE(BlockState blockState, World world, BlockPos pos, PlayerEntity player, int miningWidth, int miningHeight, MiningToolItem instance) {

        BlockHitResult hitResult = ToolUtils.getHitResult(player, world);

        Direction.Axis axis = hitResult.getSide().getAxis();

        /**
         * Determining Axis's for use in block position offsets.
         */
        Direction.Axis hAxis = (axis == Direction.Axis.Z || (axis == Direction.Axis.Y && (player.getHorizontalFacing() == Direction.NORTH || player.getHorizontalFacing() == Direction.SOUTH))) ? Direction.Axis.X : Direction.Axis.Z;

        Direction.Axis vAxis = (axis == Direction.Axis.Y && (player.getHorizontalFacing() == Direction.EAST || player.getHorizontalFacing() == Direction.WEST)) ? Direction.Axis.X
                : (axis == Direction.Axis.X || axis == Direction.Axis.Z) ? Direction.Axis.Y : Direction.Axis.Z;


        int width = ((miningWidth - 1) > 0) ? ((miningWidth - 1) / 2) : 0;
        int height = ((miningHeight - 1) > 0) ? ((miningHeight - 1) / 2) : 0;

        /**
         * h and v are horizontal and vertical coords respectively.
         */
        for (int h = -width; h <= width; h++) {
            for (int v = -height; v <= height; v++) {
                if (h != 0 || v != 0) {

                    /** check if there is enough durability left to continue mining. */
                    if( (ToolUtils.getActualDurability(player.getMainHandStack(), instance.getMaterial())) - 1 <= 1 ) { break; }

                    BlockPos newPos = pos.offset(getDirection(hAxis, h), Math.abs(h)).offset(getDirection(vAxis, v), Math.abs(v));

                    BlockState newState = world.getBlockState(newPos);

                    if (!instance.isEffectiveOn(newState)) { continue; }

                    // Let's break the block!
                    world.breakBlock(newPos, false);

                    /**
                     * Here we are grouping block drops together,  so as to minimize the mess and fuss!
                     * Who knows,  maybe it'll even help with server lag a bit even?
                     */
                    if (!player.isCreative()) {

                        Block.dropStacks(newState, world, pos, blockState.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null);
                    }

                    player.getMainHandStack().damage(1, player, (playerEntity_1x) -> {

                        playerEntity_1x.sendToolBreakStatus(player.getActiveHand());
                    });
                }
            }
        }

        return true;
    }


    public static boolean stripBlocksAOE(int miningWidth, int miningHeight, ItemUsageContext context, AdzeItem instance) {

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState blockState = world.getBlockState(pos);

        PlayerEntity player = context.getPlayer();

        BlockHitResult hitResult = ToolUtils.getHitResult(player, world);

        Direction.Axis axis = hitResult.getSide().getAxis();

        /**
         * Determining Axis's for use in block position offsets.
         */
        Direction.Axis hAxis = (axis == Direction.Axis.Z || (axis == Direction.Axis.Y && (player.getHorizontalFacing() == Direction.NORTH || player.getHorizontalFacing() == Direction.SOUTH))) ? Direction.Axis.X : Direction.Axis.Z;

        Direction.Axis vAxis = (axis == Direction.Axis.Y && (player.getHorizontalFacing() == Direction.EAST || player.getHorizontalFacing() == Direction.WEST)) ? Direction.Axis.X
                : (axis == Direction.Axis.X || axis == Direction.Axis.Z) ? Direction.Axis.Y : Direction.Axis.Z;


        int width = ((miningWidth - 1) > 0) ? ((miningWidth - 1) / 2) : 0;
        int height = ((miningHeight - 1) > 0) ? ((miningHeight - 1) / 2) : 0;

        player.swingHand(player.getActiveHand());
        /**
         * h and v are horizontal and vertical coords respectively.
         */
        for (int h = -width; h <= width; h++) {
            for (int v = -height; v <= height; v++) {


                /** check if there is enough durability left to continue mining. */
                if( (ToolUtils.getActualDurability(player.getMainHandStack(), instance.getMaterial())) - 1 <= 1 ) { break; }

                BlockPos newPos = pos.offset(getDirection(hAxis, h), Math.abs(h)).offset(getDirection(vAxis, v), Math.abs(v));

                BlockState newState = world.getBlockState(newPos);

                if ( !instance.STRIPPABLE_BLOCKS.containsKey(newState.getBlock()) ) { continue; }

                Block stripped = instance.STRIPPABLE_BLOCKS.get(newState.getBlock());

                world.setBlockState(newPos, stripped.getDefaultState().with(PillarBlock.AXIS, newState.get(PillarBlock.AXIS)), 11);

                world.playSound(player, newPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);

                player.getMainHandStack().damage(1, player, (playerEntity_1x) -> {

                    playerEntity_1x.sendToolBreakStatus(player.getActiveHand());
                });
            }
        }

        return true;
    }


    private static Direction getDirection(Direction.Axis axis, int offset) {

        if (axis == Direction.Axis.X) {

            return (offset < 0) ? Direction.WEST : Direction.EAST;
        } else if (axis == Direction.Axis.Y) {

            return (offset < 0) ? Direction.DOWN : Direction.UP;
        } else {

            return (offset < 0) ? Direction.NORTH : Direction.SOUTH;
        }
    }

    public static BlockHitResult getHitResult(PlayerEntity player, World world) {

        // Taken from Entity#rayTrace
        Vec3d vec3d_1 = player.getCameraPosVec(1);
        Vec3d vec3d_2 = player.getRotationVec(1);

        int range = 4;

        Vec3d vec3d_3 = vec3d_1.add(vec3d_2.x * range, vec3d_2.y * range, vec3d_2.z * range);
        BlockHitResult hitResult = world.rayTrace(new RayTraceContext(vec3d_1, vec3d_3, RayTraceContext.ShapeType.OUTLINE, true ? RayTraceContext.FluidHandling.ANY : RayTraceContext.FluidHandling.NONE, player));

        return hitResult;
    }



    public static List<Text> createTooltips (ItemStack tool, ToolMaterial material) {

        List<Text> list = new ArrayList<Text>();

        int damage = tool.getDamage();
        int durability = (material.getDurability() - damage);
        int health = (int)Math.floor((((float)durability) / material.getDurability()) * 100);

        int miningLevel = material.getMiningLevel();
        float miningSpeed = material.getMiningSpeed();

        list.add(new TranslatableText("text.dynamictooling.durability", durability));
        list.add(new TranslatableText("text.dynamictooling.mining_level", miningLevel));
        list.add(new TranslatableText("text.dynamictooling.damage", damage));
        list.add(new TranslatableText("text.dynamictooling.mining_speed", miningSpeed));
        list.add(new TranslatableText("text.dynamictooling.tool_health", health));

        return list;
    }


    public static int getActualDurability(ItemStack tool, ToolMaterial material) {

        return ( material.getDurability() - tool.getDamage() );
    }


}
