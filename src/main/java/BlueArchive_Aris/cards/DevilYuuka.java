package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.JobChangeAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.JobDevilPower;
import BlueArchive_Aris.powers.JobWizardPower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.patches.EnumPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class DevilYuuka extends EquipmentCard {

    public static final String ID = DefaultMod.makeArisID(DevilYuuka.class.getSimpleName());
    public static final String IMG = makeArisCardPath("DevilYuuka.png");


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 2;
    private static final int MAGIC = 5;
    private static final int UPGRADE_PLUS_MAGIC = 1;
    private static final int SECOND_MAGIC = 1;


    public DevilYuuka() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        secondMagicNumber = baseSecondMagicNumber = SECOND_MAGIC;

        this.tags.add(EnumPatch.EQUIPMENT);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new JobChangeAction(this,
                new JobDevilPower(p, magicNumber, secondMagicNumber, this)));
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
