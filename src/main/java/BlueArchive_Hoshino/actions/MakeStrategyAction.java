package BlueArchive_Hoshino.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class MakeStrategyAction extends AbstractGameAction {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;

    public MakeStrategyAction(int value) {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.setValues(this.p, AbstractDungeon.player, value);
    }

    public void update() {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            if (c.baseDamage >= 0) {
                c.baseDamage += this.amount;
                if (c.baseDamage < 0) {
                    c.baseDamage = 0;
                }
            }
            if (c.baseBlock >= 0) {
                c.baseBlock += this.amount;
                if (c.baseBlock < 0) {
                    c.baseBlock = 0;
                }
            }
        }
        this.isDone = true;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:BDNoteAction");
        TEXT = uiStrings.TEXT;
    }
}
