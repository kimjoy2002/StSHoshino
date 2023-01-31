package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.ChooseNervous;
import BlueArchive_Hoshino.cards.ChooseRepose;
import BlueArchive_Hoshino.cards.DrowsyCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;

public class PerfectPlanAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;

    public PerfectPlanAction(int amount) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = this.p.drawPile;

            if (tmp.size() <= amount) {
                this.isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, amount, TEXT[0],false, false, false, false);
                this.tickDuration();
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                Iterator drawPiles = this.p.drawPile.group.iterator();
                CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                while(drawPiles.hasNext()) {
                    AbstractCard c = (AbstractCard)drawPiles.next();
                    if(!AbstractDungeon.gridSelectScreen.selectedCards.contains(c)){
                        tmp.addToRandomSpot(c);
                    }
                }
                Iterator discards = tmp.group.iterator();
                while(discards.hasNext()) {
                    AbstractCard c = (AbstractCard)discards.next();
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:PerfectPlanAction");
        TEXT = uiStrings.TEXT;
    }
}
