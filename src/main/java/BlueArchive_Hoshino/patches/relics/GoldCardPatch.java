package BlueArchive_Hoshino.patches.relics;

import BlueArchive_Hoshino.patches.cards.LeaveItToMePatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CtBehavior;

public class GoldCardPatch {

    @SpirePatch(
            clz = ShopScreen.class,
            method = "purchaseCard",
            paramtypez={
                    AbstractCard.class
            }
    )
    public static class PurchaseCardPatcher {
        public static void Prefix(ShopScreen __instance, AbstractCard card_) {
            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:GoldCardRelic")) {
                AbstractRelic relic = AbstractDungeon.player.getRelic("BlueArchive_Hoshino:GoldCardRelic");
                if (!relic.usedUp) {
                    relic.flash();
                    card_.price = 0;
                    relic.setCounter(-2);
                    relic.stopPulse();
                }
            }
        }
    }

    @SpirePatch(
            clz = StoreRelic.class,
            method = "purchaseRelic"
    )
    public static class PurchaseRelicPatcher {
        public static void Prefix(StoreRelic __instance) {
            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:GoldCardRelic")) {
                AbstractRelic relic = AbstractDungeon.player.getRelic("BlueArchive_Hoshino:GoldCardRelic");
                if (!relic.usedUp) {
                    relic.flash();
                    __instance.price = 0;
                    relic.setCounter(-2);
                    relic.stopPulse();
                }
            }
        }
    }

    @SpirePatch(
            clz = StorePotion.class,
            method = "purchasePotion"
    )
    public static class PurchasePotionPatcher {
        public static void Prefix(StorePotion __instance) {
            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:GoldCardRelic")) {
                AbstractRelic relic = AbstractDungeon.player.getRelic("BlueArchive_Hoshino:GoldCardRelic");
                if (!relic.usedUp) {
                    relic.flash();
                    __instance.price = 0;
                    relic.setCounter(-2);
                    relic.stopPulse();
                }
            }
        }
    }

    static int temp_purge = -1;
    @SpirePatch(
            clz = ShopScreen.class,
            method = "purchasePurge"
    )
    public static class PurchasePurgePatcher {
        public static void Prefix(ShopScreen __instance) {
            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:GoldCardRelic")) {
                AbstractRelic relic = AbstractDungeon.player.getRelic("BlueArchive_Hoshino:GoldCardRelic");
                if (!relic.usedUp) {
                    temp_purge = __instance.actualPurgeCost;
                    __instance.actualPurgeCost = 0;
                }
            }
        }
    }

    @SpirePatch(
            clz = ShopScreen.class,
            method = "purchasePurge"
    )
    public static class PurchasePurgePatcher2 {
        public static void Postfix(ShopScreen __instance) {
            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:GoldCardRelic")) {
                AbstractRelic relic = AbstractDungeon.player.getRelic("BlueArchive_Hoshino:GoldCardRelic");
                if (!relic.usedUp && temp_purge != -1) {
                    __instance.actualPurgeCost = temp_purge;
                    temp_purge = -1;
                }
            }
        }
    }

    @SpirePatch(
            clz = ShopScreen.class,
            method = "purgeCard"
    )
    public static class PurgeCardPatcher {

        public static void Prefix() {
            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:GoldCardRelic")) {
                AbstractRelic relic = AbstractDungeon.player.getRelic("BlueArchive_Hoshino:GoldCardRelic");
                if (!relic.usedUp) {
                    relic.flash();
                    ShopScreen.actualPurgeCost = 0;
                    relic.setCounter(-2);
                    relic.stopPulse();
                }
            }
        }
    }
}
