package BlueArchive_Hoshino.patches.relics;

import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.ui.BulletUI;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.PrismaticShard;

@SpirePatch(
        clz = CardCrawlGame.class,
        method = "loadPlayerSave"
)
public class PrismaticShardLoadPatch {
    public static void Postfix(CardCrawlGame __instance)
    {
        if (!(AbstractDungeon.player instanceof Hoshino) && AbstractDungeon.player.hasRelic("PrismaticShard")) {
            BulletUI.useBulletBoolean = true;
        }
    }
}
