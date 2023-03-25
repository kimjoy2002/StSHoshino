package BlueArchive_Aris.actions;

import BlueArchive_Aris.powers.ChargePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static java.lang.Math.min;

public class DoubleChargeAction extends AbstractGameAction {
    boolean upgrade;
    public DoubleChargeAction(boolean upgrade) {
        this.setValues(this.target, this.source, 0);
        this.upgrade = upgrade;
        this.actionType = ActionType.ENERGY;
    }

    public void update() {
        if(!upgrade) {
            if(AbstractDungeon.player.hasPower("BlueArchive_Aris:ChargePower")) {
                AbstractPower cgPower = AbstractDungeon.player.getPower("BlueArchive_Aris:ChargePower");
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, cgPower.amount), cgPower.amount));
            }
        } else {
            int current_energy = EnergyPanel.totalCount;
            if(AbstractDungeon.player.hasPower("BlueArchive_Aris:ChargePower")) {
                AbstractPower cgPower = AbstractDungeon.player.getPower("BlueArchive_Aris:ChargePower");
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, cgPower.amount + current_energy), cgPower.amount + current_energy));
            }
        }
        this.isDone = true;
    }
}
