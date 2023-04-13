package BlueArchive_Aris.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeArisPowerPath;

//Gain 1 dex for the turn for each card played.

public class StaticArmourPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = DefaultMod.makeArisID("StaticArmourPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makeArisPowerPath("ShockPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makeArisPowerPath("ShockPower32.png"));

    public StaticArmourPower(final AbstractCreature owner, final int amount) {
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


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }


    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount < this.owner.currentHealth && damageAmount > 0 && info.owner != null) {
            this.flash();
            Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            AbstractMonster mo;
            while(var3.hasNext()) {
                mo = (AbstractMonster)var3.next();
                this.addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new ShockPower(mo, AbstractDungeon.player, amount), amount));
            }
        }
        return damageAmount;
    }
    @Override
    public AbstractPower makeCopy() {
        return new StaticArmourPower(owner, amount);
    }
}
