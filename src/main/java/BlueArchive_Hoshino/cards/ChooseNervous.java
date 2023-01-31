package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.DrowsyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class ChooseNervous extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ChooseNervous.class.getSimpleName());
    public static final String IMG = makeCardPath("Nervous.png");
    private static final CardStrings cardStrings;
    AbstractCard card;

    public ChooseNervous() {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
    }

    public ChooseNervous(AbstractCard card) {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.card = card;
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void onChoseThisOption() {
        if(card != null && DrowsyCard.isDrowsyCard(card)) {
            this.addToBot(new DrowsyAction(card.uuid, 1));
        } else if(card == null) {
            Iterator var1 = AbstractDungeon.player.hand.group.iterator();

            while(var1.hasNext()) {
                AbstractCard c = (AbstractCard)var1.next();
                if (DrowsyCard.isDrowsyCard(c)) {
                    this.addToBot(new DrowsyAction(c.uuid, 1));
                }
            }
        }
    }

    public void upgrade() {
    }

    public AbstractCard makeCopy() {
        return new ChooseNervous(card);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("BlueArchive_Hoshino:Nervous");
    }

}
