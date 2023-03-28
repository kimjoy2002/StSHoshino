package BlueArchive_Aris.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.makeArisPowerPath;

//Gain 1 dex for the turn for each card played.

public class JobWarriorPower extends JobPower implements CloneablePowerInterface {
    public int block;
    public int bonusBlock;
    public int baseBonusBlock;

    public static final String POWER_ID = DefaultMod.makeArisID(JobWarriorPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makeArisPowerPath("JobWarriorPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makeArisPowerPath("JobWarriorPower32.png"));

    public JobWarriorPower(final AbstractCreature owner, final int block, final int addBlock
          , AbstractCard equip) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.block = block;
        this.bonusBlock = addBlock;
        this.baseBonusBlock = addBlock;
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
        description = DESCRIPTIONS[0] + bonusBlock + DESCRIPTIONS[1];
    }
    public String getAnimation() {
        return "baseAnimation_Warrior";
    }
    public void onJobChange(boolean withEquip) {
        super.onJobChange(withEquip);
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, block));
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount < this.owner.currentHealth && damageAmount > 0 && info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS) {
            this.flash();
            int amount_ =  this.bonusBlock;

            if(AbstractDungeon.player.hasPower(LevelUpPower.POWER_ID)) {
                amount_+=this.baseBonusBlock*AbstractDungeon.player.getPower(LevelUpPower.POWER_ID).amount;
            }
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EndTurnBlockPower(AbstractDungeon.player,  amount_),  amount_));
        }
        return damageAmount;
    }
    public void levelUp(){
        flash();
        bonusBlock += baseBonusBlock;
        if(amount == -1) {
            amount = 2;
        }
        else {
            amount++;
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new JobWarriorPower(owner, block, bonusBlock, equip);
    }
}
