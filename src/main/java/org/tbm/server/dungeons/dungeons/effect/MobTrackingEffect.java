package org.tbm.server.dungeons.dungeons.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.tbm.server.dungeons.dungeons.component.ModComponents;
import org.tbm.server.dungeons.dungeons.component.IModifiedMobComponent;

import java.util.List;
import java.util.function.Predicate;

public class MobTrackingEffect extends StatusEffect {

    public List<String> blacklist = List.of("entity.minecraft.enderman");
    protected MobTrackingEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int pAmplifier) {
        if (livingEntity instanceof PlayerEntity) {
            Vec3d playerPos = livingEntity.getPos().add(0, 0.75, 0);
            Box mobRange = new Box(playerPos, playerPos).expand(16);
            Predicate<HostileEntity> alwaysTrue = HostileEntity -> !HostileEntity.hasCustomName();
            List<HostileEntity> hostiles = livingEntity.getEntityWorld().getEntitiesByClass(HostileEntity.class, mobRange, alwaysTrue);

            for (HostileEntity hostile : hostiles) {
                IModifiedMobComponent hostileMobComponent = ModComponents.MODIFIED_MOB_TRACK.get(hostile);
                boolean isBlacklisted = blacklist.contains(hostile.getType().toString());
                if (!hostileMobComponent.getValue() && !isBlacklisted) {
                    hostile.setTarget(livingEntity);
                    double maxHealth = hostile.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH);
                    hostile.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealth * 1.5);
                    hostile.setHealth(((float) (hostile.getMaxHealth() * 1.5)));
                    hostileMobComponent.setValue(true);
                }
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
