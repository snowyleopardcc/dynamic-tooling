package net.dynamic.tooling.blocks;

import net.dynamic.tooling.blocks.entities.PressBenchEntity;
import net.dynamic.tooling.items.press_plates.BlankPressPlate;
import net.dynamic.tooling.items.standardtools.ToolUtils;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PressBenchBlock extends Block implements BlockEntityProvider {


    public PressBenchBlock() {
        super(FabricBlockSettings.of(Material.METAL).build());

        this.setDefaultState(((BlockState)this.stateFactory.getDefaultState()).with(FACING, Direction.NORTH));
    }


    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity entity, Hand hand, BlockHitResult blockHitResult) {

        BlockEntity be = world.getBlockEntity(pos);

        if ( be instanceof PressBenchEntity ) {

            PressBenchEntity pbe = (PressBenchEntity)be;

            if ( entity.getMainHandStack().getItem() instanceof BlankPressPlate) {

                System.out.println("Found a blank plate!");

                    pbe.setPlate(entity.getMainHandStack().copy());

                    ItemStack stack = entity.getMainHandStack();
                    stack.decrement(1);

                    world.updateListeners(pos, state, state,3);
            }
            else if ( entity.getMainHandStack().isEmpty() && !pbe.getPlate().isEmpty() ) {

                int slot = entity.inventory.getEmptySlot();

                entity.inventory.setInvStack(slot, pbe.getPlate());

                pbe.setPlate(ItemStack.EMPTY);
            }
            else {

                return false;
            }

            return true;
        }

//        return super.activate(state, world, pos, entity, hand, blockHitResult);
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT; }


    public static final DirectionProperty FACING;



    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {

        return (BlockState)this.getDefaultState().with(FACING, itemPlacementContext.getPlayerFacing());
    }


    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory) {

        stateFactory.add(new Property[]{FACING});
    }


    static {

        FACING = HorizontalFacingBlock.FACING;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {

        return new PressBenchEntity();
    }
}
