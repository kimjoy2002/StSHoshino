package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.ElixirAction;
import BlueArchive_Aris.actions.PermanentExhaustAction;
import BlueArchive_Aris.actions.PurifyCardAction;
import BlueArchive_Aris.actions.RandomForgeAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class NeatCompression extends AbstractDynamicCard implements RewardCard {
    public static final String ID = DefaultMod.makeArisID(NeatCompression.class.getSimpleName());
    public static final String IMG = makeArisCardPath("NeatCompression.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 0;


    public NeatCompression() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.tags.add(CardTags.HEALING);
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new PurifyCardAction(this));
        if(upgraded) {
            this.addToBot(new RandomForgeAction());
        }
        this.addToBot(new PermanentExhaustAction(this.uuid));
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
