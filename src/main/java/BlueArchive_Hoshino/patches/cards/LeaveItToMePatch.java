package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Hoshino.cards.DrowsyCard;
import BlueArchive_Hoshino.cards.LeaveItToMe;
import BlueArchive_Hoshino.patches.power.ShieldPatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import javassist.CtBehavior;

import java.util.Iterator;

import static java.lang.Math.max;

public class LeaveItToMePatch {

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "nextRoomTransition",
            paramtypez= {
                    SaveFile.class
            }
    )
    public static class nextRoomTransitionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SaveFile v0) {
            Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                if (c instanceof LeaveItToMe) {
                    c.misc+=c.magicNumber;
                    c.applyPowers();
                    c.initializeDescription();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "resetPlayer");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}
