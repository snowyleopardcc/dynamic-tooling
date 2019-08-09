package net.dynamic.tooling.blocks;

import net.dynamic.tooling.DynamicTooling;
import net.dynamic.tooling.blocks.entities.ManualMillEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ShulkerBoxBlockEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class ManualMillBlock extends Block implements BlockEntityProvider, InventoryProvider {

    public ManualMillBlock(Settings settings) {
        super(settings);

        this.setDefaultState((BlockState)this.getDefaultState().with(ON, true));
    }

    public static BooleanProperty ON = BooleanProperty.of("false");

    @Override
    public int getLuminance(BlockState state) {
        return (Boolean)state.get(ON) ? 15 : 0;
    }

    @Override
    public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos1, Block block, BlockPos blockPos2, boolean bool) {

        if (!world.isClient) {

            boolean isOn = (Boolean)blockState.get(ON);

            if (isOn != world.isReceivingRedstonePower(blockPos1)) {

                if (isOn) {

                    world.getBlockTickScheduler().schedule(blockPos1, this, 4);
                } else {

                    world.setBlockState(blockPos1, (BlockState)blockState.cycle(ON), 2);
                }
            }

        }
    }

    @Override
    public void onScheduledTick(BlockState blockState, World world, BlockPos blockPos, Random random) {

        if (!world.isClient) {

            if ((Boolean)blockState.get(ON) && !world.isReceivingRedstonePower(blockPos)) {

                world.setBlockState(blockPos, (BlockState)blockState.cycle(ON), 2);
            }
        }
    }

    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {

        return (BlockState)this.getDefaultState().with(ON, itemPlacementContext.getWorld().isReceivingRedstonePower(itemPlacementContext.getBlockPos()));
    }


    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {

        return new ManualMillEntity();
    }

    @Override
    public SidedInventory getInventory(BlockState blockState, IWorld world, BlockPos blockPos) {

        BlockEntity be = world.getBlockEntity(blockPos);

        if(be instanceof ManualMillEntity) {

            return ((ManualMillEntity)be).INVENTORY;
        }
        else {

            return null;
        }
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {

        if (world.isClient) return true;

        BlockEntity be = world.getBlockEntity(pos);

        if (be != null && be instanceof ManualMillEntity) {

            if(player.isSneaking()) {

                ContainerProviderRegistry.INSTANCE.openContainer(DynamicTooling.MILL_ID, player, (buf) -> buf.writeBlockPos(pos));
            }
            else {

                ((ManualMillEntity)be).pulse();
            }
        }

        return true;
    }


    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactoryBuilder) {

        stateFactoryBuilder.add(new Property[]{ON});
    }
}
