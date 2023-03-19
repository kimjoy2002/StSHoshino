package BlueArchive_Aris.actions;

import BlueArchive_Aris.powers.ShockPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class DoubleShockAction extends AbstractGameAction {
    public DoubleShockAction(AbstractCreature target, AbstractCreature source) {
        this.target = target;
        this.source = source;
        this.actionType = AbstractGameAction.ActionType.DEBUFF;
        this.attackEffect = AttackEffect.LIGHTNING;
    }

    public void update() {
        if (this.target != null && this.target.hasPower(ShockPower.POWER_ID)) {
            this.addToTop(new ApplyPowerAction(this.target, this.source, new ShockPower(this.target, this.source, this.target.getPower(ShockPower.POWER_ID).amount), this.target.getPower(ShockPower.POWER_ID).amount));
        }

        this.isDone = true;
    }
}
