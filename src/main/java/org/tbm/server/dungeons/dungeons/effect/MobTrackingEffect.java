package org.tbm.server.dungeons.dungeons.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.tbm.server.dungeons.dungeons.Dungeons;
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

            for (int i = 0; i < hostiles.size(); i++) {
                IModifiedMobComponent hostileMobComponent = Dungeons.MODIFIED_MOB_TRACK.get(hostiles.get(i));
                boolean isBlacklisted = blacklist.contains(hostiles.get(i).getType().toString());
                if (!hostileMobComponent.getValue() && !isBlacklisted) {
                    hostiles.get(i).setTarget(livingEntity);
                    double maxHealth = hostiles.get(i).getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH);
                    hostiles.get(i).getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(maxHealth * 1.5);
                    hostiles.get(i).setHealth(((float) (hostiles.get(i).getMaxHealth() * 1.5)));
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
