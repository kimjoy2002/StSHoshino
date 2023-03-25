package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
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

public class ElixirSyndrome extends QuestCard {

    public static final String ID = DefaultMod.makeArisID(ElixirSyndrome.class.getSimpleName());
    public static final String IMG = makeArisCardPath("ElixirSyndrome.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    private static int count = 3;

    public ElixirSyndrome() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        this.cardsToPreview = new Elixir();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            if(this.cardsToPreview != null) {
                this.cardsToPreview.upgrade();
            }
            makeDescription();
            initializeDescription();
        }
    }

    @Override
    public boolean onQuestCheck(QuestProcess process) {
        if(process == QuestProcess.POTION_DROP) {
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
        Elixir elixir = new Elixir();
        if (upgraded) {
            elixir.upgrade();
        }
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(elixir, (float)(Settings.WIDTH/2), (float)(Settings.HEIGHT / 2)));
    }

    public void makeDescription() {
        this.rawDescription = upgraded?cardStrings.UPGRADE_DESCRIPTION:cardStrings.DESCRIPTION;
        if(misc > 0) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }
}
