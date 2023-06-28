package org.tbm.server.dungeons.dungeons.mixin;

import com.epherical.professions.PlayerManager;
import com.epherical.professions.ProfessionPlatform;
import com.epherical.professions.api.ProfessionalPlayer;
import com.epherical.professions.config.ProfessionConfig;
import com.epherical.professions.profession.ProfessionContext;
import com.epherical.professions.profession.ProfessionParameter;
import com.epherical.professions.profession.action.Action;
import com.epherical.professions.profession.progression.Occupation;
import com.epherical.professions.profession.rewards.builtin.OccupationExperience;
import com.epherical.professions.util.ActionLogger;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.tbm.server.dungeons.dungeons.component.ModComponents;

import java.util.Hashtable;

@Mixin(OccupationExperience.class)
public class ExpRewardMixin {

    public double exp;
    /**
     * @author slowest____side
     * @reason Add support for difficulty based exp
     */
    @Overwrite(remap = false)
    public void giveReward(ProfessionContext context, Action action, Occupation occupation) {
        var mcPlayer = context.getParameter(ProfessionParameter.THIS_PLAYER).getPlayer();
        var value = 1.0;
        if (mcPlayer != null) {
            var difficulty = ModComponents.DIFFICULTY_SETTING.get(mcPlayer);
            Hashtable<Integer, Double> dict = new Hashtable<>();
            dict.put(0, 0.7);
            dict.put(1, 1.0);
            dict.put(2, 1.5);
            dict.put(3, 2.0);
            value = dict.get(difficulty.getValue());
        }
        var exp = ((OccupationExperience)(Object)this).expAmount() * value;
        this.exp = exp;
        ProfessionalPlayer player = (ProfessionalPlayer)context.getParameter(ProfessionParameter.THIS_PLAYER);
        ((ActionLogger)context.getParameter(ProfessionParameter.ACTION_LOGGER)).addExpReward(((OccupationExperience)(Object)this).rewardChatInfo(), exp, occupation);
        int currentLevel = occupation.getLevel();
        if (occupation.addExp(action.modifyReward(context, ((OccupationExperience)(Object)this), exp), player)) {
            PlayerManager manager = ProfessionPlatform.platform.getPlayerManager();
            manager.levelUp(player, occupation, currentLevel);
        }
    }

    /**
     * @author slowest____side
     * @reason Add support for difficulty based exp
     */
    @Overwrite(remap = false)
    public @NotNull Text rewardChatInfo() {
        return Text.literal(String.format("%.2fxp", this.exp)).setStyle(Style.EMPTY.withColor(ProfessionConfig.experience));
    }
}