package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.PerfectPlanAction;
import BlueArchive_Hoshino.actions.ReloadAction;
import BlueArchive_Hoshino.actions.RetryAction;
import BlueArchive_Hoshino.characters.Hoshino;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class Retry extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */


    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Retry.class.getSimpleName());
    public static final String IMG = makeCardPath("Retry.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 1;
    private static final int BLOCK = 5;

    private int AMOUNT = 3;
    private static final int UPGRADE_PLUS_AMOUNT = 2;

    // /STAT DECLARATION/


    public Retry() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = AMOUNT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new RetryAction(this.magicNumber, block));
        this.addToBot(new ShuffleAction(AbstractDungeon.player.drawPile, true));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            initializeDescription();
        }
    }
}
