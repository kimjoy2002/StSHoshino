package BlueArchive_Hoshino.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.effects.KuroCarEffect;
import BlueArchive_Hoshino.effects.SiroBallEffect;
import BlueArchive_Hoshino.effects.SiroBallReturnEffect;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class ReflectablePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public boolean is_car;
    public static final Logger logger = LogManager.getLogger(ReflectablePower.class.getName());
    public static final String POWER_ID = DefaultMod.makeID("ReflectablePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ReflectablePower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ReflectablePower32.png"));


    int damage_given = 0;
    public ReflectablePower(final AbstractCreature owner, final AbstractCreature source, boolean is_car) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.source = source;
        this.is_car = is_car;

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
        description = DESCRIPTIONS[0];
    }
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(damageAmount == 0 && target != null && owner != target) {

            if(is_car) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(target, new KuroCarEffect(target.hb.cX, 100 * Settings.scale, true), 0.5F));

                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(owner, new DamageInfo(owner, damage_given, DamageInfo.DamageType.THORNS),
                                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            } else {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(target, new SiroBallReturnEffect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.4F));

                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(owner, new DamageInfo(owner, damage_given, DamageInfo.DamageType.THORNS),
                                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
    }
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        damage_given = (int)damage;
        return damage;
    }
    @Override
    public AbstractPower makeCopy() {
        return new ReflectablePower(owner, source, is_car);
    }
}
