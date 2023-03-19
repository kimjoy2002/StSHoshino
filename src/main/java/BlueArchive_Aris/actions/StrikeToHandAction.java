package BlueArchive_Aris.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class StrikeToHandAction extends AbstractGameAction {

    private AbstractPlayer p;

    public StrikeToHandAction() {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    }

    public void update() {
        Iterator var1 = AbstractDungeon.player.discardPile.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            if (c.hasTag(AbstractCard.CardTags.STRIKE)) {
                c.setCostForTurn(-9);
                this.addToBot(new DiscardToHandAction(c));
            }
        }
        this.isDone = true;
    }
}
