package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.BulletModifyAction;
import BlueArchive_Hoshino.actions.MaxBulletModifyAction;
import BlueArchive_Hoshino.actions.ReloadAction;
import BlueArchive_Hoshino.powers.FreeReloadPower;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class AutoReloaderRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    public static final int AMOUT = 3;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("AutoReloaderRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("autoreloader_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("autoreloader_relic.png"));

    public AutoReloaderRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FreeReloadPower(AbstractDungeon.player,AbstractDungeon.player,99), 99));
    }

}
