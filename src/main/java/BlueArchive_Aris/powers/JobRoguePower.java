package BlueArchive_Aris.powers;

import BlueArchive_Aris.cards.SwordBlast;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeArisPowerPath;

//Gain 1 dex for the turn for each card played.

public class JobRoguePower extends JobPower implements CloneablePowerInterface {
    public int magic;
    public int shock_count = 1;

    public static final String POWER_ID = DefaultMod.makeArisID(JobRoguePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makeArisPowerPath("JobWarriorPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makeArisPowerPath("JobWarriorPower32.png"));

    public JobRoguePower(final AbstractCreature owner, final int magic
          , AbstractCard equip) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.magic = magic;
        this.equip = equip;
        this.shock_count = 1;

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
        description = DESCRIPTIONS[0] + shock_count + DESCRIPTIONS[1];
    }
    public String getAnimation() {
        return "baseAnimation_Warrior";
    }
    public void onJobChange(boolean withEquip) {
        super.onJobChange(withEquip);
        Iterator<AbstractMonster> var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster mo;
        while(var3.hasNext()) {
            mo = (AbstractMonster)var3.next();
            this.addToBot(new ApplyPowerAction(mo, owner, new WeakPower(mo, magic, false), magic));
            this.addToBot(new ApplyPowerAction(mo, owner, new ShockPower(mo, owner, magic), magic));
        }
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.ATTACK && !(card.costForTurn != 0 && (!card.freeToPlayOnce || card.cost == -1))) {

            AbstractCreature target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
            if (target != null) {
                this.addToBot(new ApplyPowerAction(target, owner, new ShockPower(target, owner, shock_count), shock_count));
            }
            this.flash();
        }
    }


    public void levelUp(){
        flash();
        shock_count++;
        if(amount == -1) {
            amount = 2;
        }
        else {
            amount++;
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new JobRoguePower(owner, magic, equip);
    }
}
