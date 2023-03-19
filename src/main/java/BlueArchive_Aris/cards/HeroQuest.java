package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class HeroQuest extends QuestCard {

    public static final String ID = DefaultMod.makeArisID(HeroQuest.class.getSimpleName());
    public static final String IMG = makeArisCardPath("HeroQuest.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = -2;

    private static int count = 15;

    public HeroQuest() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.cardsToPreview = new SwordOfHero();
        isInnate = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if(this.cardsToPreview != null) {
                this.cardsToPreview.upgrade();
            }
            makeDescription();
            initializeDescription();
        }
    }

    @Override
    public boolean onQuestCheck(QuestProcess process) {
        if(process == QuestProcess.PROGRESS_FLOOR) {
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

        SwordOfHero swordOfHero = new SwordOfHero();
        Iterator var1 = AbstractDungeon.player.relics.iterator();

        AbstractRelic r;
        while(var1.hasNext()) {
            r = (AbstractRelic)var1.next();
            r.onObtainCard(swordOfHero);
        }

        swordOfHero.shrink();
        AbstractDungeon.getCurrRoom().souls.obtain(swordOfHero, true);
        var1 = AbstractDungeon.player.relics.iterator();

        while(var1.hasNext()) {
            r = (AbstractRelic)var1.next();
            r.onMasterDeckChange();
        }
        /*
        if (upgraded) {
            swordOfHero.upgrade();
        }
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(swordOfHero, (float)(Settings.WIDTH/2), (float)(Settings.HEIGHT / 2)));*/
    }
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[2];
        return false;
    }

    public void makeDescription() {
        this.rawDescription = upgraded?cardStrings.UPGRADE_DESCRIPTION:cardStrings.DESCRIPTION;
        if(misc > 0) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }
}
