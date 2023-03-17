package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.AbstractDynamicCard;
import BlueArchive_Hoshino.cards.ChooseRepose;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class PrismaticBeams extends OverloadCard {
    public static final String ID = DefaultMod.makeArisID(PrismaticBeams.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeArisCardPath("TempAttack.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int AMOUNT = 4;
    private static final int UPGRADE_PLUS_AMOUNT = 2;


    public PrismaticBeams() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = AMOUNT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, m.hb.cX, m.hb.cY * Settings.scale), 0.3F));
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        super.use(p,m);
    }

    public void onOverload(AbstractPlayer p, AbstractMonster m) {
        PrismaticBeams s = new PrismaticBeams();
        if(upgraded){
            s.upgrade();
        }
        s.baseDamage = baseDamage + magicNumber;
        s.exhaust = true;
        s.isEthereal = true;
        s.rawDescription = cardStrings.EXTENDED_DESCRIPTION[upgraded?1:0];
        s.initializeDescription();
        this.addToBot(new MakeTempCardInHandAction(s, 1));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            if(exhaust && isEthereal) {
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
            } else {
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            }
            initializeDescription();
        }
    }
    public AbstractCard makeCopy() {
        PrismaticBeams p = new PrismaticBeams();
        p.exhaust = this.exhaust;
        p.isEthereal = this.isEthereal;
        if(exhaust && isEthereal) {
            p.rawDescription = cardStrings.EXTENDED_DESCRIPTION[upgraded?1:0];
        }
        initializeDescription();
        return p;
    }
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard c = super.makeStatEquivalentCopy();
        return c;
    }
}
