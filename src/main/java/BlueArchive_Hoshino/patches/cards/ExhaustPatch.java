package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Aris.cards.Thoughtsteal;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.patches.EnumPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class ExhaustPatch {
    private static String tempString = "";
    public static final String ID = DefaultMod.makeArisID(Thoughtsteal.class.getSimpleName());


    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    @SpirePatch(
            clz = AbstractCard.class,
            method = "initializeDescription"
    )
    public static class exhaustPrePatcher {

        public static void Prefix(AbstractCard __instance) {
            if (__instance.hasTag(EnumPatch.EXHAUST)) {
                if(tempString.isEmpty()) {
                    tempString = __instance.rawDescription;
                    __instance.rawDescription = __instance.rawDescription + cardStrings.EXTENDED_DESCRIPTION[1];
                }
            }

        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "initializeDescription"
    )
    public static class exhaustPostPatcher {

        public static void Postfix(AbstractCard __instance) {
            if (__instance.hasTag(EnumPatch.EXHAUST)) {
                if(!tempString.isEmpty()) {
                    __instance.rawDescription = tempString;
                    tempString = "";
                }
            }
        }
    }
}
