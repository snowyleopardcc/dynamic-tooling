package net.dynamic.tooling.blocks;

import net.dynamic.tooling.blocks.entities.ManualMillEntity;
import net.dynamic.tooling.items.standardtools.ToolUtils;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

public class FlyWheelBlock extends Block {
    public FlyWheelBlock(Settings settings) {
        super(settings);

        this.setDefaultState((BlockState)((BlockState)this.stateFactory.getDefaultState()).with(FACING, Direction.NORTH));
    }

    public static final DirectionProperty FACING;

    static {

        FACING = HorizontalFacingBlock.FACING;
    }


    @Override
    public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {

        if(world.isClient() || playerEntity.isSneaking()) { return false; }

        Direction dir = blockState.get(FACING);
        BlockPos pos = blockPos.offset(dir);

        BlockEntity be = world.getBlockEntity(pos);

        if( be instanceof ManualMillEntity ) {

            playerEntity.swingHand(hand);

//            ((ManualMillEntity) be).grind();
        }

        return true;
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory) {

        stateFactory.add(new Property[]{FACING});
    }

    @Override
    public BlockRenderLayer getRenderLayer() {

        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {

        BlockState state = this.getDefaultState();

        BlockHitResult result = ToolUtils.getHitResult(itemPlacementContext.getPlayer(), itemPlacementContext.getWorld());

        Direction newFacing = result.getSide().getOpposite();

        if(newFacing != Direction.DOWN && newFacing != Direction.UP) {

            return state.with(FACING, newFacing);
        }
        else {

            return this.getDefaultState();
        }
    }

    @Override
    public boolean canPlaceAt(BlockState blockState, ViewableWorld viewableWorld, BlockPos blockPos) {

        Direction dir = (Direction)blockState.get(FACING);
        BlockPos pos = blockPos.offset(dir);

        BlockEntity be = viewableWorld.getBlockEntity(pos);
        Block b = viewableWorld.getBlockState(pos).getBlock();

        return (be instanceof ManualMillEntity || b instanceof ManualMillBlock);
    }
}
