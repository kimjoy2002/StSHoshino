package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.ThroughViolence;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class DailyQuest extends QuestCard {

    public static final String ID = DefaultMod.makeArisID(DailyQuest.class.getSimpleName());
    public static final String IMG = makeArisCardPath("DailyQuest.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 1;
    private static final int DAMAGE = 9;
    private static final int UPGRADE_PLUS_DAMAGE = 9;
    private static final int AMOUNT = 10;

    private static int count = 3;

    public DailyQuest(int i) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = AMOUNT;
    }
    public DailyQuest() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = AMOUNT;
        this.cardsToPreview = new DailyQuest(1);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            makeDescription();
            initializeDescription();
        }
    }

    @Override
    public boolean onQuestCheck(QuestProcess process) {
        if(process == QuestProcess.FINISH_BATTLE) {
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
        DailyQuest dailyQuest = new DailyQuest();
        AbstractDungeon.player.heal(this.magicNumber);
        if(upgraded) {
            CardCrawlGame.sound.play("GOLD_GAIN");
            AbstractDungeon.player.gainGold(50);
        }
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(dailyQuest, (float)(Settings.WIDTH/2), (float)(Settings.HEIGHT / 2)));
    }

    public void makeDescription() {
        this.rawDescription = upgraded?cardStrings.UPGRADE_DESCRIPTION:cardStrings.DESCRIPTION;
        if(misc > 0) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }
}
