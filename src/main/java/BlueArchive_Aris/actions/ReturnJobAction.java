package BlueArchive_Aris.actions;

import BlueArchive_Aris.cards.StraightStrike;
import BlueArchive_Aris.powers.JobPower;
import BlueArchive_Aris.relics.ClassChangeRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

public class ReturnJobAction extends AbstractGameAction {
    public ReturnJobAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        Iterator powerIter = AbstractDungeon.player.powers.iterator();
        while(powerIter.hasNext()) {
            AbstractPower p = (AbstractPower) powerIter.next();
            if(p instanceof JobPower) {
                this.addToBot(new MakeTempCardInHandAction(((JobPower)p).equip));
                this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, p));
            }
        }
        this.isDone = true;
    }
}
