package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.ItemCopyAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.SystemOverloadPower;
import BlueArchive_Aris.powers.WeaponMasterPower2;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class SystemOverload extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeArisID(SystemOverload.class.getSimpleName());
    public static final String IMG = makeArisCardPath("SystemOverload.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 0;

    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 2;


    public SystemOverload() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SystemOverloadPower(AbstractDungeon.player, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
