package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.DrowsyCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class AllDrowsyAction extends AbstractGameAction {

    public AllDrowsyAction(int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            if (DrowsyCard.isDrowsyCard(c)) {
                for(int i = 0; i < amount; i++) {
                    this.addToBot(new DrowsyAction(c.uuid, -1));
                }
            }
        }
        this.isDone = true;
    }
}
