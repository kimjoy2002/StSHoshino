package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.BetterDiscardPileToHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class BulletExhaustAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    boolean isRandom = false;

    public BulletExhaustAction(boolean isRandom) {
        this.p = AbstractDungeon.player;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
        this.isRandom = isRandom;
    }


    public void update() {
        if (this.duration == this.startDuration) {
            if(isRandom) {

                if (this.p.hand.size() == 0) {
                    this.isDone = true;
                    return;
                }

                ArrayList<AbstractCard> list = new ArrayList();
                Iterator var2 = AbstractDungeon.player.hand.group.iterator();

                AbstractCard c;
                while(var2.hasNext()) {
                    c = (AbstractCard)var2.next();
                    if (c instanceof ShotCard) {
                        list.add(c);
                    }
                }

                if(list.size() == 0) {
                    this.isDone = true;
                    return;
                }

                AbstractCard resultCard = list.get(AbstractDungeon.cardRandomRng.random(list.size()-1));

                this.p.hand.moveToExhaustPile(resultCard);
                CardCrawlGame.dungeon.checkForPactAchievement();
                this.isDone = true;
            } else {
                CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                Iterator var5 = this.p.hand.group.iterator();

                while(var5.hasNext()) {
                    AbstractCard c = (AbstractCard)var5.next();
                    if (c instanceof ShotCard) {
                        tmp.addToRandomSpot(c);
                    }
                }

                if (tmp.size() == 0) {
                    this.isDone = true;
                } else if (tmp.size() == 1) {
                    AbstractCard card = tmp.getTopCard();
                    this.p.hand.moveToExhaustPile(card);
                    CardCrawlGame.dungeon.checkForPactAchievement();
                    this.isDone = true;
                } else {
                    AbstractDungeon.gridSelectScreen.open(tmp, 1, false, TEXT[0]);
                    this.tickDuration();
                }
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                AbstractCard c;
                while (var1.hasNext()) {
                    c = (AbstractCard) var1.next();
                    this.p.hand.moveToExhaustPile(c);
                }
                this.isDone = true;
            }
            this.tickDuration();
        }
    }
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:BulletExhaustAction");
        TEXT = uiStrings.TEXT;
    }
}
