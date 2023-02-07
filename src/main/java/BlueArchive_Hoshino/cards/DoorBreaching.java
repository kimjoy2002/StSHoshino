package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.DrowsyAction;
import BlueArchive_Hoshino.characters.Hoshino;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class DoorBreaching extends CustomCard implements DrowsyCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Strike Deal 6(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(DoorBreaching.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("DoorBreaching.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hoshino.Enums.COLOR_PINK;

    private static final int COST = 2;
    private static final int DAMAGE = 16;
    private static final int UPGRADE_PLUS_DMG = 5;
    private static final int DROWSY = 2;
    private static final int AMOUNT = 3;

    public DoorBreaching() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        misc = DROWSY;
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = AMOUNT;
    }
    public void makeDescrption() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + misc + cardStrings.EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if(misc == 0) {
            this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
            this.addToBot(new DrowsyAction(this.uuid, 2));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            makeDescrption();
        }
    }

    public void onAddToHand() {
        this.addToBot(new DrowsyAction(this.uuid, -1));
        makeDescrption();
    }
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (this instanceof DrowsyCard && misc == 0) {
            this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
