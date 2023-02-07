package BlueArchive_Hoshino.patches.power;

import BlueArchive_Hoshino.cards.ShuffleCard;
import BlueArchive_Hoshino.powers.FreeReloadPower;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

public class GameActionManagerPatch {

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class getNextActionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {

            ShuffleCard.totalShuffledThisTurn.set(0);
            BulletSubscriber.reloadedThisTurn.set(0);
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnRelics");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class ShuffleActionPatch {
        public static void Postfix(GameActionManager __instance)
        {
            ShuffleCard.totalShuffledThisTurn.set(0);
            BulletSubscriber.reloadedThisTurn.set(0);
            BulletSubscriber.reloadedThisCombat = 0;
            FreeReloadPower.freeReload = false;
        }
    }

}
