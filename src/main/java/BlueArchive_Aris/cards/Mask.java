package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.JobChangeAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.JobWarriorPower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.patches.EnumPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class Mask extends EquipmentCard {

    public static final String ID = DefaultMod.makeArisID(Mask.class.getSimpleName());
    public static final String IMG = makeArisCardPath("Mask.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;
    private static final int BLOCK = 9;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    private static final int MAGIC = 4;


    public Mask() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = MAGIC;

        this.tags.add(EnumPatch.EQUIPMENT);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new JobChangeAction(this,
                new JobWarriorPower(p, block, magicNumber, this)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
