package net.dynamic.tooling.blocks.renderers;

import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.mojang.blaze3d.platform.GlStateManager;
import net.dynamic.tooling.blocks.entities.LiquidFurnaceEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class LiquidFurnaceRenderer extends BlockEntityRenderer<LiquidFurnaceEntity> {

    @Override
    public void render(LiquidFurnaceEntity lfe, double x, double y, double z, float float_1, int int_1) {
//        super.render(blockEntity_1, double_1, double_2, double_3, float_1, int_1);

        FluidVolume fluid = lfe.FLUID_INVENTORY.getTank(0).get();

        if(!fluid.isEmpty()) {


            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();

            MinecraftClient.getInstance().getTextureManager().bindTexture(fluid.getSprite());

            List<FluidRenderFace> faces = new ArrayList<>();

            double base = 1.0 / 16.0;
            double offset = (0.68 * 16) / 16;

            double x0 = base;
            double y0 = offset;
            double z0 = base;
            double x1 = 1.0 - base;
            double y1 = (((offset + ((1.0 - offset) * fluid.getAmount()) / lfe.FLUID_INVENTORY.getTank(0).getMaxAmount())) - base);
            double z1 = 1.0 - base;

            EnumSet<Direction> sides = EnumSet.allOf(Direction.class);
            FluidRenderFace.appendCuboid(x0, y0, z0, x1, y1, z1, 16, sides, faces);
            fluid.getRenderer().render(fluid, faces, x, y, z);

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
