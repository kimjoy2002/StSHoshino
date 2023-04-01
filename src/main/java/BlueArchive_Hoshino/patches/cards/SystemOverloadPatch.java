package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Aris.cards.OverloadCard;
import BlueArchive_Aris.cards.Thoughtsteal;
import BlueArchive_Aris.powers.BalanceBrokenPower;
import BlueArchive_Aris.powers.SystemOverloadPower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.patches.EnumPatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

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
                    if(c.exhaust == false && c.type != AbstractCard.CardType.POWER) {
                        AbstractPower power = AbstractDungeon.player.getPower(SystemOverloadPower.POWER_ID);
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, power.amount));
                        c.exhaust = true;
                        if(!(c instanceof OverloadCard))
                            OverloadCard.overloadThisCombat++;
                        if(AbstractDungeon.player.hasPower(BalanceBrokenPower.POWER_ID)) {
                            AbstractPower op = AbstractDungeon.player.getPower(BalanceBrokenPower.POWER_ID);
                            for(int i = 0; i < op.amount; i++) {
                                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, power.amount));
                                if(!(c instanceof OverloadCard))
                                    OverloadCard.overloadThisCombat++;
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

}
