package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.DrowsyAction;
import BlueArchive_Hoshino.characters.Hoshino;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class Patience extends CustomCard implements DrowsyCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     */

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Patience.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Patience.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;

    private static final int COST = -2;
    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int DROWSY = 5;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public Patience() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        baseBlock  = block = BLOCK;
        misc = DROWSY;
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
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
            initializeDescription();
        }
    }

    public void onAddToHand() {
        applyPowersToBlock();
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
        this.addToBot(new DrowsyAction(this.uuid, -1));
        makeDescrption();
    }
    public void whenDrowsyChange(int prev_drowsy) {
        if(prev_drowsy != 0 && misc == 0) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
            AbstractDungeon.player.hand.moveToExhaustPile(this);
            CardCrawlGame.dungeon.checkForPactAchievement();
        }
    }
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[2];
        return false;
    }
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (this instanceof DrowsyCard && misc == 0) {
            this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
