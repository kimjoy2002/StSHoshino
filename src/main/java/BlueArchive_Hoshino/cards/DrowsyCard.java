package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.actions.DrowsyAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public interface DrowsyCard {
    public void makeDescrption();
    public default void whenDrowsyChange(int prev_drowsy){};
    public void onAddToHand();


    public static boolean isDrowsyCard(AbstractCard card) {
        return card instanceof DrowsyCard || isCurseDrowsyCard(card);
    }

    public static boolean isCurseDrowsyCard(AbstractCard card) {
        if (card != null && (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS)) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:TotemPoleRelic")) {
                return true;
            }
        }
        return false;
    }

    public static void whenDrowsyChange(AbstractCard card, int prev_drowsy) {
        if(card instanceof DrowsyCard) {
            ((DrowsyCard)card).whenDrowsyChange(prev_drowsy);
            ((DrowsyCard)card).makeDescrption();
        } else if(isCurseDrowsyCard(card)) {
            if(prev_drowsy != 0 && card.misc == 0) {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
                AbstractDungeon.player.hand.moveToExhaustPile(card);
                CardCrawlGame.dungeon.checkForPactAchievement();
            }
            card.initializeDescription();
        }
    }

    public static void onAddToHand(AbstractCard card) {
        if(card instanceof DrowsyCard) {
            ((DrowsyCard) card).onAddToHand();
        } else if(isCurseDrowsyCard(card)) {
            AbstractDungeon.actionManager.addToBottom(new DrowsyAction(card.uuid, -1));
            card.initializeDescription();
        }

    }
}
