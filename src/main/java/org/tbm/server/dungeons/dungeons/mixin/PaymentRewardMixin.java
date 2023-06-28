package org.tbm.server.dungeons.dungeons.mixin;

import com.epherical.octoecon.api.Currency;
import com.epherical.octoecon.api.Economy;
import com.epherical.octoecon.api.user.UniqueUser;
import com.epherical.professions.ProfessionPlatform;
import com.epherical.professions.api.ProfessionalPlayer;
import com.epherical.professions.config.ProfessionConfig;
import com.epherical.professions.profession.ProfessionContext;
import com.epherical.professions.profession.ProfessionParameter;
import com.epherical.professions.profession.action.Action;
import com.epherical.professions.profession.progression.Occupation;
import com.epherical.professions.profession.rewards.builtin.PaymentReward;
import com.epherical.professions.util.ActionLogger;
import com.mojang.logging.LogUtils;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.tbm.server.dungeons.dungeons.component.ModComponents;

import java.util.Hashtable;
import java.util.UUID;

@Mixin(PaymentReward.class)
public class PaymentRewardMixin {
    @Final
    @Shadow
    private static final Logger LOGGER = LogUtils.getLogger();

    private double value;

    /**
     * @author slowest____side
     * @reason Add support for difficulty based payment
     */
    @Overwrite(remap = false)
    public void giveReward(ProfessionContext context, Action action, Occupation occupation) {
        Economy economy = ProfessionPlatform.platform.economy();
        if (economy != null) {
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
            this.value = value;
            UUID playerID = ((ProfessionalPlayer)context.getParameter(ProfessionParameter.THIS_PLAYER)).getUuid();
            UniqueUser user = economy.getOrCreatePlayerAccount(playerID);
            Currency currency = economy.getCurrency(((PaymentReward)(Object)this).currency());
            if (ProfessionConfig.overrideCurrencyID) {
                currency = economy.getCurrency(new Identifier(ProfessionConfig.overriddenCurrencyID));
                if (currency == null) {
                    LOGGER.warn("You overrode the currency in the datapack, but an override currency could not be found! If this was a mistake, go in the config");
                    LOGGER.warn("And change 'overrideCurrencyID' to false!");
                    throw new RuntimeException("payment could not be processed.");
                }
            }

            if (currency == null) {
                throw new RuntimeException("payment could not be processed.");
            }

            if (user != null) {
                if (((PaymentReward)(Object)this).amount() * value > 0.0) {
                    user.depositMoney(currency, ((PaymentReward)(Object)this).amount() * value, "Professions Action Reward");
                } else {
                    user.withdrawMoney(currency, ((PaymentReward)(Object)this).amount() * value, "Professions Action Penalty");
                }

                ((ActionLogger)context.getParameter(ProfessionParameter.ACTION_LOGGER)).addMoneyReward(((PaymentReward)(Object)this).rewardChatInfo());
            }
        }
    }

    /**
     * @author slowest____side
     * @reason Add support for difficulty based payment
     */
    @Overwrite
    public Text rewardChatInfo() {
        return Text.literal(String.format("$%.2f", ((PaymentReward)(Object)this).amount() * this.value)).setStyle(Style.EMPTY.withColor(ProfessionConfig.money));
    }
}
