package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class WhaleTubeRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    public static final int AMOUNT = 3;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("WhaleTubeRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("whale_tube_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("whale_tube_relic.png"));

    public WhaleTubeRelic() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
    }


    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0]+ AMOUNT + this.DESCRIPTIONS[1];
    }
}
