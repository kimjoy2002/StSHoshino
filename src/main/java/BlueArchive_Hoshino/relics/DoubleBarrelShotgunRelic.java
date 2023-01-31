package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.BulletModifyAction;
import BlueArchive_Hoshino.actions.MaxBulletModifyAction;
import BlueArchive_Hoshino.powers.ShieldPower;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class DoubleBarrelShotgunRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    public static final int AMOUT = 2;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("DoubleBarrelShotgunRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("double_barrel_shotgun_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("double_barrel_shotgun_relic.png"));

    public DoubleBarrelShotgunRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.CLINK);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUT + this.DESCRIPTIONS[1] ;
    }

    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new MaxBulletModifyAction(-AMOUT));
        this.addToBot(new BulletModifyAction(-AMOUT));
    }

    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }


}
