package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.DrowsyCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

public class DisguiseAction extends AbstractGameAction {
    public DisguiseAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }


    public void update() {
        Iterator var5 = AbstractDungeon.player.drawPile.group.iterator();

        ArrayList<AbstractCard> list = new ArrayList();
        while(var5.hasNext()) {
            AbstractCard c = (AbstractCard)var5.next();
            if (DrowsyCard.isDrowsyCard(c)) {
                list.add(c);
            }
        }

        AbstractCard random_card = (AbstractCard)list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));

        random_card.unhover();
        AbstractDungeon.player.drawPile.removeCard(random_card);
        AbstractDungeon.player.hand.addToTop(random_card);
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();

        this.isDone = true;
    }
}
