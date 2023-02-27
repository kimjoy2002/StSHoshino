package BlueArchive_Hoshino.patches.power;

import BlueArchive_Hoshino.powers.KaitengerPower;
import BlueArchive_Hoshino.powers.TauntPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

public class TauntPatch {

    private static final Logger logger = LogManager.getLogger(HodGloryPatch.class.getName());


    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    @SpirePatch(
            clz = AbstractCard.class,
            method = "cardPlayable",
            paramtypez= {
                    AbstractMonster.class
            }
    )
    public static class onModifyPowerPatcher {
        public static SpireReturn Prefix(AbstractCard __instance, AbstractMonster m)
        {
            if (m != null && AbstractDungeon.player.hasPower("BlueArchive_Hoshino:TauntPower")) {
                TauntPower tauntPower = (TauntPower)AbstractDungeon.player.getPower("BlueArchive_Hoshino:TauntPower");
                if(m != tauntPower.source) {

                    Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

                    while(var1.hasNext()) {
                        AbstractMonster m1 = (AbstractMonster) var1.next();
                        if(m1 == tauntPower.source && !m1.isDead && !m1.isDying && !m1.halfDead) {
                            __instance.cantUseMessage = TEXT[0];
                            return SpireReturn.Return((boolean) false);
                        }
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:TauntMessage");
        TEXT = uiStrings.TEXT;
    }
}
