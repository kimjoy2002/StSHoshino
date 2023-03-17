package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Hoshino.cards.LeaveItToMe;
import BlueArchive_Hoshino.cards.QuickShotAttack;
import BlueArchive_Hoshino.patches.EnumPatch;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import javassist.CtBehavior;

import java.util.Iterator;

public class JobCardPatch {
    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class useCardActionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(UseCardAction __instance) {
            AbstractCard targetCard = (AbstractCard) ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
            if(targetCard.hasTag(EnumPatch.EQUIPMENT)) {
                targetCard.exhaustOnUseOnce = false;
                targetCard.dontTriggerOnUseCard = false;
                AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
                __instance.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(UseCardAction.class, "reboundCard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
