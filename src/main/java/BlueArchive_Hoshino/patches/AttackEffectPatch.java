package BlueArchive_Hoshino.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class AttackEffectPatch {
    public AttackEffectPatch() {
    }

    @SpirePatch(
            clz = FlashAtkImgEffect.class,
            method = "playSound"
    )
    public static class sfx {
        public sfx() {
        }

        @SpirePrefixPatch
        public static SpireReturn Prefix(FlashAtkImgEffect e, AbstractGameAction.AttackEffect effect) {
            if (effect == EnumPatch.HOSHINO_SHOTGUN) {
                CardCrawlGame.sound.playV("BlueArchive_Hoshino:Fire", 0.35F);
            } else if (effect == EnumPatch.HOSHINO_SHOTGUN_LIGHT) {
                CardCrawlGame.sound.playV("BlueArchive_Hoshino:FireLight", 0.35F);
            } else if (effect == EnumPatch.HOSHINO_SHOTGUN_HEAVY) {
                CardCrawlGame.sound.playV("BlueArchive_Hoshino:FireHeavy", 0.35F);
            } else {
                return SpireReturn.Continue();
            }
            return SpireReturn.Return((Object)null);
        }
    }

    @SpirePatch(
            clz = FlashAtkImgEffect.class,
            method = "loadImage"
    )
    public static class vfx {
        public vfx() {
        }

        @SpirePrefixPatch
        public static SpireReturn Prefix(FlashAtkImgEffect e, AbstractGameAction.AttackEffect ___effect) {
            if (___effect != EnumPatch.HOSHINO_SHOTGUN && ___effect != EnumPatch.HOSHINO_SHOTGUN_LIGHT && ___effect != EnumPatch.HOSHINO_SHOTGUN_HEAVY) {
                return SpireReturn.Continue();
            } else {
                return SpireReturn.Return(___effect == EnumPatch.HOSHINO_SHOTGUN_HEAVY?ImageMaster.ATK_BLUNT_HEAVY:ImageMaster.ATK_BLUNT_LIGHT);
            }
        }
    }
}
