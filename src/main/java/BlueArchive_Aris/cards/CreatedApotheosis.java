package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.CreatedApotheosisAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class CreatedApotheosis extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeArisID(CreatedApotheosis.class.getSimpleName());
    public static final String IMG = makeArisCardPath("CreatedApotheosis.png");


    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;
    private static final int MAGIC = 5;
    private static final int UPGRADE_PLUS_MAGIC = 3;

    public CreatedApotheosis() {
        this(0);
    }

    public CreatedApotheosis(int upgrades) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        this.timesUpgraded = upgrades;
        exhaust = true;
    }
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CreatedApotheosisAction(this, magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_MAGIC + this.timesUpgraded);
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = cardStrings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
        initializeDescription();
    }

    public boolean canUpgrade() {
        return true;
    }

    public AbstractCard makeCopy() {
        return new CreatedApotheosis(this.timesUpgraded);
    }
}
