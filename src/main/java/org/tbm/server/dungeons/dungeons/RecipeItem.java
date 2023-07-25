package org.tbm.server.dungeons.dungeons;

import com.google.gson.JsonElement;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeItem {
    public Optional<RegistryKey<Item>> id;
    public int count;

    public RecipeItem(Optional<RegistryKey<Item>> id, int count) {
        this.id = id;
        this.count = count;
    }

    public static RecipeItem fromItemStack(ItemStack stack) {

        Optional<RegistryKey<Item>> loc = Registry.ITEM.getKey(stack.getItem());

        return new RecipeItem(loc, stack.getCount());
    }

    public static List<JsonElement> fromIngredients(DefaultedList<Ingredient> items) {
        List<JsonElement> ingredients = new ArrayList<>();

        for (Ingredient ingredient : items) {
            ingredients.add(ingredient.toJson());
        }

        return ingredients;
    }
}
