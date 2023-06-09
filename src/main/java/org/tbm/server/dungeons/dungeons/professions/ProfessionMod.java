package org.tbm.server.dungeons.dungeons.professions;

import org.tbm.server.dungeons.dungeons.professions.api.ProfessionalPlayer;
import org.tbm.server.dungeons.dungeons.professions.data.Storage;
import org.tbm.server.dungeons.dungeons.professions.datapack.ProfessionLoader;

import java.util.UUID;

public abstract class ProfessionMod {

    public abstract ProfessionLoader getProfessionLoader();
    public abstract PlayerManager getPlayerManager();
    public abstract Storage<ProfessionalPlayer, UUID> getDataStorage();
}
