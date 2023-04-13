package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.ElixirAction;
import BlueArchive_Aris.actions.PermanentExhaustAction;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class Elixir extends AbstractDynamicCard implements RewardCard {
    public static final String ID = DefaultMod.makeArisID(Elixir.class.getSimpleName());
    public static final String IMG = makeArisCardPath("Elixir.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 0;
    private static final int AMOUNT = 7;
    private static final int UPGRADE_PLUS_AMOUNT = 3;


    public Elixir() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = AMOUNT;
        this.tags.add(CardTags.HEALING);
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ElixirAction(-1, magicNumber));
        this.addToBot(new PermanentExhaustAction(this.uuid));
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
