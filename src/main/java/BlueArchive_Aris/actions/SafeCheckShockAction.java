package BlueArchive_Aris.actions;

import BlueArchive_Aris.powers.ShockPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerExpireTextEffect;

import java.util.Iterator;

public class SafeCheckShockAction extends AbstractGameAction {
    private String powerToRemove;
    public SafeCheckShockAction(AbstractCreature target) {
        this.setValues(target, AbstractDungeon.player, 0);
        this.actionType = ActionType.DEBUFF;
        this.duration = 0.1F;
        this.powerToRemove = ShockPower.POWER_ID;
    }

    public void update() {
        if (this.duration == 0.1F) {
            if (this.target.isDeadOrEscaped()) {
                this.isDone = true;
                return;
            }

            AbstractPower removeMe = null;
            if (this.powerToRemove != null) {
                removeMe = this.target.getPower(this.powerToRemove);
            }

            if (removeMe != null && removeMe.amount <= 0) {
                AbstractDungeon.effectList.add(new PowerExpireTextEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, removeMe.name, removeMe.region128));
                removeMe.onRemove();
                this.target.powers.remove(removeMe);
                AbstractDungeon.onModifyPower();
                Iterator var2 = AbstractDungeon.player.orbs.iterator();

                while(var2.hasNext()) {
                    AbstractOrb o = (AbstractOrb)var2.next();
                    o.updateDescription();
                }
            } else {
                this.duration = 0.0F;
            }
        }

        this.tickDuration();
    }
}
