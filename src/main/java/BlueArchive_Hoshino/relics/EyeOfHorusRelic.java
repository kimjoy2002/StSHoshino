package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class EyeOfHorusRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    public static final int AMOUNT = 2;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("EyeOfHorusRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("eye_of_horus_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("eye_of_horus_relic.png"));

    public EyeOfHorusRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.SOLID);
        counter = 1;
    }


    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof MonsterRoom) {
            this.counter = 1;
            this.pulse = true;
        } else {
            this.counter = 1;
            this.pulse = false;
        }
    }
    public void atTurnStart() {
        this.counter = 1;
        this.pulse = true;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0]+ AMOUNT + this.DESCRIPTIONS[1];
    }
}
