package net.dynamic.tooling.blocks.lawnblocks;

import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;

public class LawnLightSlabBlock extends SlabBlock {

    public LawnLightSlabBlock(Settings settings) {
        super(settings);
    }


    @Override
    public int getLuminance(BlockState blockState_1) {
        return 15;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }


}
