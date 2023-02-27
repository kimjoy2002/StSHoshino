package BlueArchive_Hoshino.patches;

import BlueArchive_Hoshino.patches.power.ShieldPatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;


import static BlueArchive_Hoshino.DefaultMod.getModID;
import static BlueArchive_Hoshino.DefaultMod.onlyBluearchiveBoss;
import static java.lang.Math.max;

public class BlueArchiveBossPatch {

    private static final Logger logger = LogManager.getLogger(BlueArchiveBossPatch.class.getName());


    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "<ctor>",
            paramtypez = {String.class, String.class, AbstractPlayer.class, ArrayList.class}
    )
    public static class abstractDungeonPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon __instance, String name, String levelId, AbstractPlayer p, ArrayList<String> newSpecialOneTimeEventList) {
            if(levelId == "TheEnding") {
                if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:PeroroRelic")) {
                    ArrayList<String> replaceBoss = new ArrayList<String>();
                    for(String boss : __instance.bossList) {
                        if(boss.startsWith(getModID())){
                            replaceBoss.add(boss);
                        }
                    }
                    if(replaceBoss.size() >= 1) {
                        __instance.bossList = replaceBoss;
                    }
                } else {
                    ArrayList<String> replaceBoss = new ArrayList<String>();
                    for(String boss : __instance.bossList) {
                        if(boss.startsWith(getModID())){
                            __instance.bossList.remove(boss);
                            break;
                        }
                    }
                }
            }
            else if(onlyBluearchiveBoss){
                ArrayList<String> replaceBoss = new ArrayList<String>();
                for(String boss : __instance.bossList) {
                    if(boss.startsWith(getModID())){
                        replaceBoss.add(boss);
                    }
                }
                if(replaceBoss.size() >= 1) {
                    __instance.bossList = replaceBoss;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "setBoss");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
