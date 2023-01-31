package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.BulletVigorPower;
import BlueArchive_Hoshino.powers.PlaceboPower;
import BlueArchive_Hoshino.powers.ExpertPower;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class SerikaRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    private boolean firstTurn = true;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("SerikaRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("serika_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("serika_relic.png"));

    public SerikaRelic() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    public void atPreBattle() {
        this.firstTurn = true;
    }

    public void randomEffect() {

        switch(AbstractDungeon.cardRandomRng.random(8)) {
            case 0:
                this.addToTop(new GainEnergyAction(1));
                break;
            case 1:
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
                break;
            case 2:
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
                break;
            case 3:
                AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, 1));
                break;
            case 4:
                AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, 2));
                break;
            case 5:
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegenPower(AbstractDungeon.player, 2), 2));
                break;
            case 6:
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BulletVigorPower(AbstractDungeon.player, 2), 2));
                break;
            case 7:
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ExpertPower(AbstractDungeon.player, AbstractDungeon.player, 1), 1));
                break;
            default:
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlaceboPower(AbstractDungeon.player, AbstractDungeon.player, 0), 0));
                break;
        }

    }


    public void atTurnStart() {
        if (this.firstTurn) {
            this.flash();
            randomEffect();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.firstTurn = false;
            this.grayscale = true;
        }
    }

    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }
}
