package BlueArchive_Hoshino.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.monsters.act3.boss.GozBunsin;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;
import static BlueArchive_Hoshino.monsters.act3.boss.Goz.BUNSHIN_HP;

//Gain 1 dex for the turn for each card played.

public class ChesedMinionPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = DefaultMod.makeID("ChesedMinionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ChesedMinionPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ChesedMinionPower32.png"));

    public ChesedMinionPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        float damage_ = damage/10.0F;
        if(damage_ < 1.0F)
            damage_ = 1.0F;
        return damage_;
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ChesedMinionPower(owner, amount);
    }
}
