package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.ElecWhirlwindAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.JobPower;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.NewThunderStrikeAction;
import com.megacrit.cardcrawl.actions.unique.WhirlwindAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Aris.cards.OverloadCard.overloadThisCombat;
import static BlueArchive_Aris.powers.ChargePower.chargeThisCombat;
import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class ElectronicExplosion extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeArisID(ElectronicExplosion.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeArisCardPath("ElectronicExplosion.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;


    public ElectronicExplosion() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = 0;
    }

    public void updateVal () {
        magicNumber = baseMagicNumber = overloadThisCombat;
    }
    public void applyPowers() {
        updateVal();
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + cardStrings.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + cardStrings.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        updateVal();
        this.calculateCardDamage(m);
        for(int i = 0; i < this.magicNumber; ++i) {
            this.addToBot(new NewThunderStrikeAction(this));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
