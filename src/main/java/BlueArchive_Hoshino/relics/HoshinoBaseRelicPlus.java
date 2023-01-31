package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.ExpertPower;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class HoshinoBaseRelicPlus extends CustomRelic  implements ReloadRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    public static final int BLOCK = 5;
    public static final int MAGIC = 5;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("HoshinoBaseRelicPlus");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HoshinoBasePlusRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HoshinoBasePlusRelic.png"));

    public HoshinoBaseRelicPlus() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + BLOCK + this.DESCRIPTIONS[1] + MAGIC + this.DESCRIPTIONS[2];
    }

    public void obtain() {
        if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:HoshinoBaseRelic")) {
            for(int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (((AbstractRelic)AbstractDungeon.player.relics.get(i)).relicId.equals("BlueArchive_Hoshino:HoshinoBaseRelic")) {
                    this.instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }

    }

    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new ExpertPower(AbstractDungeon.player, AbstractDungeon.player, BLOCK), BLOCK));
    }

    public void onReload(){

    }
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:HoshinoBaseRelic");
    }



}
