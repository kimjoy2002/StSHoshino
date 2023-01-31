package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.BulletCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class AmmoBoxAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    private boolean upgrade = false;

    public AmmoBoxAction(boolean upgrade) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, 1);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.upgrade = upgrade;
    }

    public void update() {
        Iterator c;
        AbstractCard derp;
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.size() == 10) {
                AbstractDungeon.player.createHandIsFullDialog();
                this.isDone = true;
            } else if (this.p.exhaustPile.isEmpty()) {
                this.isDone = true;
            } else if (this.p.exhaustPile.size() == 1) {
                if ((this.p.exhaustPile.group.get(0) instanceof BulletCard)) {
                    AbstractCard card = this.p.exhaustPile.getTopCard();
                    card.unfadeOut();
                    this.p.hand.addToHand(card);
                    if(upgrade)
                        card.setCostForTurn(card.costForTurn - 1);

                    this.p.exhaustPile.removeCard(card);
                    card.unhover();
                    card.fadingOut = false;
                }
                this.isDone = true;
            } else {
                c = this.p.exhaustPile.group.iterator();

                while(c.hasNext()) {
                    derp = (AbstractCard)c.next();
                    derp.stopGlowing();
                    derp.unhover();
                    derp.unfadeOut();
                }

                c = this.p.exhaustPile.group.iterator();

                while(c.hasNext()) {
                    derp = (AbstractCard)c.next();
                    if (!(derp instanceof BulletCard)) {
                        c.remove();
                    }
                }

                if (this.p.exhaustPile.isEmpty()) {
                    this.isDone = true;
                } else {
                    AbstractDungeon.gridSelectScreen.open(this.p.exhaustPile, 1, TEXT[0], false);
                    this.tickDuration();
                }
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for(c = AbstractDungeon.gridSelectScreen.selectedCards.iterator(); c.hasNext(); derp.unhover()) {
                    derp = (AbstractCard)c.next();
                    this.p.hand.addToHand(derp);
                    if(upgrade)
                        derp.setCostForTurn(-9);

                    this.p.exhaustPile.removeCard(derp);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();

                for(c = this.p.exhaustPile.group.iterator(); c.hasNext(); derp.target_y = 0.0F) {
                    derp = (AbstractCard)c.next();
                    derp.unhover();
                    derp.target_x = (float)CardGroup.DISCARD_PILE_X;
                }
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:AmmoBoxAction");
        TEXT = uiStrings.TEXT;
    }
}
