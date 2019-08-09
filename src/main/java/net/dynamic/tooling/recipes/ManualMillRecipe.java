package net.dynamic.tooling.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.dynamic.tooling.DynamicTooling;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;

public class ManualMillRecipe implements Recipe<Inventory> {

    /**
     * NOTE:  Some of this code comes from Falkreon / UM!
     * Currently using as a starting point,  and eventually
     * will be refactored out into a JSON Utilities Class...
     *
     * TODO:  See above for more information...  :P
     */


    public ManualMillRecipe(String id, Ingredient ingredient, ItemStack result, int count, int duration) {

        this(new Identifier(id), ingredient, result, count, duration);
    }

    public ManualMillRecipe(Identifier identifier, Ingredient ingredient, ItemStack result, int count, int duration) {

        this.identifier = identifier;
        this.ingredient = ingredient;
        this.result = result;
        this.count = count;
        this.duration = duration;
    }

    public static final Serializer SERIALIZER = new Serializer();

    protected final Identifier identifier;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final int count;
    protected final int duration;

    public int getCount() { return this.count; }
    public int getDuration() { return duration; }

    @Override
    public boolean matches(Inventory inventory, World world) {

        return ingredient.method_8093(inventory.getInvStack(0));
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return result.copy();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean fits(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return result;
    }

    @Override
    public Identifier getId() {
        return identifier;
    }

    @Override
    public RecipeSerializer<ManualMillRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return DTRecipes.MANUAL_MILL;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ItemStack getRecipeKindIcon() {

        return new ItemStack(DynamicTooling.MANUAL_MILL_BLOCK);
    }

    public static class Serializer implements RecipeSerializer<ManualMillRecipe> {

        @Override
        public ManualMillRecipe read(Identifier id, JsonObject json) {

            JsonElement ingredientElem = json.get("ingredient");

            if (ingredientElem==null) throw new JsonSyntaxException("Recipe must have an ingredient.");

            Ingredient input = DTRecipes.getIngredient(ingredientElem, "ingredient");

            JsonElement resultElem = json.get("result");

            if (resultElem==null) throw new JsonSyntaxException("Recipe must have a result.");
            ItemStack result = DTRecipes.getItemStack(resultElem, "result");

            int countElem = JsonHelper.getInt(json, "count");

            int processTime = JsonHelper.getInt(json, "duration");

            System.out.println("#######################Recipe '"+id+"' read successfully.");

            return new ManualMillRecipe(id, input, result, countElem, processTime);
        }

        @Override
        public ManualMillRecipe read(Identifier id, PacketByteBuf buffer) {

            Ingredient ingredient = Ingredient.fromPacket(buffer);
            ItemStack result = buffer.readItemStack();
//            ItemStack extraResult = buffer.readItemStack();
//            float randomChance = buffer.readFloat();
//            int energy = buffer.readInt();
            int resultCount = buffer.readInt();
            int processTime = buffer.readInt();

            System.out.println("################################Received recipes "+id+" on client.");
            return new ManualMillRecipe(id, ingredient, result, resultCount, processTime);
        }

//        java.util.concurrent.ExecutionException: java.lang.NoSuchMethodError: net.minecraft.class_1856.method_8101([Lnet/minecr
        @Override
        public void write(PacketByteBuf buffer, ManualMillRecipe recipe) {

            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeInt(recipe.count);
            buffer.writeInt(recipe.duration);
        }
    }
}
