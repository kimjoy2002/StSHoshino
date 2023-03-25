package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.UUID;

public class NonomiRelicAction extends AbstractGameAction {
    private UUID uuid;

    public NonomiRelicAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        int toDraw = 10 - AbstractDungeon.player.hand.size();
        for (int i = 0; i < toDraw; ++i) {
            AbstractCard card = generateAnyCard(false);
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, true));
        }
        this.isDone = true;
    }
    public static AbstractCard generateAnyCard(boolean common) {
        AbstractCard tmp;
        while(true) {
            int roll = AbstractDungeon.cardRandomRng.random(99);
            AbstractCard.CardRarity cardRarity;
            if (common || roll < 55) {
                cardRarity = AbstractCard.CardRarity.COMMON;
            } else if (roll < 85) {
                cardRarity = AbstractCard.CardRarity.UNCOMMON;
            } else {
                cardRarity = AbstractCard.CardRarity.RARE;
            }

            tmp = CardLibrary.getAnyColorCard(cardRarity);
            if(tmp != null && tmp.color != Hoshino.Enums.COLOR_PINK
                    && tmp.color != AbstractCard.CardColor.COLORLESS
                    && tmp.color != AbstractCard.CardColor.CURSE)
                break;
        }

        return tmp.makeCopy();
    }
}
