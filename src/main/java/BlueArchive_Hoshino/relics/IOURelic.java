package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class IOURelic extends CustomRelic implements ClickableRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("IOURelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("IOU_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("IOU_relic.png"));
    public IOURelic() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
        this.counter = 0;
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
    public IOURelic(int gold) {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
        this.counter = gold;
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public void onEnterRoom(AbstractRoom room) {
        if (!this.usedUp) {
            setCounter((int)(counter*1.03f));
            this.description = this.getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
            if (room instanceof ShopRoom) {
                this.flash();
                this.pulse = true;
            } else {
                this.flash();
                this.pulse = false;
            }
        }
    }

    public void setCounter(int setCounter) {
        this.counter = setCounter;
        if (setCounter == -2) {
            this.usedUp();
            this.counter = -2;
        }
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + counter + this.DESCRIPTIONS[1];
    }

    @Override
    public void onRightClick() {
        if(AbstractDungeon.player.gold >= this.counter) {
            AbstractDungeon.player.loseGold(this.counter);
            setCounter(-2);
        }
    }
}
