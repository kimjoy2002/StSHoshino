package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Hoshino.cards.DrowsyCard;
import BlueArchive_Hoshino.cards.OnDeckCard;
import BlueArchive_Hoshino.patches.power.ShieldPatch;
import BlueArchive_Hoshino.relics.WhaleTubeRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CardGroupPatch {

    private static final Logger logger = LogManager.getLogger(ShieldPatch.class.getName());


    @SpirePatch(
            clz = CardGroup.class,
            method = "addToHand",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class addToHandPatcher {

        public static void Postfix(CardGroup __instance, AbstractCard card)
        {
            if(AbstractDungeon.currMapNode != null &&
                AbstractDungeon.getCurrRoom() != null &&
                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                __instance.type == CardGroup.CardGroupType.HAND &&
                DrowsyCard.isDrowsyCard(card)){
                DrowsyCard.onAddToHand(card);
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "addToTop",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class addToTopPatcher {

        public static void Postfix(CardGroup __instance, AbstractCard card)
        {
            if(AbstractDungeon.currMapNode != null &&
                    AbstractDungeon.getCurrRoom() != null &&
                    AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
                if(__instance.type == CardGroup.CardGroupType.HAND &&
                        DrowsyCard.isDrowsyCard(card)){
                    DrowsyCard.onAddToHand(card);
                } else if(__instance.type == CardGroup.CardGroupType.DRAW_PILE &&
                        (card instanceof OnDeckCard || AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:WhaleTubeRelic"))) {
                    boolean isSystemShuffle = false;
                    StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
                    for(StackTraceElement trace : stackTraces) {
                        if(trace.getClassName().equals(EmptyDeckShuffleAction.class.getName())
                        || trace.getMethodName().equals("initializeDeck")){
                            isSystemShuffle = true;
                            break;
                        }
                    }
                    if(!isSystemShuffle && card instanceof OnDeckCard) {
                        ((OnDeckCard)card).onDeck();
                    }
                    if(!isSystemShuffle && AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:WhaleTubeRelic")) {
                        AbstractDungeon.player.getRelic("BlueArchive_Hoshino:WhaleTubeRelic").flash();
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, WhaleTubeRelic.AMOUNT));
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "addToBottom",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class addToBottomPatcher {

        public static void Postfix(CardGroup __instance, AbstractCard card)
        {
            if(AbstractDungeon.currMapNode != null &&
                AbstractDungeon.getCurrRoom() != null &&
                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
                if(__instance.type == CardGroup.CardGroupType.HAND &&
                    DrowsyCard.isDrowsyCard(card)){
                    DrowsyCard.onAddToHand(card);
                }
                if(__instance.type == CardGroup.CardGroupType.DRAW_PILE &&
                       card instanceof OnDeckCard) {
                    ((OnDeckCard) card).onDeck();
                }
                if(__instance.type == CardGroup.CardGroupType.DRAW_PILE &&
                        AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:WhaleTubeRelic")) {
                    AbstractDungeon.player.getRelic("BlueArchive_Hoshino:WhaleTubeRelic").flash();
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, WhaleTubeRelic.AMOUNT));
                }
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "addToRandomSpot",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class addToRandomSpotPatcher {

        public static void Postfix(CardGroup __instance, AbstractCard card)
        {
            if(AbstractDungeon.currMapNode != null &&
                AbstractDungeon.getCurrRoom() != null &&
                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
                if(__instance.type == CardGroup.CardGroupType.HAND &&
                        DrowsyCard.isDrowsyCard(card)){
                    DrowsyCard.onAddToHand(card);
                }
                if(__instance.type == CardGroup.CardGroupType.DRAW_PILE &&
                       card instanceof OnDeckCard) {
                    ((OnDeckCard) card).onDeck();
                }
                if(__instance.type == CardGroup.CardGroupType.DRAW_PILE &&
                        AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:WhaleTubeRelic")) {
                    AbstractDungeon.player.getRelic("BlueArchive_Hoshino:WhaleTubeRelic").flash();
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, WhaleTubeRelic.AMOUNT));
                }
            }
        }
    }
}