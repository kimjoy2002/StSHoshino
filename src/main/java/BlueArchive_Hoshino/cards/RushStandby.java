package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.ReloadAction;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class RushStandby extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */


    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(RushStandby.class.getSimpleName());
    public static final String IMG = makeCardPath("RushStandby.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 1;
    private static final int BLOCK = 2;

    private int AMOUNT = 2;
    private static final int UPGRADE_PLUS_AMOUNT = 1;

    // /STAT DECLARATION/


    public RushStandby() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = AMOUNT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        for(int i = 0; i < magicNumber; i++) {
            this.addToBot(new ReloadAction(true, false));
        }
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
