package net.dynamic.tooling.blocks.lawnblocks;


import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class LawnLightBlock extends Block implements BlockEntityProvider, RenderAttachmentBlockEntity {

    public LawnLightBlock(Settings block$Settings_1) {
        super(block$Settings_1);

    }





    @Override
    public int getLuminance(BlockState blockState_1) {
        return 15;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }



    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new LawnLightBlockEntity();
    }

    @Override
    public BlockState getRenderAttachmentData() {
        return Blocks.GRASS.getDefaultState();
    }
}
