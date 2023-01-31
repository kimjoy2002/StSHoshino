package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.powers.ExpertPower;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class HoshinoBaseRelic extends CustomRelic implements ReloadRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    public static final int BLOCK = 3;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("HoshinoBaseRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HoshinoBaseRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HoshinoBaseRelic.png"));

    public HoshinoBaseRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + BLOCK + this.DESCRIPTIONS[1];
    }

    public void onReload(){

    }

    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new ExpertPower(AbstractDungeon.player, AbstractDungeon.player, BLOCK), BLOCK));
    }
}
