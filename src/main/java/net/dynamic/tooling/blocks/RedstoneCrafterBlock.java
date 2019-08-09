package net.dynamic.tooling.blocks;

import net.dynamic.tooling.DynamicTooling;
import net.dynamic.tooling.blocks.entities.ManualMillEntity;
import net.dynamic.tooling.blocks.entities.RedstoneCrafterEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
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

public class RedstoneCrafterBlock extends Block implements BlockEntityProvider, InventoryProvider {


    public RedstoneCrafterBlock(Settings settings) {
        super(settings);


        this.setDefaultState((BlockState)this.getDefaultState().with(ON, true));
    }


    public static final BooleanProperty ON = BooleanProperty.of("false");


    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {

        return new RedstoneCrafterEntity();
    }

    @Override
    public int getLuminance(BlockState state) {
        return (Boolean)state.get(ON) ? 15 : 0;
    }


    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {

        if (world.isClient) return true;

        BlockEntity be = world.getBlockEntity(pos);

        if (be != null && be instanceof RedstoneCrafterEntity) {

            ContainerProviderRegistry.INSTANCE.openContainer(DynamicTooling.RSC_ID, player, (buf) -> buf.writeBlockPos(pos));
        }

        return true;
    }


    @Override
    public SidedInventory getInventory(BlockState blockState, IWorld world, BlockPos blockPos) {

        BlockEntity be = world.getBlockEntity(blockPos);

        if(be instanceof RedstoneCrafterEntity) {

            return ((RedstoneCrafterEntity)be).INVENTORY;
        }
        else {

            return null;
        }
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactoryBuilder) {

        stateFactoryBuilder.add(new Property[]{ON});
    }
}
