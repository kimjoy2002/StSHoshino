package BlueArchive_Aris.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class CreatedApotheosisAction extends AbstractGameAction {
    int amount;
    AbstractCard card;

    public CreatedApotheosisAction(AbstractCard card, int amount) {
        this.duration = Settings.ACTION_DUR_MED;
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.amount = amount;
        this.card = card;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            AbstractPlayer p = AbstractDungeon.player;
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            this.addToGroup(p.hand, tmp);
            this.addToGroup(p.drawPile, tmp);
            this.addToGroup(p.discardPile, tmp);
            this.addToGroup(p.exhaustPile, tmp);

            tmp.shuffle();

            this.upgradeCardsInGroup(tmp, amount);

            this.isDone = true;
        }

    }

    private void upgradeCardsInGroup(CardGroup cardGroup, int amout) {
        Iterator var2 = cardGroup.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.canUpgrade()) {
                if (isInHand(c)) {
                    c.superFlash();
                }

                c.upgrade();
                c.applyPowers();
                if(--amout<= 0)
                    break;
            }
        }

    }

    public boolean isInHand(AbstractCard target) {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            if (c == target) {
                return true;
            }
        }
        return false;
    }
    private void addToGroup(CardGroup source, CardGroup target) {
        Iterator var2 = source.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (card != c && c.canUpgrade()) {
                target.addToRandomSpot(c);
            }
        }
    }
}
