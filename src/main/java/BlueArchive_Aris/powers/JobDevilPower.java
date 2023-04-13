package BlueArchive_Aris.powers;

import BlueArchive_Aris.actions.EquipsmithAction;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hoshino.DefaultMod.makeArisPowerPath;

//Gain 1 dex for the turn for each card played.

public class JobDevilPower extends JobPower implements CloneablePowerInterface {
    public int strong;
    public int lose_power;
    public int lose_power_level;

    public static final String POWER_ID = DefaultMod.makeArisID(JobDevilPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makeArisPowerPath("JobDevilPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makeArisPowerPath("JobDevilPower32.png"));

    public JobDevilPower(final AbstractCreature owner, final int amount, final int lose_power, AbstractCard equip) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.strong = amount;
        this.lose_power = lose_power;
        this.lose_power_level = lose_power;
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
        description = DESCRIPTIONS[0] + lose_power + DESCRIPTIONS[1];
    }
    public String getAnimation() {
        return "baseAnimation_Devil";
    }
    public void onJobChange(boolean withEquip) {
        super.onJobChange(withEquip);
        this.addToBot(new EquipsmithAction(strong, true));
        this.addToBot(new EquipsmithAction(strong, false));
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.flash();
            int amount_ =  this.lose_power;

            if(AbstractDungeon.player.hasPower(LevelUpPower.POWER_ID)) {
                amount_+=this.lose_power*AbstractDungeon.player.getPower(LevelUpPower.POWER_ID).amount;
            }
            this.addToBot(new EquipsmithAction(-amount_, true));
            this.addToBot(new EquipsmithAction(-amount_, false));
        }
    }
    public void levelUp(){
        flash();
        lose_power_level += lose_power;
        if(amount == -1) {
            amount = 2;
        }
        else {
            amount++;
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new JobDevilPower(owner, strong, lose_power, equip);
    }
}
