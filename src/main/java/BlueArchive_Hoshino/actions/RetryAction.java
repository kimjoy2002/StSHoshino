package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.DrowsyCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class RetryAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    int block;

    public RetryAction(int amount, int block) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.block = block;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup discardPiles = this.p.discardPile;

            if (discardPiles.size() <= amount) {
                Iterator discardPileIter = this.p.discardPile.group.iterator();
                CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                while(discardPileIter.hasNext()) {
                    AbstractCard c = (AbstractCard)discardPileIter.next();
                    tmp.addToRandomSpot(c);
                }

                Iterator discards = tmp.group.iterator();
                while(discards.hasNext()) {
                    AbstractCard c = (AbstractCard)discards.next();
                    AbstractDungeon.player.discardPile.moveToDeck(c, true);
                    if(c instanceof DrowsyCard) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
                    }
                }

                this.isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(discardPiles, amount, true , TEXT[0]);
                this.tickDuration();
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                Iterator discardPiles = this.p.discardPile.group.iterator();
                CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                while(discardPiles.hasNext()) {
                    AbstractCard c = (AbstractCard)discardPiles.next();
                    if(AbstractDungeon.gridSelectScreen.selectedCards.contains(c)){
                        tmp.addToRandomSpot(c);
                    }
                }
                Iterator discards = tmp.group.iterator();
                while(discards.hasNext()) {
                    AbstractCard c = (AbstractCard)discards.next();
                    AbstractDungeon.player.discardPile.moveToDeck(c, true);
                    if(c instanceof DrowsyCard) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:RetryAction");
        TEXT = uiStrings.TEXT;
    }
}
