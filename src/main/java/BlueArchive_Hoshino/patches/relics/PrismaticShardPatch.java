package BlueArchive_Hoshino.patches.relics;

import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.ui.BulletUI;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.shop.ShopScreen;

@SpirePatch(
        clz = PrismaticShard.class,
        method = "onEquip"
)
public class PrismaticShardPatch {
    public static void Postfix(PrismaticShard __instance)
    {
        if (!(AbstractDungeon.player instanceof Hoshino)) {
            BulletUI.useBulletBoolean = true;
        }
    }
}
