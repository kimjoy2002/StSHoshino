package BlueArchive_Hoshino.patches.power;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Iterator;

import static java.lang.Math.max;


public class ShieldPatch {
    private static final Logger logger = LogManager.getLogger(ShieldPatch.class.getName());


    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class getNextActionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {
            if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:ShieldPower")) {


                boolean isBarricade  = true;
                Iterator var2 = AbstractDungeon.player.powers.iterator();
                AbstractPower p;
                do {
                    if (!var2.hasNext()) {
                        isBarricade = false;
                        break;
                    }
                    p = (AbstractPower)var2.next();
                } while(!p.ID.equals("Barricade"));

                isBarricade = isBarricade || AbstractDungeon.player.hasPower("Blur");
                if(isBarricade)
                    return;
                AbstractPower sdPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:ShieldPower");
                if (AbstractDungeon.player.hasRelic("Calipers")) {
                    int maxBlock = max(AbstractDungeon.player.currentBlock - 15, sdPower.amount);
                    if(AbstractDungeon.player.currentBlock - maxBlock > 0) {
                        AbstractDungeon.player.loseBlock(AbstractDungeon.player.currentBlock - maxBlock);
                    }
                    sdPower.flash();
                } else {
                    if(AbstractDungeon.player.currentBlock - sdPower.amount > 0) {
                        AbstractDungeon.player.loseBlock(AbstractDungeon.player.currentBlock - sdPower.amount);
                    }
                    sdPower.flash();
                }

            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPower");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }




    @SpirePatch(
            clz = AbstractCreature.class,
            method = "hasPower",
            paramtypez= {
                String.class
            }
    )
    public static  class hasPowerPatcher {
        public static boolean Postfix(boolean __result, AbstractCreature __instance, String targetID)
        {
            if(__result == false && targetID.equals("Barricade")){
                return __instance.hasPower("BlueArchive_Hoshino:ShieldPower");
            }
            return __result;
        }
    }
}
