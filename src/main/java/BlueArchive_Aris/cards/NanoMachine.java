package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.LevelUpAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.ChargePower;
import BlueArchive_Aris.powers.NanoMachinePower;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class NanoMachine extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeArisID(NanoMachine.class.getSimpleName());
    public static final String IMG = makeArisCardPath("NanoMachine.png");


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 1;

    public NanoMachine() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NanoMachinePower(AbstractDungeon.player, !upgraded?2:4), !upgraded?2:4));


    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
