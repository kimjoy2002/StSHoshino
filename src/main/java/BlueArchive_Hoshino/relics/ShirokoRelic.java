package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class ShirokoRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    public static final int GOLD = 12;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("ShirokoRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("shiroko_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("shiroko_relic.png"));

    public ShirokoRelic() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + GOLD + this.DESCRIPTIONS[1];
    }

    @Override
    public void onVictory() {
        this.flash();
    }
}
