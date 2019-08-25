package net.dynamic.tooling.blocks;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.dynamic.tooling.DynamicTooling;
import net.dynamic.tooling.blocks.entities.LiquidFurnaceEntity;
import net.dynamic.tooling.blocks.entities.ManualMillEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LiquidFurnaceBlock extends Block implements BlockEntityProvider, AttributeProvider {

    public LiquidFurnaceBlock() {
        super(FabricBlockSettings.of(Material.METAL).hardness(10.0f).build());


    }

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {

        return new LiquidFurnaceEntity();
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {

        BlockEntity be = world.getBlockEntity(pos);

        if (be instanceof LiquidFurnaceEntity) {

            LiquidFurnaceEntity entity = (LiquidFurnaceEntity) be;
            to.offer(entity);
        }
    }


    @Override
    public boolean activate(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {

        if (world.isClient) { return true; }

            BlockEntity be = world.getBlockEntity(pos);

            if (be instanceof LiquidFurnaceEntity) {

                LiquidFurnaceEntity lfe = (LiquidFurnaceEntity) be;
                if (!lfe.onActivate(player, hand)) {

                    ContainerProviderRegistry.INSTANCE.openContainer(DynamicTooling.FURNACE_ID, player, (buf) -> buf.writeBlockPos(pos));
                }
                else {

                    world.updateListeners(pos, blockState, blockState,3);
                    return false;
                }
            }

        return true;
    }
}
