package org.tbm.server.dungeons.dungeons.professions.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ProfessionDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(FabricProfessionProvider::new);
        fabricDataGenerator.addProvider(ForgeProfessionProvider::new);
    }

}
