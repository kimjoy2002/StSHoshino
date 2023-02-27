package BlueArchive_Hoshino.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class ShieldPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("ShieldPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ShieldPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ShieldPower32.png"));

    public ShieldPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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

        if(owner.isPlayer && AbstractDungeon.player instanceof Hoshino) {
            if(!AbstractDungeon.player.hasPower("BlueArchive_Hoshino:ShieldPower")) {
                AnimationState.TrackEntry e = AbstractDungeon.player.state.setAnimation(0, Hoshino.getShieldUpTextureName(), false);
                AbstractDungeon.player.state.addAnimation(0, Hoshino.getShieldBaseTextureName(), true, e.getEndTime());
            }
        }

        updateDescription();
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0];
        }
    }

    public void onInitialApplication(){
        if(amount > owner.currentBlock) {
            reducePower(amount - owner.currentBlock);
            if(amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "BlueArchive_Hoshino:ShieldPower"));
            }
        }
    }
    public void onGainCharge(int chargeAmount) {
        if(amount > owner.currentBlock) {
            reducePower(amount - owner.currentBlock);
            if(amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "BlueArchive_Hoshino:ShieldPower"));
            }
        }
    }

    public void onGainedBlock(float blockAmount) {
        if(amount > owner.currentBlock) {
            reducePower(amount - owner.currentBlock);
            if(amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "BlueArchive_Hoshino:ShieldPower"));
            }
        }
    }

    public void atEndOfTurn(boolean isPlayer) {
        if(amount > owner.currentBlock) {
            reducePower(amount - owner.currentBlock);
            if(amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "BlueArchive_Hoshino:ShieldPower"));
            }
        }
    }
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(amount > owner.currentBlock) {
            reducePower(amount - owner.currentBlock);
            if(amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "BlueArchive_Hoshino:ShieldPower"));
            }
        }
        return damageAmount;
    }
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if(amount > owner.currentBlock) {
            reducePower(amount - owner.currentBlock);
            if(amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "BlueArchive_Hoshino:ShieldPower"));
            }
        }
    }

    public void onVictory() {
        if(owner.isPlayer && AbstractDungeon.player instanceof Hoshino) {
            AbstractDungeon.player.state.addAnimation(0, Hoshino.getBaseTextureName(true), true, 0);
        }
    }

    public void onRemove() {
        if(owner.isPlayer && AbstractDungeon.player instanceof Hoshino) {
            AbstractDungeon.player.state.addAnimation(0, Hoshino.getBaseTextureName(true), true, 0);
        }
    }

    /*public void onVictory() {
    }*/

    @Override
    public AbstractPower makeCopy() {
        return new ShieldPower(owner, source, amount);
    }
}
