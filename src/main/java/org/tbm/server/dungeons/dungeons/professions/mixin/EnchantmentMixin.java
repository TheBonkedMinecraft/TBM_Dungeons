package org.tbm.server.dungeons.dungeons.professions.mixin;

import org.tbm.server.dungeons.dungeons.professions.events.trigger.TriggerEvents;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantedItemTrigger.class)
public class EnchantmentMixin {

    @Inject(method = "trigger", at = @At("HEAD"))
    public void onTrigger(ServerPlayer player, ItemStack item, int levelsSpent, CallbackInfo ci) {
        TriggerEvents.ENCHANT_ITEM_EVENT.invoker().onItemEnchant(player, item, levelsSpent);
    }
}
