package BlueArchive_Hoshino.patches.power;

import BlueArchive_Aris.cards.OutputCard;
import BlueArchive_Aris.powers.AwakeningSupernovaPower;
import BlueArchive_Aris.powers.FreeCardPower;
import BlueArchive_Hoshino.patches.EnumPatch;
import BlueArchive_Hoshino.patches.cards.JobCardPatch;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
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

public class ChargePatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "hasEnoughEnergy"
    )
    public static class hasEnoughEnergyPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(AbstractCard __instance) {
            //출력 키워드가 있는지 확인
            if(AbstractDungeon.player.hasPower("BlueArchive_Aris:ChargePower") && !(__instance instanceof OutputCard)) {
                AbstractPower cgPower = AbstractDungeon.player.getPower("BlueArchive_Aris:ChargePower");
                if (EnergyPanel.totalCount + cgPower.amount < __instance.costForTurn  && !__instance.freeToPlay() && !__instance.isInAutoplay) {
                    __instance.cantUseMessage = __instance.TEXT[11];
                    return SpireReturn.Return(false);
                }

                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "costForTurn");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard",
            paramtypez = {
                AbstractCard.class,
                AbstractMonster.class,
                int.class
            }
    )
    public static class useCardPatcher {
        public static int chargeThisUse = 0;
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance, AbstractCard c, AbstractMonster m, int energyOnUse) {
            //출력 키워드가 있는지 확인
            if(AbstractDungeon.player.hasPower("BlueArchive_Aris:ChargePower") && !(__instance instanceof OutputCard)) {
                AbstractPower cgPower = AbstractDungeon.player.getPower("BlueArchive_Aris:ChargePower");
                int use_ = c.costForTurn - EnergyPanel.totalCount;
                if(use_ > 0) {
                    int reduceAmount = min(cgPower.amount, use_);
                    if(reduceAmount > 0){
                        chargeThisCombat+=reduceAmount;
                        chargeThisUse=reduceAmount;
                        if(AbstractDungeon.player.hasPower(AwakeningSupernovaPower.POWER_ID)) {
                            ((AwakeningSupernovaPower) AbstractDungeon.player.getPower(AwakeningSupernovaPower.POWER_ID)).onSpend(reduceAmount);
                        }
                    } else {
                        chargeThisUse = 0;
                    }
                    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, "BlueArchive_Aris:ChargePower", use_));
                } else {
                    chargeThisUse = 0;
                }
            } else {
                chargeThisUse = 0;
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(EnergyManager.class, "use");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = EnergyPanel.class,
            method = "render",
            paramtypez = {
                    SpriteBatch.class
            }
    )
    public static class renderPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"energyMsg"}
        )
        public static SpireReturn Insert(EnergyPanel __instance, SpriteBatch sb, String energyMsg) {
            //출력 키워드가 있는지 확인
            if(AbstractDungeon.player.hasPower("BlueArchive_Aris:ChargePower")) {
                Color ENERGY_TEXT_COLOR = (Color) ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "ENERGY_TEXT_COLOR");
                Hitbox tipHitbox = (Hitbox)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "tipHitbox");

                AbstractPower cgPower = AbstractDungeon.player.getPower("BlueArchive_Aris:ChargePower");
                String energyPlus = "  + " + min(99, cgPower.amount);
                FontHelper.renderFontCentered(sb, AbstractDungeon.player.getEnergyNumFont(), energyMsg, __instance.current_x, __instance.current_y, ENERGY_TEXT_COLOR);
                FontHelper.renderFontLeft(sb, AbstractDungeon.player.getEnergyNumFont(), energyPlus, __instance.current_x + FontHelper.layout.width / 2.0F, __instance.current_y, ENERGY_TEXT_COLOR);
                tipHitbox.render(sb);
                if (tipHitbox.hovered && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
                    TipHelper.renderGenericTip(50.0F * Settings.scale, 380.0F * Settings.scale, __instance.LABEL[0], __instance.MSG[0]);
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = EnergyPanel.class,
            method = "renderOrb",
            paramtypez = {
                    SpriteBatch.class
            }
    )
    public static class renderOrbPatcher {
        public static SpireReturn Prefix(EnergyPanel __instance, SpriteBatch sb) {
            if ( __instance.totalCount == 0 && AbstractDungeon.player.hasPower("BlueArchive_Aris:ChargePower")) {
                AbstractDungeon.player.renderOrb(sb, true, __instance.current_x, __instance.current_y);
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }

    }
}
