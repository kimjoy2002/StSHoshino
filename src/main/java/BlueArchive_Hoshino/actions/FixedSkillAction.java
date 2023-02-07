package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.powers.FixedSkillPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.StasisPower;

import java.util.Iterator;

public class FixedSkillAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;

    public FixedSkillAction(int amount) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = this.p.drawPile;

            if (tmp.size() <= amount) {
                Iterator drawPiles = this.p.drawPile.group.iterator();
                CardGroup group_ = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                while(drawPiles.hasNext()) {
                    AbstractCard c = (AbstractCard)drawPiles.next();
                    group_.addToRandomSpot(c);
                }
                Iterator saves = group_.group.iterator();
                while(saves.hasNext()) {
                    AbstractCard c = (AbstractCard) saves.next();
                    AbstractDungeon.player.drawPile.removeCard(c);
                }

                if (group_.size() > 0) {
                    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new FixedSkillPower(AbstractDungeon.player, AbstractDungeon.player, group_)));
                }
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
                    if(AbstractDungeon.gridSelectScreen.selectedCards.contains(c)){
                        tmp.addToRandomSpot(c);
                    }
                }

                Iterator saves = tmp.group.iterator();
                while(saves.hasNext()) {
                    AbstractCard c = (AbstractCard) saves.next();
                    AbstractDungeon.player.drawPile.removeCard(c);
                    //AbstractDungeon.player.limbo.addToBottom(c);

                    //c.unfadeOut();
                    //c.unhover();
                    //c.fadingOut = false;
                }

                if (tmp.size() > 0) {
                    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new FixedSkillPower(AbstractDungeon.player, AbstractDungeon.player, tmp)));
                }


                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:FixedSkillAction");
        TEXT = uiStrings.TEXT;
    }
}
