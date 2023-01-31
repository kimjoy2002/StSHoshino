package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class GoldCardRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("GoldCardRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("gold_card_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("gold_card_relic.png"));

    public GoldCardRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    public void onEnterRoom(AbstractRoom room) {
        if (!this.usedUp) {
            if (room instanceof ShopRoom) {
                this.flash();
                this.pulse = true;
            } else {
                this.pulse = false;
            }
        }
    }

    public void onSpendGold() {
        if (!this.usedUp) {
            this.flash();
            this.setCounter(-2);
        }
    }

    public void setCounter(int setCounter) {
        this.counter = setCounter;
        if (setCounter == -2) {
            this.usedUp();
            this.counter = -2;
        }

    }

    public boolean canSpawn() {
        return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
    }


}
