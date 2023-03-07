package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class SandStorm extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(SandStorm.class.getSimpleName());
    public static final String IMG = makeCardPath("SandStorm.png");
    private int AMOUNT = 1;
    public SandStorm() {
        super(ID, IMG, 1, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        magicNumber = baseMagicNumber = AMOUNT;
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new SandStorm();
    }

}
