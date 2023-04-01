package BlueArchive_Aris.powers;

import BlueArchive_Aris.actions.GetPowerAction;
import BlueArchive_Aris.cards.SwordBlast;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hoshino.DefaultMod.makeArisPowerPath;

//Gain 1 dex for the turn for each card played.

public class JobWizardPower extends JobPower implements CloneablePowerInterface {
    private boolean upgrade;
    public int block_power;

    public static final String POWER_ID = DefaultMod.makeArisID(JobWizardPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makeArisPowerPath("JobWizardPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makeArisPowerPath("JobWizardPower32.png"));

    public JobWizardPower(final AbstractCreature owner, boolean upgrade, final int amount
          , AbstractCard equip) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.block_power = amount;
        this.upgrade = upgrade;
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
        description = DESCRIPTIONS[0] + block_power + DESCRIPTIONS[1];
    }
    public String getAnimation() {
        return "baseAnimation_Wizard";
    }
    public void onJobChange(boolean withEquip) {
        super.onJobChange(withEquip);
        AbstractDungeon.actionManager.addToBottom(new GetPowerAction(upgrade));
    }
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.POWER) {
            int amount_ =  this.block_power;

            if(AbstractDungeon.player.hasPower(LevelUpPower.POWER_ID)) {
                amount_+=AbstractDungeon.player.getPower(LevelUpPower.POWER_ID).amount * block_power;
            }
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, amount_));
            this.flash();
        }
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
        return new JobWizardPower(owner, upgrade, block_power, equip);
    }
}
