package net.dynamic.tooling.blocks.lawnblocks;

import net.dynamic.tooling.DynamicTooling;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;

import java.util.HashMap;

public class LightModelVariantProvider implements ModelVariantProvider {
    @Override
    public UnbakedModel loadModelVariant(ModelIdentifier modelIdentifier, ModelProviderContext modelProviderContext) throws ModelProviderException {

        return variants.get(modelIdentifier);
    }

    private final HashMap<ModelIdentifier, UnbakedModel> variants = new HashMap<ModelIdentifier, UnbakedModel>();

    public void registerModelVariants() {

        for(BlockState state : DynamicTooling.LAWN_LIGHT_BLOCK.getStateFactory().getStates()) {


        }
    }
}
