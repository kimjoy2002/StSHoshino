package BlueArchive_Aris.actions;

import BlueArchive_Hoshino.cards.ShotCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.Iterator;

public class TutorialAction  extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public int[] multiDamage;
    private boolean freeToPlayOnce = false;
    private AbstractPlayer p;
    private int energyOnUse = -1;

    public TutorialAction(AbstractPlayer p, int amount, boolean freeToPlayOnce, int energyOnUse) {
        this.amount = amount;
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            int effect = EnergyPanel.totalCount;
            if (this.energyOnUse != -1) {
                effect = this.energyOnUse;
            }

            if (this.p.hasRelic("Chemical X")) {
                effect += 2;
                this.p.getRelic("Chemical X").flash();
            }

            if (effect > 0) {
                for(int i = 0; i < effect; ++i) {
                    this.addToBot(new GainBlockAction(this.p, this.p, this.amount));
                }


                CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                Iterator var2 = AbstractDungeon.player.hand.group.iterator();

                AbstractCard c;
                while(var2.hasNext()) {
                    c = (AbstractCard)var2.next();
                    if (c.cost != -1 && c.cost != -2 && c.costForTurn <= effect) {
                        tmp.addToRandomSpot(c);
                    }
                }


                if (tmp.size() == 0) {
                    this.isDone = true;
                } else if (tmp.size() == 1) {
                    AbstractCard card = tmp.getTopCard();
                    card.costForTurn = 0;
                    this.isDone = true;
                } else {
                    AbstractDungeon.gridSelectScreen.open(tmp, 1, false, TEXT[0]);
                    this.tickDuration();
                }
                if (!this.freeToPlayOnce) {
                    this.p.energy.use(EnergyPanel.totalCount);
                }
            } else {
                this.isDone = true;
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                AbstractCard c;
                while (var1.hasNext()) {
                    c = (AbstractCard) var1.next();
                    c.costForTurn = 0;
                }
                this.isDone = true;
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Aris:TutorialAction");
        TEXT = uiStrings.TEXT;
    }
}
