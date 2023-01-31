package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.BulletModifyAction;
import BlueArchive_Hoshino.actions.DrowsyAction;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;
import static java.lang.Math.min;
// "How come this card extends CustomCard and not DynamicCard like all the rest?"
// Skip this question until you start figuring out the AbstractDefaultCard/AbstractDynamicCard and just extend DynamicCard
// for your own ones like all the other cards.

// Well every card, at the end of the day, extends CustomCard.
// Abstract Default Card extends CustomCard and builds up on it, adding a second magic number. Your card can extend it and
// bam - you can have a second magic number in that card (Learn Java inheritance if you want to know how that works).
// Abstract Dynamic Card builds up on Abstract Default Card even more and makes it so that you don't need to add
// the NAME and the DESCRIPTION into your card - it'll get it automatically. Of course, this functionality could have easily
// Been added to the default card rather than creating a new Dynamic one, but was done so to deliberately to showcase custom cards/inheritance a bit more.
public class TacticalSuppressionAttack extends CustomCard implements DrowsyCard, ShotCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Strike Deal 4 (5) * Bullet damage.
     */

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(TacticalSuppressionAttack.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("tacticalsuppression.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name
    // and make sure it's in the correct folder. That's all.
    // There's makeCardPath, makeRelicPath, power, orb, event, etc..
    // The list of all of them can be found in the main DefaultMod.java file in the
    // ==INPUT TEXTURE LOCATION== section under ==MAKE IMAGE PATHS==


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;

    private static final int COST = 3;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int DROWSY = 4;

    public TacticalSuppressionAttack() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        baseDamage = DAMAGE;
        isMultiDamage = true;
        exhaust = true;
        misc = DROWSY;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Create an int which equals to your current energy.
        int effect = min(5, BulletSubscriber.getBullet());

        // For each bullet, create 1 damage action.
        for (int i = 0; i < effect; i++) {
            this.addToBot(new VFXAction(p, new CleaveEffect(), (i == 4 ? 0.2F : (i == 0 ? 0.1F : 0))));
            this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));


            if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:BulletVune")) {
                AbstractPower igPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:BulletVune");
                igPower.flash();
                Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

                while(var3.hasNext()) {
                    AbstractMonster mo = (AbstractMonster)var3.next();
                    this.addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, igPower.amount, false), igPower.amount));
                }
            }

        }
        if(misc == 0) {
            Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while(var3.hasNext()) {
                AbstractMonster mo = (AbstractMonster)var3.next();
                this.addToBot(new StunMonsterAction(mo, p, 1));
            }
        }
        this.addToBot(new BulletModifyAction(-effect));
    }

    @Override
    public void makeDescrption() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            makeDescrption();
        }
    }

    public boolean cardPlayable(AbstractMonster m) {
        boolean playable = super.cardPlayable(m);
        return playable && BulletSubscriber.getBullet() > 0;
    }

    public void onAddToHand() {
        this.addToBot(new DrowsyAction(this.uuid, -1));
        makeDescrption();
    }
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (this instanceof DrowsyCard && misc == 0) {
            this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
