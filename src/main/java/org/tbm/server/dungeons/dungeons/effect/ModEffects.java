package org.tbm.server.dungeons.dungeons.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.tbm.server.dungeons.dungeons.Dungeons;

public class ModEffects {
    public static StatusEffect MAGNETISM;

    public static StatusEffect MOB_TRACKING;

    public static StatusEffect registerMagnetEffect() {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(Dungeons.MOD_ID, "magnetism"),
                new MagnetEffect(StatusEffectCategory.BENEFICIAL, 11031295));
    }

    public static StatusEffect registerMobTrackingEffect() {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(Dungeons.MOD_ID, "mob_tracking"),
                new MobTrackingEffect(StatusEffectCategory.HARMFUL, 11031295));
    }

    public static void registerEffects() {
        MAGNETISM = registerMagnetEffect();
        MOB_TRACKING = registerMobTrackingEffect();
    }
}