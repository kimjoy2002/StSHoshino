package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.DrowsyAction;
import BlueArchive_Hoshino.characters.Hoshino;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;
import static java.lang.Math.max;

public class Preparation extends AbstractDynamicCard implements DrowsyCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */


    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Preparation.class.getSimpleName());
    public static final String IMG = makeCardPath("Preparation.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;

    private static final int COST = 0;
    private static final int DROWSY = 4;
    private static final int LOW_DROWSY_DIFF = 1;

    // /STAT DECLARATION/


    public Preparation() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        misc = DROWSY;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DiscardPileToTopOfDeckAction(p));
        if (misc == 0) {
            this.addToBot(new GainEnergyAction(2));
        } else if (misc <= LOW_DROWSY_DIFF) {
            this.addToBot(new GainEnergyAction(1));
        }
    }

    public void makeDescrption() {
        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[1] + max(0, misc-LOW_DROWSY_DIFF) + cardStrings.EXTENDED_DESCRIPTION[2] + misc + cardStrings.EXTENDED_DESCRIPTION[3];
        initializeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            misc--;
            initializeDescription();
        }
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
