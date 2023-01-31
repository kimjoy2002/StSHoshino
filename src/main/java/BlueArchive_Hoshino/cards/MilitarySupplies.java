package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.AmmoBoxAction;
import BlueArchive_Hoshino.characters.Hoshino;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class MilitarySupplies extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */


    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(MilitarySupplies.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("MilitarySupplies.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;

    private static final int COST = 1;

    private static final int BLOCK = 8;
    // /STAT DECLARATION/

    public MilitarySupplies() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
    }


    public static AbstractCard returnRandomBulletCardInCombat() {
        ArrayList<AbstractCard> list = new ArrayList();
        Iterator var2 = AbstractDungeon.srcCommonCardPool.group.iterator();

        AbstractCard c;
        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof BulletCard && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        var2 = AbstractDungeon.srcUncommonCardPool.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof BulletCard && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        var2 = AbstractDungeon.srcRareCardPool.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c instanceof BulletCard &&!c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        return (AbstractCard)list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        this.addToBot(new AmmoBoxAction(upgraded));
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
