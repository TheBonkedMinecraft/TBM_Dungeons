package org.tbm.server.dungeons.dungeons.gear.tier1;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.NotNull;
import org.tbm.server.dungeons.dungeons.item.ModArmorMaterials;


public class Helmet extends ArmorItem implements FabricItem {
    public int armorValue;
    public Helmet(int armorValue) {
        super(ModArmorMaterials.MYTHRIL, EquipmentSlot.HEAD, new Item.Settings().rarity(Rarity.RARE));
        this.armorValue = armorValue;
    }

    @NotNull
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        Multimap<EntityAttribute, EntityAttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == this.slot) {
            multimap.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier("Armor", this.armorValue, EntityAttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }
}
