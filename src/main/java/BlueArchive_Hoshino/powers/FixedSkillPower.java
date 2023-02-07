package BlueArchive_Hoshino.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.FixedSkillGetAction;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class FixedSkillPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("FixedSkillPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("FixedSkillPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("FixedSkillPower32.png"));

    public CardGroup tmp;



    private static int fixedSkillIdOffset;

    public FixedSkillPower(final AbstractCreature owner, final AbstractCreature source, CardGroup tmp) {
        name = NAME;
        ID = POWER_ID + fixedSkillIdOffset;
        ++fixedSkillIdOffset;

        this.owner = owner;
        this.tmp = tmp;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void atStartOfTurnPostDraw() {
        this.flash();
        this.addToBot(new FixedSkillGetAction(this, tmp));
    }



    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
        boolean first = true;
        for (Iterator<AbstractCard> it = tmp.group.iterator(); it.hasNext(); ) {
            if(!first)
                description += " , ";
            AbstractCard c = (AbstractCard) it.next();
            description += "#y";
            description += c.name.replace(" ", " #y");
            first = false;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new FixedSkillPower(owner, source, tmp);
    }
}
