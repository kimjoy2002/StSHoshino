package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.BulletModifyAction;
import BlueArchive_Hoshino.actions.MaxBulletModifyAction;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.powers.ExpertPower;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class Contract extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */


    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Contract.class.getSimpleName());
    public static final String IMG = makeCardPath("Contract.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;

    private static final int COST = 1;

    // /STAT DECLARATION/
    private int AMOUNT = 2;
    private static final int UPGRADE_PLUS_AMOUNT = 1;


    public Contract() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = AMOUNT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MaxBulletModifyAction(-1));
        if(BulletSubscriber.getBullet() == BulletSubscriber.getMaxBullet()) {
            this.addToBot(new BulletModifyAction(-1));
        }
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new ExpertPower(p,p, this.magicNumber), this.magicNumber));
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

    public boolean cardPlayable(AbstractMonster m) {
        boolean playable = super.cardPlayable(m);
        return playable && BulletSubscriber.getMaxBullet() > 0;
    }
}
