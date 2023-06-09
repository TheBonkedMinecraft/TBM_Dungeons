package org.tbm.server.dungeons.dungeons.professions.datagen;

import com.epherical.octoecon.api.Economy;
import com.epherical.org.mbertoli.jfep.Parser;
import org.tbm.server.dungeons.dungeons.professions.ProfessionsFabric;
import org.tbm.server.dungeons.dungeons.professions.datapack.FabricProfLoader;
import org.tbm.server.dungeons.dungeons.professions.profession.Profession;
import org.tbm.server.dungeons.dungeons.professions.profession.editor.Editor;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.Reward;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.builtin.OccupationExperience;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.builtin.PaymentReward;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;

public interface ProviderHelpers {

    default Path createNormalPath(Path path, ResourceLocation id, boolean forge) {
        String namespace = id.getNamespace();
        if (forge) {
            return path.resolve("resourcepacks/forge/normal/data/" + namespace + "/professions/occupations/" + id.getPath() + ".json");
        }
        return path.resolve("resourcepacks/fabric/normal/data/" + namespace + "/professions/occupations/" + id.getPath() + ".json");
    }

    default Path createHardcoreAppenders(Path path, ResourceLocation id, boolean forge) {
        String namespace = id.getNamespace();
        if (forge) {
            return path.resolve("resourcepacks/forge/hardcore/data/" + namespace + "/professions/occupations/" + id.getPath() + ".json");
        }
        return path.resolve("resourcepacks/fabric/hardcore/data/" + namespace + "/professions/occupations/" + id.getPath() + ".json");
    }

    default void generate(CachedOutput cache, Profession profession, Path id) throws IOException {
        DataProvider.saveStable(cache, FabricProfLoader.serialize(profession), id);
    }

    default void generate(CachedOutput cache, Editor editor, Path id) throws IOException {
        DataProvider.saveStable(cache, FabricProfLoader.serialize(editor), id);
    }

    default Parser defaultLevelParser() {
        return new Parser("(1000)*((1.03706264)^(lvl-1))");
    }

    default Parser defaultIncomeParser() {
        return new Parser("base");
    }

    default Reward.Builder expReward(double reward) {
        return OccupationExperience.builder().exp(reward);
    }

    default Reward.Builder moneyReward(double amount) {
        Economy economy = ProfessionsFabric.getEconomy();
        if (economy == null) {
            return PaymentReward.builder().money(amount, null);
        } else {
            return PaymentReward.builder().money(amount, economy.getDefaultCurrency());
        }
    }
}
