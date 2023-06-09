package org.tbm.server.dungeons.dungeons.professions.events;


import org.tbm.server.dungeons.dungeons.professions.api.ProfessionalPlayer;
import org.tbm.server.dungeons.dungeons.professions.profession.Profession;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionContext;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.OccupationSlot;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public final class OccupationEvents {


    public static final Event<BeforeActionHandled> BEFORE_ACTION_HANDLED_EVENT = EventFactory.createArrayBacked(BeforeActionHandled.class, calls -> (context, player) -> {
        for (BeforeActionHandled call : calls) {
            call.onHandleAction(context, player);
        }
    });

    public static final Event<ProfessionJoin> PROFESSION_JOIN_EVENT = EventFactory.createArrayBacked(ProfessionJoin.class, calls -> (player, profession, slot, serverPlayer) -> {
        for (ProfessionJoin call : calls) {
            call.onProfessionJoin(player, profession, slot, serverPlayer);
        }
    });

    public static final Event<ProfessionLeave> PROFESSION_LEAVE_EVENT = EventFactory.createArrayBacked(ProfessionLeave.class, calls -> (player, profession, serverPlayer) -> {
        for (ProfessionLeave call : calls) {
            call.onProfessionLeave(player, profession, serverPlayer);
        }
    });


    public interface BeforeActionHandled {
        void onHandleAction(ProfessionContext context, ProfessionalPlayer player);
    }

    public interface ProfessionJoin {
        void onProfessionJoin(ProfessionalPlayer player, Profession profession, OccupationSlot slot, ServerPlayer serverPlayer);
    }

    public interface ProfessionLeave {
        void onProfessionLeave(ProfessionalPlayer player, Profession profession, ServerPlayer serverPlayer);
    }
}
