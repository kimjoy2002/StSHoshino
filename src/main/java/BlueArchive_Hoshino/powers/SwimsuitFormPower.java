package BlueArchive_Hoshino.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.SwimsuitFormAction;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class SwimsuitFormPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("SwimsuitFormPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("SwimsuitFormPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("SwimsuitFormPower32.png"));

    public SwimsuitFormPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        if(AbstractDungeon.player instanceof Hoshino) {
            AnimationState.TrackEntry e = AbstractDungeon.player.state.setAnimation(0, Hoshino.getNutralTextureName(true , false), true);
        }
        updateDescription();
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    public void atStartOfTurnPostDraw() {
        this.flash();
        this.addToBot(new SwimsuitFormAction(this.amount));
    }

    public void onVictory() {
        if(AbstractDungeon.player instanceof Hoshino) {
            AnimationState.TrackEntry e = AbstractDungeon.player.state.addAnimation(0, Hoshino.getReleaseSwimSuitTextureName(), false, 0.0f);
            AbstractDungeon.player.state.addAnimation(0, Hoshino.getNutralTextureName(false, true), true, e.getEndTime());
        }
    }

    public void onRemove() {
        if(AbstractDungeon.player instanceof Hoshino) {
            AnimationState.TrackEntry e = AbstractDungeon.player.state.addAnimation(0, Hoshino.getReleaseSwimSuitTextureName(), false, 0.0f);
            AbstractDungeon.player.state.addAnimation(0, Hoshino.getNutralTextureName(false, true), true, e.getEndTime());
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SwimsuitFormPower(owner, source, amount);
    }
}
