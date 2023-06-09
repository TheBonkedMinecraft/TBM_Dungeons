package org.tbm.server.dungeons.dungeons.professions.util;

import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public record EnchantmentContainer(@NotNull Enchantment enchantment, int level) {
}
