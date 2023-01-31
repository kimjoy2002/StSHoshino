package BlueArchive_Hoshino.patches.relics;

import BlueArchive_Hoshino.cards.ShuffleCard;
import BlueArchive_Hoshino.patches.power.GameActionManagerPatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CtBehavior;

public class TotemPolePatch {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static String tempString = "";

    @SpirePatch(
            clz = AbstractCard.class,
            method = "initializeDescription"
    )
    public static class totemPolePrePatcher {

        public static void Prefix(AbstractCard __instance) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:TotemPoleRelic")) {
                if (__instance.type == AbstractCard.CardType.CURSE || __instance.type == AbstractCard.CardType.STATUS) {
                    if(tempString.isEmpty()) {
                        tempString = __instance.rawDescription;
                        __instance.rawDescription += TEXT[0] + __instance.misc + TEXT[1];
                    }
                }
            }
        }
    }



    @SpirePatch(
            clz = AbstractCard.class,
            method = "initializeDescription"
    )
    public static class totemPolePostPatcher {

        public static void Postfix(AbstractCard __instance) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:TotemPoleRelic")) {
                if (__instance.type == AbstractCard.CardType.CURSE || __instance.type == AbstractCard.CardType.STATUS) {
                    if(!tempString.isEmpty()) {
                        __instance.rawDescription = tempString;
                        tempString = "";
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez= {
                String.class,
                String.class,
                String.class,
                int.class,
                String.class,
                AbstractCard.CardType.class,
                AbstractCard.CardColor.class,
                AbstractCard.CardRarity.class,
                AbstractCard.CardTarget.class,
                DamageInfo.DamageType.class
            }
    )
    public static class AbstractCardConstructorPatch {
        public static void Postfix(AbstractCard __instance, String v0, String v1, String v2, int v3, String v4, AbstractCard.CardType v5, AbstractCard.CardColor v6, AbstractCard.CardRarity v7, AbstractCard.CardTarget v8, DamageInfo.DamageType v9) {
            if (__instance.type == AbstractCard.CardType.CURSE || __instance.type == AbstractCard.CardType.STATUS) {
                if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:TotemPoleRelic")) {
                    __instance.misc = 2;

                    __instance.initializeDescription();
                }
            }
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:TotemPoleText");
        TEXT = uiStrings.TEXT;
    }
}
