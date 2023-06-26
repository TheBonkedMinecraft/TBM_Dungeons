package org.tbm.server.dungeons.dungeons.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.tbm.server.dungeons.dungeons.Dungeons;

public class ModScreenHandlers {
    public static ScreenHandlerType<GemInfusingScreenHandler> GEM_INFUSING_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(GemInfusingScreenHandler::new);

    public static void registerAllScreenHandlers() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(Dungeons.MOD_ID, "gem_infusing"),
                GEM_INFUSING_SCREEN_HANDLER);
    }
}