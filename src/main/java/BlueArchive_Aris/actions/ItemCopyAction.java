package BlueArchive_Aris.actions;

import BlueArchive_Aris.cards.RewardCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class ItemCopyAction extends AbstractGameAction {
    public ItemCopyAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        int size = 0;
        for (Iterator<AbstractCard> it = AbstractDungeon.player.hand.group.iterator(); it.hasNext(); ) {
            if (AbstractDungeon.player.hand.size() + size >= 10) {
                break;
            }

            AbstractCard iter = it.next();
            if(!(iter instanceof RewardCard)) {
                this.addToBot(new MakeTempCardInHandAction(iter.makeStatEquivalentCopy()));
                size++;
            }
        }
        this.isDone = true;
    }
}
