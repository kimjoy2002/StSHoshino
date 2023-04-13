package BlueArchive_Hoshino.patches.power;

import BlueArchive_Aris.cards.OutputCard;
import BlueArchive_Aris.cards.Shock;
import BlueArchive_Aris.powers.AwakeningSupernovaPower;
import BlueArchive_Aris.powers.ShockPower;
import BlueArchive_Aris.relics.Battery;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

import static BlueArchive_Aris.powers.ChargePower.chargeThisCombat;
import static java.lang.Math.min;

public class BatteryPatch {
    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class hasEnoughEnergyPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(ApplyPowerAction __instance) {
            AbstractPower power_ = (AbstractPower) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
            if (AbstractDungeon.player.hasRelic(Battery.ID) && __instance.source != null && __instance.source.isPlayer && __instance.target != __instance.source
                    && power_.ID.equals(ShockPower.POWER_ID)
                    && !__instance.target.hasPower(ShockPower.POWER_ID)
                    && !__instance.target.hasPower("Artifact")) {
                AbstractDungeon.player.getRelic(Battery.ID).onTrigger(__instance.target);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
