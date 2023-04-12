package org.tbm.server.dungeons.dungeons.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.tbm.server.dungeons.dungeons.Dungeons;

public class ModEffects {
    public static StatusEffect MAGNETISM;

    public static StatusEffect registerStatusEffect(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(Dungeons.MOD_ID, name),
                new MagnetEffect(StatusEffectCategory.BENEFICIAL, 11031295));
    }

    public static void registerEffects() {
        MAGNETISM = registerStatusEffect("magnetism");
    }
}