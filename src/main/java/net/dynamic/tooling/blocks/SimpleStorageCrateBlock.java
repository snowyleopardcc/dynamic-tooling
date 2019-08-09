package net.dynamic.tooling.blocks;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import net.dynamic.tooling.blocks.entities.SimpleStorageCrateBlockEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class SimpleStorageCrateBlock extends Block implements AttributeProvider, BlockEntityProvider, InventoryProvider {


    private World world;
    private BlockPos pos;
    private BlockState blockState;
    private PlayerEntity player;
    private Hand hand;
    private BlockHitResult hitResult;
    private AttributeList<?> to;
    private PlayerEntity Player;

    public static final Identifier CRATE_ID = new Identifier("dynamictooling", "simple_storage_crate_block");

    public SimpleStorageCrateBlock(Settings settings) {

        super(settings);

    }


    public static final TranslatableText SIMPLE_STORAGE_CRATE_BLOCK = new TranslatableText("Simple Storage Crate");

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {

        return new SimpleStorageCrateBlockEntity();
    }


    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {

        if (world.isClient) return true;

        BlockEntity be = world.getBlockEntity(pos);

        if (be != null && be instanceof SimpleStorageCrateBlockEntity) {

            ContainerProviderRegistry.INSTANCE.openContainer(CRATE_ID, player, (buf) -> buf.writeBlockPos(pos));
        }

        return true;
    }

    @Override
    public void addAllAttributes(World world, BlockPos blockPos, BlockState blockState, AttributeList<?> attributeList) {


        BlockEntity entity = world.getBlockEntity(blockPos);

        if (entity instanceof SimpleStorageCrateBlockEntity) {

            to.offer(((SimpleStorageCrateBlockEntity) entity).inventory);
        }

    }


    @Override
    public SidedInventory getInventory(BlockState state, IWorld world, BlockPos pos) {

        BlockEntity be = world.getBlockEntity(pos);

        if(be !=null && be instanceof SimpleStorageCrateBlockEntity) {

            return ((SimpleStorageCrateBlockEntity)be).invComponent;
        }
        else {

            return null;
        }
    }
}
