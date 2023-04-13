package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.RestoreStrPower;
import BlueArchive_Aris.powers.ShockPower;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class WeakeningCurse extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeArisID(WeakeningCurse.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeArisCardPath("WeakeningCurse.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;

    private static final int AMOUNT = 9;
    private static final int UPGRADE_PLUS_AMOUNT = 3;

    private static final int AMOUNT2 = 3;
    private static final int UPGRADE_PLUS_AMOUNT2 = 1;

    public WeakeningCurse() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = AMOUNT;
        secondMagicNumber = baseSecondMagicNumber = AMOUNT2;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -this.magicNumber), -this.magicNumber));
        if (m != null && !m.hasPower("Artifact")) {
            this.addToBot(new ApplyPowerAction(m, p, new RestoreStrPower(m, this.magicNumber/secondMagicNumber, secondMagicNumber)));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_AMOUNT2);
            initializeDescription();
        }
    }
}
