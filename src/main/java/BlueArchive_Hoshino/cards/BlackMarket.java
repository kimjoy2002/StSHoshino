package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.powers.BlackMarketPower;
import BlueArchive_Hoshino.powers.SupressionVeteranPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class BlackMarket extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(BlackMarket.class.getSimpleName());
    public static final String IMG = makeCardPath("BlackMarket.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;

    private static final int COST = 2;

    // /STAT DECLARATION/

    public BlackMarket() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }


    public static AbstractCard returnRandomBlackMarketCardInCombat() {
        ArrayList<AbstractCard> list = new ArrayList();
        Iterator var2 = AbstractDungeon.srcCommonCardPool.group.iterator();

        AbstractCard c;
        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof ShotCard && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        var2 = AbstractDungeon.srcUncommonCardPool.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof ShotCard && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        var2 = AbstractDungeon.srcRareCardPool.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof ShotCard &&!c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        return (AbstractCard)list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
    }



    
    // Actions the card should do.
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new BlackMarketPower(p, p, 1), 1));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(1);
            initializeDescription();
        }
    }
}
