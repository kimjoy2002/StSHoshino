package BlueArchive_Aris.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class RandomDiscardToHandAction extends AbstractGameAction {

    private AbstractPlayer p;

    public RandomDiscardToHandAction() {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator var1 = AbstractDungeon.player.discardPile.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            tmp.addToRandomSpot(c);
        }
        tmp.shuffle();

        if (tmp.size() != 0) {
            AbstractCard card = tmp.getTopCard();
            this.addToBot(new DiscardToHandAction(card));
        }

        this.isDone = true;
    }
}
