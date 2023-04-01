package BlueArchive_Aris.cards;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.ShuffleCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;

public class Ranker extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeArisID(Ranker.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeArisCardPath("Ranker.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aris.Enums.COLOR_BLUE;

    private static final int COST = 3;
    private static final int DAMAGE = 29;
    private static final int UPGRADE_PLUS_DAM = 9;

    public Ranker() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            this.addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
        }
        this.addToBot(new WaitAction(0.8F));

        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.NONE));
    }

    public void setCost() {
        boolean is_only_rare = true;
        boolean is_only_attack = true;
        for(AbstractCard iter : AbstractDungeon.player.hand.group) {
            if(iter != this) {
                if(iter.type == CardType.ATTACK) {
                    is_only_attack = false;
                }
                if(iter.rarity == CardRarity.RARE) {
                    is_only_rare = false;
                }
            }
        }
        for(AbstractCard iter : AbstractDungeon.player.hand.group) {
            if(iter != this) {
                if(iter.type == CardType.ATTACK) {
                    is_only_attack = false;
                }
                if(iter.rarity == CardRarity.RARE) {
                    is_only_rare = false;
                }
            }
        }
        for(AbstractCard iter : AbstractDungeon.player.discardPile.group) {
            if(iter != this) {
                if(iter.type == CardType.ATTACK) {
                    is_only_attack = false;
                }
                if(iter.rarity == CardRarity.RARE) {
                    is_only_rare = false;
                }
            }
        }

        for(AbstractCard iter : AbstractDungeon.player.drawPile.group) {
            if(iter != this) {
                if(iter.type == CardType.ATTACK) {
                    is_only_attack = false;
                }
                if(iter.rarity == CardRarity.RARE) {
                    is_only_rare = false;
                }
            }
        }
        if(is_only_attack) {
            this.newCost(0);
        }
        else if(is_only_rare) {
            this.newCost(2);
        }
        else {
            this.newCost(3);
        }
        initializeDescription();
    }

    protected void newCost(int newBaseCost) {
        int diff = this.costForTurn - this.cost;
        this.cost = newBaseCost;
        if (this.costForTurn > 0) {
            this.costForTurn = this.cost + diff;
        }

        if (this.costForTurn < 0) {
            this.costForTurn = 0;
        }
    }

    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        setCost();
    }

    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        super.triggerOnCardPlayed(cardPlayed);
        setCost();
    }
    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAM);
            initializeDescription();
        }
    }
}
