package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.MomoiAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.BalanceBrokenPower;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class MomoiAppear extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeArisID(MomoiAppear.class.getSimpleName());
    public static final String IMG = makeArisCardPath("MomoiAppear.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 0;

    public MomoiAppear() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        misc = 0;
        cardsToPreview = new GameScenario();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MomoiAction(this, 120 + misc*80));
    }

    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
        if(misc > 0) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
        }
        this.initializeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            if(misc > 0) {
                this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
            }
            initializeDescription();
        }
    }
}
