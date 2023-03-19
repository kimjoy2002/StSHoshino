package BlueArchive_Aris.powers;

import BlueArchive_Aris.cards.Shock;
import BlueArchive_Aris.cards.SwordBlast;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;

import static BlueArchive_Hoshino.DefaultMod.makeArisPowerPath;

//Gain 1 dex for the turn for each card played.

public class JobHeroPower extends JobPower implements CloneablePowerInterface {
    public int sword_amout = 1;

    public static final String POWER_ID = DefaultMod.makeArisID(JobHeroPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makeArisPowerPath("JobHeroPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makeArisPowerPath("JobHeroPower32.png"));

    public JobHeroPower(final AbstractCreature owner, AbstractCard equip) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.equip = equip;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.SKILL) {
            this.addToBot(new MakeTempCardInHandAction( new SwordBlast(), sword_amout));
            this.flash();
        }
    }
    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(sword_amout == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + sword_amout + DESCRIPTIONS[2];
        }
    }

    public String getAnimation() {
        return "baseAnimation_Hero";
    }
    public void onJobChange() {
        super.onJobChange();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, 2), 2));
    }

    public void levelUp(){
        flash();
        sword_amout++;
        if(amount == -1) {
            amount = 2;
        }
        else {
            amount++;
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new JobHeroPower(owner, equip);
    }
}
