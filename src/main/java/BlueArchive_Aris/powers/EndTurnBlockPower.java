package BlueArchive_Aris.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;

public class EndTurnBlockPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeArisID("EndTurnBlockPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("EnergyLosePower84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("EnergyLosePower32.png"));

    public EndTurnBlockPower(AbstractCreature owner, int armorAmt) {
        name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = armorAmt;
        type = PowerType.BUFF;
        isTurnBased = false;
        this.updateDescription();
        this.loadRegion("defenseNext");
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void atEndOfTurn(boolean isPlayer) {
        if(isPlayer) {
            this.flash();
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.owner.hb.cX, this.owner.hb.cY, AbstractGameAction.AttackEffect.SHIELD));
            this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }
}
