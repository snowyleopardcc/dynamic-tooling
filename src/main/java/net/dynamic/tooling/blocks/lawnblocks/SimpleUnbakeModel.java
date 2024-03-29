package net.dynamic.tooling.blocks.lawnblocks;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

@FunctionalInterface
public interface SimpleUnbakeModel extends UnbakedModel {

    BakedModel bake();

    @Override
    default Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    @Override
    default Collection<Identifier> getTextureDependencies(Function<Identifier, UnbakedModel> var1, Set<String> var2) {
        return Collections.emptyList();
    }

    @Override
    default BakedModel bake(ModelLoader loader, Function<Identifier, Sprite> spriteFunc, ModelBakeSettings settings) {
        return bake();
    }
}
