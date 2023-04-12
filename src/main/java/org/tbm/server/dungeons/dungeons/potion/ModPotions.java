package org.tbm.server.dungeons.dungeons.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.tbm.server.dungeons.dungeons.Dungeons;
import org.tbm.server.dungeons.dungeons.effect.ModEffects;
import org.tbm.server.dungeons.dungeons.item.ModItems;
import org.tbm.server.dungeons.dungeons.mixin.BrewingRecipeRegistryMixin;

public class ModPotions {
    public static Potion MAGNETISM_POTION;

    public static Potion registerPotion(String name) {
        return Registry.register(Registry.POTION, new Identifier(Dungeons.MOD_ID, name),
                new Potion(new StatusEffectInstance(ModEffects.MAGNETISM, 9600, 0)));
    }

    public static void registerPotions() {
        MAGNETISM_POTION = registerPotion("magnetism_potion");

        registerPotionRecipes();
    }

    private static void registerPotionRecipes() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.HEART_OF_ATTRACTION,
                ModPotions.MAGNETISM_POTION);
    }
}
