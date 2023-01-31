package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.ExpertPower;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class TacticalSatchelBagRelic extends CustomRelic implements ShotRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    public static final int RELOAD = 7;
    public static final int SKILLED = 1;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("TacticalSatchelBagRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("tactical_satchel_bag_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("tactical_satchel_bag_relic.png"));

    public TacticalSatchelBagRelic() {

        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);

        this.counter = 0;
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + RELOAD + this.DESCRIPTIONS[1] + SKILLED + this.DESCRIPTIONS[2];
    }

    public void onBulletUse(int amount){
        for(int i = 0; i < amount; ++i) {
            counter++;
            if(RELOAD <= counter){
                flash();
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ExpertPower(AbstractDungeon.player, AbstractDungeon.player, SKILLED), SKILLED));
                counter=0;
            }
        }
    }
}
