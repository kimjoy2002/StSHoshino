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
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class RaidAddiction extends QuestCard {

    public static final String ID = DefaultMod.makeArisID(RaidAddiction.class.getSimpleName());
    public static final String IMG = makeArisCardPath("RaidAddiction.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final int COST = 2;
    private static final int DAMAGE = 16;
    private static final int UPGRADE_PLUS_BLOCK = 5;

    private static int count = 3;

    public RaidAddiction() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.cardsToPreview = new RaidersLeader();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SMASH));
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
        if(process == QuestProcess.SLAIN_ELITE) {
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
        RaidersLeader raidersLeader = new RaidersLeader();
        if (upgraded) {
            raidersLeader.upgrade();
        }
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(raidersLeader, (float)(Settings.WIDTH/2), (float)(Settings.HEIGHT / 2)));
    }

    public void makeDescription() {
        this.rawDescription = upgraded?cardStrings.UPGRADE_DESCRIPTION:cardStrings.DESCRIPTION;
        if(misc > 0) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }
}
