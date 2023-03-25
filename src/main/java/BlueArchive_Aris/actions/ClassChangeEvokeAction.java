package BlueArchive_Aris.actions;

import BlueArchive_Aris.cards.StraightStrike;
import BlueArchive_Aris.powers.JobPower;
import BlueArchive_Aris.relics.ClassChangeRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

public class ClassChangeEvokeAction extends AbstractGameAction {
    public ClassChangeEvokeAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        Iterator iter = AbstractDungeon.player.discardPile.group.iterator();

        Iterator powerIter = AbstractDungeon.player.powers.iterator();
        while(powerIter.hasNext()) {
            AbstractPower p = (AbstractPower) powerIter.next();
            if(p instanceof JobPower) {
                ((JobPower)p).onJobChange(false);

                while(iter.hasNext()) {
                    AbstractCard c = (AbstractCard)iter.next();
                    if (c instanceof StraightStrike){
                        this.addToBot(new DiscardToHandAction(c));
                    }
                }

                Iterator relicIter = AbstractDungeon.player.relics.iterator();
                while(relicIter.hasNext()) {
                    AbstractRelic r = (AbstractRelic) relicIter.next();
                    if (r instanceof ClassChangeRelic) {
                        ((ClassChangeRelic)r).onClassChange();
                    }
                }
                break;
            }
        }


        this.isDone = true;
    }
}
