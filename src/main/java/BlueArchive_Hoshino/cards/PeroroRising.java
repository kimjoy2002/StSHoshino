package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.PeroroUpgradeAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class PeroroRising extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(PeroroRising.class.getSimpleName());
    public static final String IMG = makeCardPath("Peroro_rising.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private int AMOUNT = 5;
    private static final int UPGRADE_PLUS_AMOUNT = 3;
    public PeroroRising() {
        super(ID, IMG, -2, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        magicNumber = baseMagicNumber = AMOUNT;
        isEthereal = true;
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
    }
    public void triggerWhenDrawn() {
        this.addToBot(new PeroroUpgradeAction());
    }

    public void triggerOnExhaust() {
        if (this.upgraded && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractCard c = makeCopy();
            c.upgrade();
            this.addToBot(new MakeTempCardInDiscardAction(c, false));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            isEthereal = false;
            initializeDescription();
        }
    }


    public AbstractCard makeCopy() {
        return new PeroroRising();
    }

}
