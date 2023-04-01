package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.EndTurnBlockPower;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class ReactiveArmour extends AbstractDynamicCard implements AttackedCard {

    public static final String ID = DefaultMod.makeArisID(ReactiveArmour.class.getSimpleName());
    public static final String IMG = makeArisCardPath("ReactiveArmour.png");


    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;
    private static final int MAGIC = 8;
    private static final int UPGRADE_PLUS_MAGIC = 3;


    public ReactiveArmour() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EndTurnBlockPower(AbstractDungeon.player,  this.magicNumber),  this.magicNumber));
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount < AbstractDungeon.player.currentHealth && damageAmount > 0 && info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS) {
            this.addToBot(new DiscardToHandAction(this));
        }
        return damageAmount;
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
