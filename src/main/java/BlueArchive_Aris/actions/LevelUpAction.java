package BlueArchive_Aris.actions;

import BlueArchive_Aris.powers.JobPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

public class LevelUpAction extends AbstractGameAction {
    public LevelUpAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        Iterator powerIter = AbstractDungeon.player.powers.iterator();
        while(powerIter.hasNext()) {
            AbstractPower p = (AbstractPower) powerIter.next();
            if(p instanceof JobPower) {
                ((JobPower)p).levelUp();
                p.updateDescription();
            }
        }
        this.isDone = true;
    }
}
