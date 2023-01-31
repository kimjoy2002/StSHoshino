package BlueArchive_Hoshino.patches.power;

import BlueArchive_Hoshino.relics.ShirokoRelic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

import static java.lang.Math.max;


public class GoldWhalePatch {
    private static final Logger logger = LogManager.getLogger(GoldWhalePatch.class.getName());


    @SpirePatch(
            clz = AbstractRoom.class,
            method = "update"
    )
    public static class dropRewardPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractRoom __instance) {
            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:ShirokoRelic")) {
                __instance.addGoldToRewards(ShirokoRelic.GOLD);
            }

            if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:GoldWhalePower")) {
                AbstractPower gwPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:GoldWhalePower");
                for(int i = 0; i < gwPower.amount; i++) {
                    __instance.rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier())));
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "dropReward");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }



}
