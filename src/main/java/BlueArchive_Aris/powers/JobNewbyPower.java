package BlueArchive_Aris.powers;

import BlueArchive_Aris.cards.SuperNova;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hoshino.DefaultMod.makeArisPowerPath;

//Gain 1 dex for the turn for each card played.

public class JobNewbyPower extends JobPower implements CloneablePowerInterface {
    public int draw_power;

    public static final String POWER_ID = DefaultMod.makeArisID(JobNewbyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makeArisPowerPath("JobNewbyPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makeArisPowerPath("JobNewbyPower32.png"));

    public JobNewbyPower(final AbstractCreature owner, final int amount
          , AbstractCard equip) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.draw_power = amount;
        this.equip = equip;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }
    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
    public String getAnimation() {
        return "baseAnimation_Newby";
    }
    public void onJobChange() {
        super.onJobChange();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(owner, draw_power));
    }

    public void levelUp(){
        flash();
        if(amount == -1) {
            amount = 2;
        }
        else {
            amount++;
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new JobNewbyPower(owner, draw_power, equip);
    }
}
