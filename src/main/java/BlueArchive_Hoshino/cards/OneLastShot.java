package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.BulletModifyAction;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.patches.EnumPatch;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class OneLastShot extends CustomCard implements ShotCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(OneLastShot.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("OneLastShot.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 3;

    public OneLastShot() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean isLastshot = BulletSubscriber.getBullet() == 1;
        this.addToBot(new BulletModifyAction(-1));
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage , damageTypeForTurn),
                        isLastshot?EnumPatch.HOSHINO_SHOTGUN_HEAVY:EnumPatch.HOSHINO_SHOTGUN));
        if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:BulletVune")) {
            AbstractPower igPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:BulletVune");
            igPower.flash();
            this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, igPower.amount, false), igPower.amount));
        }
    }

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (BulletSubscriber.getBullet() == 1) {
             this.damage += this.damage;
        }

        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:BulletToolTips");
        TEXT = uiStrings.TEXT;
    }
    public boolean cardPlayable(AbstractMonster m) {
        boolean playable = super.cardPlayable(m);
        if(BulletSubscriber.getBullet() <=  0) {
            this.cantUseMessage = TEXT[2];
        }
        return playable && BulletSubscriber.getBullet() > 0;
    }
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (BulletSubscriber.getBullet() == 1) {
            this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
