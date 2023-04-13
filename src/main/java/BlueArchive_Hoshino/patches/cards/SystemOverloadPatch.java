package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Aris.cards.AbstractDynamicCard;
import BlueArchive_Aris.cards.OverloadCard;
import BlueArchive_Aris.cards.Thoughtsteal;
import BlueArchive_Aris.powers.BalanceBrokenPower;
import BlueArchive_Aris.powers.SystemOverloadPower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.QuickShotAttack;
import BlueArchive_Hoshino.patches.EnumPatch;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

import java.util.Iterator;

public class SystemOverloadPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard",
            paramtypez={
                AbstractCard.class,
                AbstractMonster.class,
                int.class
            }
    )
    public static class useCardPatcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance, AbstractCard c) {

            if (EnergyPanel.totalCount <= c.costForTurn) {
                if(AbstractDungeon.player.hasPower(SystemOverloadPower.POWER_ID)) {
                    {
                        AbstractPower power = AbstractDungeon.player.getPower(SystemOverloadPower.POWER_ID);
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, power.amount));
                        c.exhaust = true;
                        if(!(c instanceof OverloadCard))
                            OverloadCard.overloadCheck();
                        if(AbstractDungeon.player.hasPower(BalanceBrokenPower.POWER_ID)) {
                            AbstractPower op = AbstractDungeon.player.getPower(BalanceBrokenPower.POWER_ID);
                            for(int i = 0; i < op.amount; i++) {
                                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, power.amount));
                                if(!(c instanceof OverloadCard))
                                    OverloadCard.overloadCheck();
                            }
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz=AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class CardGrowField
    {
        public static SpireField<Color> before_color = new SpireField<>(() -> new Color());
        public static SpireField<Boolean> color_modify = new SpireField<>(() -> false);
    }


    @SpirePatch(
            clz = CardGroup.class,
            method = "glowCheck"
    )
    public static class triggerOnGlowCheckPatcher {

        public static void Prefix(CardGroup __instance)
        {
            if(AbstractDungeon.player.hasPower(SystemOverloadPower.POWER_ID)) {

                AbstractCard c;
                for(Iterator var1 = __instance.group.iterator(); var1.hasNext();) {
                    c = (AbstractCard)var1.next();
                    if (EnergyPanel.totalCount <= c.costForTurn) {
                        Boolean isModify = CardGrowField.color_modify.get(c);
                        if(!isModify.booleanValue()) {
                            Color color_ = (Color) ReflectionHacks.getPrivate(c, AbstractCard.class, "GOLD_BORDER_GLOW_COLOR");
                            CardGrowField.before_color.set(c, c.glowColor);
                            CardGrowField.color_modify.set(c, true);
                            c.glowColor = color_.cpy();
                        }
                    }
                }
            } else {
                AbstractCard c;
                for(Iterator var1 = __instance.group.iterator(); var1.hasNext();) {
                    c = (AbstractCard)var1.next();
                    Boolean isModify = CardGrowField.color_modify.get(c);
                    if(isModify.booleanValue()) {
                        c.glowColor = CardGrowField.before_color.get(c);
                        CardGrowField.color_modify.set(c, false);
                    }
                }
            }
        }
    }
}
