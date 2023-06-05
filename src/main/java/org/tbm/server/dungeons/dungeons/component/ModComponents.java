package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<IDungeonsTickComponent> DUNGEONS_TICK =
            ComponentRegistry.getOrCreate(new Identifier("tbm_dungeons", "dungeons_tick"), IDungeonsTickComponent.class);
    public static final ComponentKey<IDungeonsPortalPosComponent> PORTAL_POS =
            ComponentRegistry.getOrCreate(new Identifier("tbm_dungeons", "portal_pos"), IDungeonsPortalPosComponent.class);
    public static final ComponentKey<IDifficultyComponent> DIFFICULTY_SETTING =
            ComponentRegistry.getOrCreate(new Identifier("tbm_dungeons", "difficulty"), IDifficultyComponent.class);
    public static final ComponentKey<IModifiedMobComponent> MODIFIED_MOB_TRACK =
            ComponentRegistry.getOrCreate(new Identifier("tbm_dungeons", "modified_mob"), IModifiedMobComponent.class);
    public static final ComponentKey<ILastUpdatedDifficultyComponent> LAST_UPDATED =
            ComponentRegistry.getOrCreate(new Identifier("tbm_dungeons", "last_updated"), ILastUpdatedDifficultyComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DUNGEONS_TICK, DungeonsTick::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(PORTAL_POS, DungeonsPortalPos::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(DIFFICULTY_SETTING, DungeonDifficulty::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(LAST_UPDATED, LastUpdatedDifficulty::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerFor(HostileEntity.class, MODIFIED_MOB_TRACK, entity -> new ModifiedMobTrack());
    }
}
