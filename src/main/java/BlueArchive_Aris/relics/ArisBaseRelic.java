package BlueArchive_Aris.relics;

import BlueArchive_Aris.powers.ChargePower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.ExpertPower;
import BlueArchive_Hoshino.relics.ReloadRelic;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.*;

public class ArisBaseRelic extends CustomRelic implements ClassChangeRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeArisID("ArisBaseRelic");

    public static final int AMOUNT = 1;
    private static boolean usedThisCombat = false;
    private static final Texture IMG = TextureLoader.getTexture(makeArisRelicPath("ArisBaseRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeArisRelicOutlinePath("ArisBaseRelic.png"));

    public ArisBaseRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }

    public void atPreBattle() {
        usedThisCombat = false;
    }
    @Override
    public void onClassChange() {
        if(!usedThisCombat) {
            flash();
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, AMOUNT), AMOUNT));
            this.grayscale = true;
            usedThisCombat = true;
        }
    }
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }
}
