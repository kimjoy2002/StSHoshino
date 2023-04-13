package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.BlacksmithAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class EnhancementScroll extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeArisID(EnhancementScroll.class.getSimpleName());
    public static final String IMG = makeArisCardPath("EnhancementScroll.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;
    private static final int AMOUNT = 2;
    private static final int UPGRADE_AMOUNT = 1;

    public EnhancementScroll() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = AMOUNT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new BlacksmithAction(magicNumber));

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_AMOUNT);
            initializeDescription();
        }
    }
}
