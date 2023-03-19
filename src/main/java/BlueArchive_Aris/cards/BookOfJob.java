package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.powers.ChargePower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class BookOfJob extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeArisID(BookOfJob.class.getSimpleName());
    public static final String IMG = makeArisCardPath("BookOfJob.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;



    public BookOfJob() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }
    public static AbstractCard returnTrulyRandomJobCardInCombat() {
        ArrayList<AbstractCard> list = new ArrayList();
        Iterator var2 = AbstractDungeon.srcCommonCardPool.group.iterator();

        AbstractCard c;
        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c.hasTag(EnumPatch.EQUIPMENT) && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        var2 = AbstractDungeon.srcUncommonCardPool.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c.hasTag(EnumPatch.EQUIPMENT) && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        var2 = AbstractDungeon.srcRareCardPool.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c.hasTag(EnumPatch.EQUIPMENT) && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        return (AbstractCard)list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
    }
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard c = returnTrulyRandomJobCardInCombat().makeCopy();
        c.setCostForTurn(0);
        this.addToBot(new MakeTempCardInHandAction(c, true));

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(0);
            initializeDescription();
        }
    }
}
