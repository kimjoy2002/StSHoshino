package BlueArchive_Aris.actions;

import BlueArchive_Aris.powers.ChargePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static java.lang.Math.min;

public class EnergyChargeAction extends AbstractGameAction {
    int amount;
    public EnergyChargeAction(int amount) {
        this.setValues(this.target, this.source, 0);
        this.amount = amount;
        this.actionType = ActionType.ENERGY;
    }

    public void update() {
        int current_energy = EnergyPanel.totalCount;
        AbstractDungeon.player.loseEnergy(this.amount);
        int gain_energy = min(current_energy, this.amount);

        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, gain_energy), gain_energy));
        this.isDone = true;
    }
}
