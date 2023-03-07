package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.DrowsyCard;
import BlueArchive_Hoshino.cards.GozBomb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static BlueArchive_Hoshino.patches.EnumPatch.GOZ_BOMB;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.*;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.STATUS;

public class IntoCardAction extends AbstractGameAction {
    AbstractCard card;
    public IntoCardAction(AbstractCard card) {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = 0;
        this.card = card;
    }


    boolean isRareThen(AbstractCard.CardRarity is, AbstractCard.CardRarity than) {
        if(is == RARE && (than == UNCOMMON || than == COMMON || than == BASIC || than == CURSE))
            return true;
        if(is == UNCOMMON && (than == COMMON || than == BASIC || than == CURSE))
            return true;
        if(is == COMMON && (than == BASIC || than == CURSE))
            return true;
        if(is == BASIC && (than == CURSE))
            return true;
        return false;
    }

    public void update() {
        this.tickDuration();

        if(this.isDone) {
            ArrayList<AbstractCard> tmp = new ArrayList();
            Iterator var4 = AbstractDungeon.player.drawPile.group.iterator();

            while(var4.hasNext()) {
                AbstractCard c = (AbstractCard)var4.next();
                if(c.hasTag(GOZ_BOMB) || c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE || c.cost < 0){
                    continue;
                }
                else if(tmp.isEmpty()) {
                    tmp.add(c);
                }
                else if (c.cost > tmp.get(0).cost) {
                    tmp.clear();
                    tmp.add(c);
                }
                else if (c.cost == tmp.get(0).cost
                        && isRareThen(c.rarity, tmp.get(0).rarity)) {
                    tmp.clear();
                    tmp.add(c);
                }
                else if (c.cost == tmp.get(0).cost && c.rarity == tmp.get(0).rarity) {
                    tmp.add(c);
                }
            }
            if(tmp.isEmpty()) {
                this.isDone = true;
                return;
            }

            AbstractCard target = (AbstractCard)tmp.get(AbstractDungeon.cardRandomRng.random(tmp.size() - 1));


            if(target != null) {
                target.tags.add(GOZ_BOMB);
                target.initializeDescription();
                AbstractDungeon.player.drawPile.removeCard(card);
            }
        }
    }
}
