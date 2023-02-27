package BlueArchive_Hoshino.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class KaitenMagicCirclePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public int circle_color= 0;

    public static final String POWER_ID = DefaultMod.makeID("KaitenMagicCirclePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("KaitenMagicCirclePower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("KaitenMagicCirclePower32.png"));

    public KaitenMagicCirclePower(final AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.source = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL && circle_color==0 ? damage * 1.3F : damage;
    }

    public void atEndOfRound() {
        if (circle_color==2) {
            this.flash();
            this.addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 3), 3));
        }
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = circle_color ==0?DESCRIPTIONS[0]:(circle_color==1?DESCRIPTIONS[1]:DESCRIPTIONS[2]);
        description += DESCRIPTIONS[3] + amount + DESCRIPTIONS[4];
    }

    @Override
    public AbstractPower makeCopy() {
        return new KaitenMagicCirclePower(owner, amount);
    }
}
