package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Aris.cards.AttackedCard;
import BlueArchive_Hoshino.patches.EnumPatch;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.Iterator;

public class ReactiveArmourPatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage",
            paramtypez = {
                    DamageInfo.class
            }
    )
    public static class damagePatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"damageAmount"}
        )
        public static void Insert(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
            if (info.owner != null) {
                Iterator iter = __instance.discardPile.group.iterator();

                while(iter.hasNext()) {
                    AbstractCard c = (AbstractCard)iter.next();
                    if (c instanceof  AttackedCard){
                        ((AttackedCard)c).onAttacked(info, damageAmount);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "lastDamageTaken");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
