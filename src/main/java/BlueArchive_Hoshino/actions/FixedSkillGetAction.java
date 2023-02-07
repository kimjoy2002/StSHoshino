package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.powers.FixedSkillPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

public class FixedSkillGetAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    public CardGroup getCard;
    public AbstractPower power;

    public FixedSkillGetAction(AbstractPower power, CardGroup getCard) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.power = power;
        this.getCard = getCard;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = getCard;

            if (tmp.size() == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.p, this.p, power));
                this.isDone = true;
            } else if(tmp.size() == 1){
                AbstractCard card = tmp.group.get(0);
                //card.unfadeOut();
                //card.unhover();
                //card.fadingOut = false;

                if (AbstractDungeon.player.hand.size() != 10) {
                    this.addToBot(new MakeTempCardInHandAction(card, false, true));
                } else {
                    this.addToBot(new MakeTempCardInDiscardAction(card, true));
                }

                ((FixedSkillPower)power).tmp.removeCard(card);
                this.addToBot(new RemoveSpecificPowerAction(this.p, this.p, power));
                this.tickDuration();
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, 1, TEXT[0],false, false, false, false);
                this.tickDuration();
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

                //card.unfadeOut();
                //card.unhover();
                //.fadingOut = false;

                if (AbstractDungeon.player.hand.size() != 10) {
                    this.addToBot(new MakeTempCardInHandAction(card, false, true));
                } else {
                    this.addToBot(new MakeTempCardInDiscardAction(card, true));
                }

                ((FixedSkillPower)power).tmp.removeCard(card);
                ((FixedSkillPower)power).updateDescription();
                if(((FixedSkillPower)power).tmp.size() == 0) {
                    this.addToBot(new RemoveSpecificPowerAction(this.p, this.p, power));
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:FixedSkillGetAction");
        TEXT = uiStrings.TEXT;
    }
}
