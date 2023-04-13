package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Aris.cards.BalanceBroken;
import BlueArchive_Aris.cards.OverloadCard;
import BlueArchive_Aris.powers.BalanceBrokenPower;
import BlueArchive_Aris.powers.SystemOverloadPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

import java.util.Iterator;

public class BalanceBrokenPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "renderHand",
            paramtypez={
                SpriteBatch.class
            }
    )
    public static class renderHandPatcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance, SpriteBatch sb) {
            if(__instance.hoveredCard instanceof BalanceBroken) {
                __instance.hoveredCard.calculateCardDamage(null);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredMonster");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
