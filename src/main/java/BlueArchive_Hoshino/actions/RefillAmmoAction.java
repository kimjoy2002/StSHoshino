package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.BulletCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RefillAmmoAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;

    public RefillAmmoAction() {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }


    public void update() {
        if (this.duration != Settings.ACTION_DUR_FAST) {
            this.tickDuration();
        } else if (this.p.exhaustPile.isEmpty()) {
            this.isDone = true;
        } else {
            int exhaustSize = this.p.exhaustPile.size();
            CardGroup cardsToReturn = AbstractDungeon.player.exhaustPile;
            List<AbstractCard> cardsToExhaust = new ArrayList();
            Iterator var4 = cardsToReturn.group.iterator();

            AbstractCard c;
            while(var4.hasNext()) {
                c = (AbstractCard)var4.next();
                if (c instanceof BulletCard) {
                    AbstractDungeon.player.discardPile.moveToDeck(c, true);
                    if (c.cost > 0) {
                        c.freeToPlayOnce = true;
                    }
                    cardsToExhaust.add(c);
                    c.unfadeOut();
                    c.unhover();
                    c.fadingOut = false;
                }
            }
            if(cardsToExhaust.size() == 0) {
                this.isDone = true;
                return;
            }

            var4 = cardsToExhaust.iterator();

            while(var4.hasNext()) {
                c = (AbstractCard)var4.next();
                this.p.exhaustPile.removeCard(c);
            }

            AbstractDungeon.actionManager.addToBottom(new ShuffleAction(AbstractDungeon.player.drawPile, true));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, cardsToExhaust.size()));
            this.isDone = true;
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:RefillAmmoAction");
        TEXT = uiStrings.TEXT;
    }
}
