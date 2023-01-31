package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class SupporterExpressRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    public static final int HEAL = 5;

    private static boolean usedThisCombat = false;
    // ID, images, text.
    public static final String ID = DefaultMod.makeID("SupporterExpressRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("supporter_express_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("supporter_express_relic.png"));

    public SupporterExpressRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + HEAL + this.DESCRIPTIONS[1] ;
    }


    public void atPreBattle() {
        usedThisCombat = false;
        this.pulse = true;
        this.beginPulse();
    }

    public void wasHPLost(int damageAmount) {
        if (damageAmount > 0 && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !usedThisCombat) {
            this.flash();
            this.pulse = false;
            this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            usedThisCombat = true;
            this.grayscale = true;
        }

    }

    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    public void onVictory() {
        this.pulse = false;
    }
}
