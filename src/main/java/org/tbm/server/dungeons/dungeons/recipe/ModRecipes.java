package org.tbm.server.dungeons.dungeons.recipe;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.tbm.server.dungeons.dungeons.Dungeons;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Dungeons.MOD_ID, GemInfusingRecipe.Serializer.ID),
                GemInfusingRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(Dungeons.MOD_ID, GemInfusingRecipe.Type.ID),
                GemInfusingRecipe.Type.INSTANCE);
    }
}