package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.ElixirAction;
import BlueArchive_Aris.actions.PermanentExhaustAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class ExecutionSword extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeArisID(ExecutionSword.class.getSimpleName());
    public static final String IMG = makeArisCardPath("ExecutionSword.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 3;

    public ExecutionSword() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, p.gold/100), p.gold/100));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(2);
            initializeDescription();
        }
    }
}
