package net.dynamic.tooling.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * NOTE: Some of this code comes from Falkreon / UM!
 */
public class DTRecipes {

    public static RecipeType<ManualMillRecipe> MANUAL_MILL;



    public static void init() {

        /**
         * This *should* be working...?
         */
//        MANUAL_MILL = Registry.register(Registry.RECIPE_TYPE, new Identifier("dynamic-tooling:manual_mill"), new RecipeType<ManualMillRecipe>() {
//            @Override
//            public <C extends Inventory> Optional<ManualMillRecipe> get(Recipe<C> recipe_1, World world_1, C inventory_1) {
//
//                return recipe_1.matches(inventory_1, world_1) ? Optional.of((ManualMillRecipe) recipe_1) : Optional.empty();
//            }
//        });
//

        MANUAL_MILL = DTRecipes.register("dynamic-tooling:manual_mill");

        Registry.register(Registry.RECIPE_SERIALIZER, "dynamic-tooling:manual_mill", ManualMillRecipe.SERIALIZER);
    }

    public static <T extends Recipe<?>> RecipeType<T> register(final String id) {
        return Registry.register(Registry.RECIPE_TYPE, new Identifier(id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    public static Ingredient getIngredient(JsonElement elem, String elemKey) throws JsonSyntaxException {

        if (elem instanceof JsonPrimitive) {

            Item item = JsonHelper.asItem(elem, elemKey);
            return Ingredient.ofItems(item);
        } else {

            return Ingredient.fromJson(elem);
        }
    }

    /**
     * Gets an ItemStack from the specified JsonElement. This should really already exist in JsonHelper.
     * @param elem      The element to turn into an ItemStack
     * @param elemKey   The key the element has on its parent object, for constructing error messages
     * @return          The ItemStack expressed by this Json. Will never be nonnull or an empty ItemStack.
     * @throws JsonSyntaxException If the ItemStack expressed is empty or invalid.
     */
    public static ItemStack getItemStack(JsonElement elem, String elemKey) throws JsonSyntaxException {

        if (elem instanceof JsonPrimitive) {

            Item item = JsonHelper.asItem(elem, elemKey);
            return new ItemStack(item);
        } else if (elem instanceof JsonObject) {

            JsonObject obj = (JsonObject)elem;

            int count = JsonHelper.getInt(obj, "count", 1);
            int meta = JsonHelper.getInt(obj, "meta", 0);

            if (obj.has("item")) {

                Item item = JsonHelper.asItem(elem, elemKey);
                ItemStack stack = new ItemStack(item, count);

                if (meta!=0) stack.setDamage(meta);
                return stack;
            } else if (obj.has("tag")) {
                String tagName = JsonHelper.getString(obj, "tag");
                Tag<Item> tag = ItemTags.getContainer().get(new Identifier(tagName));
                if (tag==null) throw new JsonSyntaxException("Can't find a tag named '"+tagName+"'.");
                Collection<Item> tagValues = tag.values();
                if (tagValues.size()==0) throw new JsonSyntaxException("Tag '"+tagName+"' has no Items associated with it.");
                Item firstEntry = tagValues.iterator().next();
                ItemStack stack = new ItemStack(firstEntry, count);
                if (meta!=0) stack.setDamage(meta);
                return stack;
            } else {
                throw new JsonSyntaxException("Expected " + elemKey + " to contain an 'item' or 'tag' key, found neither.");
            }
        } else {
            throw new JsonSyntaxException("Expected " + elemKey + " to be a valid item string or object, found "+JsonHelper.getType(elem));
        }
    }

}
