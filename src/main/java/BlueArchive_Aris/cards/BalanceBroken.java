package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.ChargePower;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.CharacterManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;
import static BlueArchive_Hoshino.patches.power.ChargePatch.useCardPatcher.chargeThisUse;
import static java.lang.Math.min;

public class BalanceBroken extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeArisID(BalanceBroken.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeArisCardPath("BalanceBroken.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 3;
    private static final int DAMAGE = 21;
    private static final int UPGRADE_PLUS_DMG = 5;

    private static final int AMOUNT = 8;
    private static final int UPGRADE_PLUS_AMOUNT = 2;

    public BalanceBroken() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = AMOUNT;
        this.isMultiDamage = true;
        exhaust = true;
    }

    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = DAMAGE + (upgraded?UPGRADE_PLUS_DMG:0);
        int remain_cost = this.costForTurn - EnergyPanel.totalCount;
        if (remain_cost > 0) {
            if(AbstractDungeon.player.hasPower(ChargePower.POWER_ID)) {
                AbstractPower sdPower = AbstractDungeon.player.getPower(ChargePower.POWER_ID);
                this.baseDamage += magicNumber*min(remain_cost, sdPower.amount);
            }
        }
        super.calculateCardDamage(mo);
        this.isDamageModified = this.damage != (DAMAGE + (upgraded?UPGRADE_PLUS_DMG:0));
        this.baseDamage = DAMAGE + (upgraded?UPGRADE_PLUS_DMG:0);
        this.initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int use_charge = 0;
        int remain_cost = this.costForTurn - EnergyPanel.totalCount;
        if (remain_cost > 0) {
            if(AbstractDungeon.player.hasPower(ChargePower.POWER_ID)) {
                AbstractPower sdPower = AbstractDungeon.player.getPower(ChargePower.POWER_ID);
                use_charge = min(remain_cost, sdPower.amount);
            }
        }


        this.baseDamage = DAMAGE + (upgraded?UPGRADE_PLUS_DMG:0);
        this.baseDamage += magicNumber*use_charge;
        calculateCardDamage(m);
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            initializeDescription();
        }
    }
}
