package org.tbm.server.dungeons.dungeons.effect;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.Predicate;

public class MagnetEffect extends StatusEffect {
    public MagnetEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int pAmplifier) {
        Vec3d playerPos = livingEntity.getPos().add(0, 0.75, 0);
        Box itemRange = new Box(playerPos, playerPos).expand(pAmplifier == 1 ? 6 : 12);
        Predicate<ItemEntity> alwaysTrue = ItemEntity -> !ItemEntity.cannotPickup();
        List<ItemEntity> items = livingEntity.getEntityWorld().getEntitiesByClass(ItemEntity.class, itemRange, alwaysTrue);

        int pulled = 0;
        for (ItemEntity item : items) {
            boolean attractable = item.age > 75;
            if (attractable && item.isAlive()) {
                if (pulled++ > 200) {
                    break;
                }

                Vec3d motion = playerPos.subtract(item.getPos().add(0, item.getHeight() / 2, 0));
                if (Math.sqrt(motion.x * motion.x + motion.y * motion.y + motion.z * motion.z) > 1) {
                    motion = motion.normalize();
                }
                item.move(MovementType.SELF, motion.multiply(0.6));
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}