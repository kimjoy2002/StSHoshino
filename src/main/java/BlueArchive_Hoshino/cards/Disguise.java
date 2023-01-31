package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.DisguiseAction;
import BlueArchive_Hoshino.actions.DrowsyAction;
import BlueArchive_Hoshino.characters.Hoshino;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class Disguise extends CustomCard implements DrowsyCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Disguise.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Disguise.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;

    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int DROWSY = 4;
    private static final int MAGIC = 5;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public Disguise() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        baseBlock = BLOCK;
        misc = DROWSY;
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));


        Iterator var5 = p.drawPile.group.iterator();
        boolean hasDrwosy = false;

        while(var5.hasNext()) {
            AbstractCard c = (AbstractCard)var5.next();
            if (DrowsyCard.isDrowsyCard(c)) {
                hasDrwosy = true;
                break;
            }
        }

        if(hasDrwosy) {
            this.shuffleBackIntoDrawPile = true;
            this.addToBot(new DisguiseAction());
        } else {
            this.shuffleBackIntoDrawPile = false;
        }

    }
    public void makeDescrption() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            makeDescrption();
        }
    }

    public void onAddToHand() {
        this.addToBot(new DrowsyAction(this.uuid, -1));
        makeDescrption();
    }

    public void whenDrowsyChange(int prev_drowsy) {
        if(prev_drowsy > 0 && misc == 0) {
            upgradeBlock(magicNumber);
            makeDescrption();
        }
        else if(prev_drowsy == 0 && misc > 0) {
            upgradeBlock(-magicNumber);
            makeDescrption();
        }
    }
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (this instanceof DrowsyCard && misc == 0) {
            this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
