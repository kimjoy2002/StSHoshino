package BlueArchive_Hoshino.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class DelayedPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;


    public static final String POWER_ID = DefaultMod.makeID("DelayedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DelayedPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DelayedPower32.png"));

    private static int afterPowerIdOffset;
    AbstractPower delayedPower;
    public DelayedPower(final AbstractCreature owner, final AbstractCreature source, AbstractPower delayedPower) {
        name = NAME;
        ID = POWER_ID + afterPowerIdOffset;
        ++afterPowerIdOffset;

        this.owner = owner;
        this.source = source;
        this.delayedPower = delayedPower;

        type = delayedPower.type;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        this.addToBot(new ApplyPowerAction(owner, source, delayedPower));
    }

    public void atStartOfTurn() {
        this.flash();
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        this.addToBot(new ApplyPowerAction(owner, source, delayedPower));
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + delayedPower.name + DESCRIPTIONS[1];
    }


    @Override
    public AbstractPower makeCopy() {
        return new DelayedPower(owner, source, delayedPower);
    }
}
