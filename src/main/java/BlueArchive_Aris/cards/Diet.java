package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class Diet extends QuestCard {

    public static final String ID = DefaultMod.makeArisID(Diet.class.getSimpleName());
    public static final String IMG = makeArisCardPath("Diet.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 0;
    private static final int AMOUNT = 1;
    private static final int UPGRADE_PLUS_AMOUNT = 1;

    private static int count = 4;

    public Diet() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = AMOUNT;
        this.cardsToPreview = new NeatCompression();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ExhaustAction(this.magicNumber, false, true, true));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            if(this.cardsToPreview != null) {
                this.cardsToPreview.upgrade();
            }
            makeDescription();
            initializeDescription();
        }
    }

    @Override
    public boolean onQuestCheck(QuestProcess process) {
        if(process == QuestProcess.GAIN_CARD || process == QuestProcess.SKIP_CARD) {
            if(process == QuestProcess.GAIN_CARD)
                misc = count;
            else
                misc--;
            makeDescription();
            if(misc == 0)
                return true;
        }
        return false;
    }
    public void questInit() {
        misc = count;
        makeDescription();
    }
    public void onQuestComplete() {
        super.onQuestComplete();
        NeatCompression neatCompression = new NeatCompression();
        if (upgraded) {
            neatCompression.upgrade();
        }
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(neatCompression, (float)(Settings.WIDTH/2), (float)(Settings.HEIGHT / 2)));
    }

    public void makeDescription() {
        this.rawDescription = upgraded?cardStrings.UPGRADE_DESCRIPTION:cardStrings.DESCRIPTION;
        if(misc > 0) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }
}
