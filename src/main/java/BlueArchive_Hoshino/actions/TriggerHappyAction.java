package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.TriggerHappy;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

public class TriggerHappyAction extends AbstractGameAction {
    private AbstractCard card;

    public TriggerHappyAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    public void update() {
        AbstractCard var10000 = this.card;
        var10000.baseDamage += this.amount;
        this.card.applyPowers();
        Iterator var1 = AbstractDungeon.player.discardPile.group.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c instanceof TriggerHappy) {
                ((TriggerHappy)c).damageForTurn += this.amount;
                c.applyPowers();
            }
        }

        var1 = AbstractDungeon.player.hand.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c instanceof TriggerHappy) {
                ((TriggerHappy)c).damageForTurn += this.amount;
                c.applyPowers();
            }
        }

        var1 = AbstractDungeon.player.drawPile.group.iterator();
        ArrayList<AbstractCard> getCards = new ArrayList<AbstractCard>();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c instanceof TriggerHappy) {
                getCards.add(c);
                ((TriggerHappy)c).damageForTurn += this.amount;
                c.applyPowers();
            }
        }

        var1 = getCards.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c instanceof TriggerHappy) {
                if (AbstractDungeon.player.hand.size() < 10) {
                    c.unhover();
                    AbstractDungeon.player.drawPile.removeCard(c);
                    AbstractDungeon.player.hand.addToTop(c);
                }
            }

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
        }

        AbstractDungeon.player.hand.refreshHandLayout();

        this.isDone = true;
    }
}
