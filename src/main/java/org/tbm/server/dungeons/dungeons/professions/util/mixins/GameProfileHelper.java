package org.tbm.server.dungeons.dungeons.professions.util.mixins;

import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

public interface GameProfileHelper {

    @Nullable GameProfile professions$getProfileNoLookup(String name);

}
