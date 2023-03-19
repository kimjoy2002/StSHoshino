package BlueArchive_Aris.actions;

import BlueArchive_Aris.powers.ShockPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class RandomForgeAction extends AbstractGameAction  {
    public RandomForgeAction() {
        this.target = target;
        this.source = source;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.attackEffect = AbstractGameAction.AttackEffect.LIGHTNING;
    }

    public void update() {

        ArrayList<AbstractCard> possibleCards = new ArrayList();
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.canUpgrade()) {
                possibleCards.add(c);
            }
        }

        if (!possibleCards.isEmpty()) {
            AbstractCard theCard = (AbstractCard)possibleCards.get(AbstractDungeon.miscRng.random(0, possibleCards.size() - 1));
            theCard.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(theCard);
            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(theCard.makeStatEquivalentCopy()));
            this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        }

        this.isDone = true;
    }


}
