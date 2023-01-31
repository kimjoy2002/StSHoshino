package BlueArchive_Hoshino.patches.relics;

import BlueArchive_Hoshino.cards.ShuffleCard;
import BlueArchive_Hoshino.powers.RefillAmmoPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.util.Iterator;

@SpirePatch(
        clz = ShopScreen.class,
        method = "init"
)
public class ShopPatch {
    public static void Postfix(ShopScreen __instance)
    {
        if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:IOURelic")) {
            AbstractRelic relic = AbstractDungeon.player.getRelic("BlueArchive_Hoshino:IOURelic");
            if(!relic.usedUp) {
                __instance.applyDiscount(1.2F, true);
            }
        }
    }
}
