package org.tbm.server.dungeons.dungeons;

import com.google.gson.JsonElement;
import io.wispforest.owo.network.OwoNetChannel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.tbm.server.dungeons.dungeons.block.ModBlocks;
import org.tbm.server.dungeons.dungeons.component.*;
import org.tbm.server.dungeons.dungeons.effect.ModEffects;
import org.tbm.server.dungeons.dungeons.item.ModItems;
import org.tbm.server.dungeons.dungeons.packet.*;
import org.tbm.server.dungeons.dungeons.potion.ModPotions;
import org.tbm.server.dungeons.dungeons.world.dimension.ModDimensions;
import org.tbm.server.dungeons.dungeons.world.dimension.ModPortals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dungeons implements ModInitializer {
    public static final String MOD_ID = "tbm_dungeons";
    public static final OwoNetChannel STATE_CHANNEL = OwoNetChannel.create(new Identifier("tbm_dungeons", "state"));
    @Override
    public void onInitialize(){
        ModDimensions.register();
        ModBlocks.registerModBlocks();
        ModPortals.registerPortals();
        ModItems.registerModItems();
        ModEffects.registerEffects();
        ModPotions.registerPotions();
        STATE_CHANNEL.registerServerbound(C2SRequestStateOverworld.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","overworld"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(World.OVERWORLD).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(C2SRequestStateNether.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","the_nether"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(World.NETHER).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(C2SRequestStateEnd.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","the_end"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(World.END).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(C2SRequestStateDungeons.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("tbm_dungeons","tbm_dungeons"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(ModDimensions.DUNGEONS_KEY).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(C2SDifficultyUpdate.class, (packet, server) -> {
            IDifficultyComponent difficulty = ModComponents.DIFFICULTY_SETTING.get(server.player());
            ILastUpdatedDifficultyComponent lastUpdate = ModComponents.LAST_UPDATED.get(server.player());
            difficulty.setValue(packet.difficulty());
            lastUpdate.setValue(System.currentTimeMillis());
        });
        ServerEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
            if(entity.isPlayer()){
                IDifficultyComponent difficulty = ModComponents.DIFFICULTY_SETTING.get(entity);
                ((PlayerEntity) entity).clearStatusEffects();
                if (difficulty.getValue() == 0){
                    ((PlayerEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 999999, 0, true, false, true));
                } else if (difficulty.getValue() == 2) {
                    ((PlayerEntity) entity).getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(14);
                } else if (difficulty.getValue() == 3) {
                    ((PlayerEntity) entity).getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(10);
                }
            }
        }));
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer)->{
            var recipe = minecraftServer.getRecipeManager();
            var all = recipe.values().stream().toList();
            for (net.minecraft.recipe.Recipe<?> value : all) {
                RecipeType<?> type = value.getType();
                ItemStack out = value.getOutput();
                DefaultedList<Ingredient> ingredients = value.getIngredients();

                RecipeInfo info = new RecipeInfo();

                info.type = type.toString();
                info.output = RecipeItem.fromItemStack(out);
                info.ingredients = RecipeItem.fromIngredients(ingredients);

            }
        });
    }
}

