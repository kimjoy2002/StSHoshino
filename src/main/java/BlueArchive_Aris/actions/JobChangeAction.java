package BlueArchive_Aris.actions;

import BlueArchive_Aris.cards.AttackedCard;
import BlueArchive_Aris.cards.StraightStrike;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.*;
import BlueArchive_Aris.relics.ClassChangeRelic;
import BlueArchive_Hoshino.cards.DrowsyCard;
import BlueArchive_Hoshino.powers.GoldWhalePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Iterator;

public class JobChangeAction extends AbstractGameAction {
    AbstractCard card;
    JobPower power;
    public JobChangeAction(AbstractCard card, JobPower power) {
        this.card = card;
        this.power = power;
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }


    public void update() {
        //CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        //tmp.addToRandomSpot(card);
        AbstractDungeon.player.drawPile.removeCard(card);
        AbstractDungeon.player.hand.removeCard(card);
        AbstractDungeon.player.discardPile.removeCard(card);
        boolean sameJob = false;
        Iterator powerIter = AbstractDungeon.player.powers.iterator();
        while(powerIter.hasNext()) {
            AbstractPower p = (AbstractPower) powerIter.next();
            if(p instanceof JobPower) {
                if(AbstractDungeon.player.hasPower(WeaponReturnPower.POWER_ID)) {
                    this.addToBot(new MakeTempCardInHandAction(((JobPower)p).equip, 1));
                    ((WeaponMasterPower)AbstractDungeon.player.getPower(WeaponMasterPower.POWER_ID)).flash();
                    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, WeaponReturnPower.POWER_ID, 1));
                } else {
                    this.addToBot(new MakeTempCardInDiscardAction(((JobPower)p).equip, 1));
                }

                this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, p));
                if(power.ID == p.ID) {
                    sameJob = true;
                }
            }
        }



        if(!sameJob){
            power.onJobChange(true);

            Iterator iter = AbstractDungeon.player.discardPile.group.iterator();

            this.addToBot(new ChangeArisAniAction(power.getAnimation()));
            while(iter.hasNext()) {
                AbstractCard c = (AbstractCard)iter.next();
                if (c instanceof StraightStrike){
                    this.addToBot(new DiscardToHandAction(c));
                }
            }

            Iterator relicIter = AbstractDungeon.player.relics.iterator();
            while(relicIter.hasNext()) {
                AbstractRelic r = (AbstractRelic) relicIter.next();
                if (r instanceof ClassChangeRelic) {
                    ((ClassChangeRelic)r).onClassChange();
                }
            }
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                power));

        this.isDone = true;
    }
}
