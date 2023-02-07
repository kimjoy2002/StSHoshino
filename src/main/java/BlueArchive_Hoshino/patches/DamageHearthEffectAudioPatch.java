package BlueArchive_Hoshino.patches;

import BlueArchive_Hoshino.characters.Hoshino;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.DamageHeartEffect;

@SpirePatch(
        clz = DamageHeartEffect.class,
        method = "playSound"
)
public class DamageHearthEffectAudioPatch {
    public DamageHearthEffectAudioPatch() {}

    public static SpireReturn<Void> Prefix(DamageHeartEffect __instance) {
        if (AbstractDungeon.player.chosenClass == Hoshino.Enums.HOSHINO) {
            CardCrawlGame.sound.playV("BlueArchive_Hoshino:Fire", 0.35F);
            return SpireReturn.Return();
        } else {
            return SpireReturn.Continue();
        }
    }
}
