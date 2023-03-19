package BlueArchive_Aris.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hoshino.DefaultMod.makeArisPowerPath;

//Gain 1 dex for the turn for each card played.

public class NanoMachinePower extends AbstractPower implements CloneablePowerInterface {
    int turn = 1;
    int divide = 1;
    public static final String POWER_ID = DefaultMod.makeArisID("NanoMachinePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makeArisPowerPath("NanoMachinePower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makeArisPowerPath("NanoMachinePower32.png"));

    public NanoMachinePower(final AbstractCreature owner, int divide) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = 0;
        this.divide = divide;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    public void stackPower(int stackAmount) {
        if(stackAmount > divide) {
            this.divide = stackAmount;
        }
        turn = 1;
        updateDescription();
    }
    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(turn > 0) {
            description = DESCRIPTIONS[0] + (amount/divide) + DESCRIPTIONS[1];
        }
        else {
            description = DESCRIPTIONS[2] + (amount/divide) + DESCRIPTIONS[3];
        }
    }

    public void atEndOfTurn(boolean isPlayer) {
        if(isPlayer) {
            if(turn== 0) {
                flash();
                this.addToTop(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.amount/divide));
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
            turn--;
            updateDescription();
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new NanoMachinePower(owner, divide);
    }
}
