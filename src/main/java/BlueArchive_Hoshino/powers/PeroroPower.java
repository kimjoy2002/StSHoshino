package BlueArchive_Hoshino.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class PeroroPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public AbstractCreature perorodzilla;

    public final int damage = 50;
    public final int str_power = (AbstractDungeon.ascensionLevel >= 19)?3:2;

    public static final String POWER_ID = DefaultMod.makeID("PeroroPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("PeroroPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("PeroroPower32.png"));

    public PeroroPower(final AbstractCreature owner, final AbstractCreature source, AbstractCreature perorodzilla) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.source = source;
        this.perorodzilla = perorodzilla;

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
        description = DESCRIPTIONS[0] + damage + DESCRIPTIONS[1] + str_power + DESCRIPTIONS[2];
    }

    public void onDeath() {
        AbstractDungeon.actionManager.addToTop(
                new DamageAction(perorodzilla, new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.SMASH));
    }
    @Override
    public AbstractPower makeCopy() {
        return new PeroroPower(owner, source, perorodzilla);
    }
}
