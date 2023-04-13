package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.ElixirAction;
import BlueArchive_Aris.actions.RandomForgeAction;
import BlueArchive_Aris.actions.RandomUpgradeAction;
import BlueArchive_Aris.actions.SelectAndCopyCardAction;
import BlueArchive_Aris.cards.custom.GainValue;
import BlueArchive_Hoshino.DefaultMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.cards.tempCards.Insight;
import com.megacrit.cardcrawl.cards.tempCards.Omega;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import com.megacrit.cardcrawl.powers.watcher.ForesightPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import org.apache.logging.log4j.LogManager;

import java.util.*;

import static org.apache.commons.lang3.math.NumberUtils.max;
import static org.apache.commons.lang3.math.NumberUtils.min;

public abstract class CustomCard extends AbstractDynamicCard {
    public static class ability {
        String name;
        String description;
        GainValue weight; //레어도
        GainValue cost; //코스트
        int min_level;
        int max_level; //최대 레벨
        int level; //현재 레벨
        public ability(String name, GainValue weight, GainValue cost,  int min_level, int max_level, String description) {
            super();
            this.name = name;
            this.description = description;
            this.weight = weight;
            this.cost = cost;
            this.min_level = min_level;
            this.max_level = max_level;
            this.level = min_level;
        }
        public ability(String name, int weight, int cost,  int min_level, int max_level, String description) {
            this(name, new GainValue.GainInt(weight), new GainValue.GainInt(cost), min_level, max_level, description);
        }
        public ability(String name, GainValue weight, int cost,  int min_level, int max_level, String description) {
            this(name, weight, new GainValue.GainInt(cost), min_level, max_level, description);
        }
        public ability(String name, int weight, GainValue cost,  int min_level, int max_level, String description) {
            this(name, new GainValue.GainInt(weight), cost, min_level, max_level, description);
        }


        public ability(ability ability_) {
            this(ability_.name,ability_.weight,ability_.cost,ability_.min_level,ability_.max_level,ability_.description);
        }

        public String getDescription() {
            String description_ = description;
            while(description_.contains("%")) {
                int index = description_.indexOf("%");
                int first_index = index;
                int val = 0;
                while(description_.length() > index+1) {
                    char c = description_.charAt(++index);
                    if(c > '0' && c < '9') {
                        val = val*10 + (c-'0');
                    } else {
                        break;
                    }
                }
                description_ = description_.substring(0,first_index) + Integer.toString(val*level) + description_.substring(index);
            }


            return description_;
        }
    }

    public static final CardStrings cardStrings;

    public static final String[] TEXT;

    public static List<List<String>> excludeAbliity;
    public static List<String> onlyAttackAbliity;
    public static List<String> onlySkillAbliity;
    public static List<String> targetingAbliity;
    public static List<String> onlyExhaustAbliity;
    public static List<String> uncreatableAbliity;
    public static List<Integer> NLstring;


    public static final List<ability> abilityList;

    public int UPGRADE_PLUS_DMG = 0;
    public int UPGRADE_PLUS_BLOCK = 0;
    public int UPGRADE_PLUS_MAGIC = 0;
    public CustomCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

    public boolean alterDamage = false;

    public static void initializeCustomCard(CustomCard card, List<CustomGameCard.ability> currentAbility_) {

        if (card.type == CardType.SKILL) {
            card.target = CardTarget.SELF;
            for (String includes : targetingAbliity) {
                for (CustomGameCard.ability ability_ : currentAbility_) {
                    if (Objects.equals(includes, ability_.name)) {
                        card.target = CardTarget.ENEMY;
                    }
                }
            }
        }

        for (CustomGameCard.ability ability_ : currentAbility_) {
            if (Objects.equals(ability_.name, "innate")) {
                card.isInnate = true;
            } else if (Objects.equals(ability_.name, "ratain")) {
                card.selfRetain = true;
            } else if (Objects.equals(ability_.name, "deal")) {
                card.UPGRADE_PLUS_DMG = 3;
                card.UPGRADE_PLUS_DMG += max(0, ability_.level / 5 - 1);
                card.baseDamage = ability_.level;
            } else if (Objects.equals(ability_.name, "dealAll")) {
                card.target = CardTarget.ALL_ENEMY;
                card.alterDamage = true;
            } else if (Objects.equals(ability_.name, "dealRepeat")) {
                card.UPGRADE_PLUS_DMG = card.baseDamage / ((ability_.level - 1) * 5 + 5) + 1;
                card.alterDamage = true;
            } else if (Objects.equals(ability_.name, "dealRandomlyOnce")) {
                card.target = CardTarget.ALL_ENEMY;
                card.alterDamage = true;
            } else if (Objects.equals(ability_.name, "dealRandomly")) {
                card.UPGRADE_PLUS_DMG = card.baseDamage / ((ability_.level - 1) * 5 + 5) + 1;
                card.target = CardTarget.ALL_ENEMY;
                card.alterDamage = true;
            } else if (Objects.equals(ability_.name, "block")) {
                if (card.UPGRADE_PLUS_DMG > 0) {
                    int block_upgrade = 2;
                    block_upgrade += max(0, ability_.level / 5 - 1);
                    card.UPGRADE_PLUS_DMG -= block_upgrade;
                    if (card.UPGRADE_PLUS_DMG <= 0) {
                        card.UPGRADE_PLUS_DMG = 1;
                    }
                    card.UPGRADE_PLUS_BLOCK = block_upgrade;
                } else {
                    card.UPGRADE_PLUS_BLOCK = 3;
                    card.UPGRADE_PLUS_BLOCK += max(0, ability_.level / 5 - 1);
                }
                card.baseBlock = ability_.level;
            } else if (Objects.equals(ability_.name, "ethereal")) {
                card.isEthereal = true;
            } else if (Objects.equals(ability_.name, "exhaust")) {
                card.exhaust = true;
            } else if (Objects.equals(ability_.name, "draw")) {
                card.magicNumber = card.baseMagicNumber = ability_.level;
                card.UPGRADE_PLUS_MAGIC = 1;
            } else if (Objects.equals(ability_.name, "poison")) {
                card.magicNumber = card.baseMagicNumber = ability_.level;
                card.UPGRADE_PLUS_MAGIC += max(1, ability_.level * 5 / 2);
            } else if (Objects.equals(ability_.name, "poisonall")) {
                card.magicNumber = card.baseMagicNumber = ability_.level;
                card.UPGRADE_PLUS_MAGIC += max(1, ability_.level * 5 / 2);
            } else if (Objects.equals(ability_.name, "vigor")) {
                card.magicNumber = card.baseMagicNumber = ability_.level;
                card.UPGRADE_PLUS_MAGIC += max(1, ability_.level * 5 / 3);
            } else if (Objects.equals(ability_.name, "dagger")) {
                card.cardsToPreview = new Shiv();
            } else if (Objects.equals(ability_.name, "deckWound")) {
                card.cardsToPreview = new Wound();
            } else if (Objects.equals(ability_.name, "deckDazed")) {
                card.cardsToPreview = new Dazed();
            } else if (Objects.equals(ability_.name, "deckVoid")) {
                card.cardsToPreview = new VoidCard();
            } else if (Objects.equals(ability_.name, "deckBurn")) {
                card.cardsToPreview = new Burn();
            } else if (Objects.equals(ability_.name, "handWound")) {
                card.cardsToPreview = new Wound();
            } else if (Objects.equals(ability_.name, "handBurn")) {
                card.cardsToPreview = new Burn();
            } else if (Objects.equals(ability_.name, "discardBurn")) {
                card.cardsToPreview = new Burn();
            } else if (Objects.equals(ability_.name, "discardWound")) {
                card.cardsToPreview = new Wound();
            } else if (Objects.equals(ability_.name, "omega")) {
                card.cardsToPreview = new Omega();
            }

        }
    }


    public void use(AbstractPlayer p, AbstractMonster m, List<CustomGameCard.ability> currentAbility_) {
        for (ability ability_ : currentAbility_) {
            if (Objects.equals(ability_.name, "loseHealth")) {
                this.addToBot(new LoseHPAction(p, p, ability_.level * 3));
            } else if (Objects.equals(ability_.name, "deal") && !alterDamage) {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            } else if (Objects.equals(ability_.name, "dealAll")) {
                this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
            } else if (Objects.equals(ability_.name, "dealRepeat")) {
                for (int i = 0; i < ability_.level; i++) {
                    AbstractDungeon.actionManager.addToBottom(
                            new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
            } else if (Objects.equals(ability_.name, "dealRandomlyOnce")) {
                this.addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            } else if (Objects.equals(ability_.name, "dealRandomly")) {
                for (int i = 0; i < ability_.level; i++) {
                    this.addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
            } else if (Objects.equals(ability_.name, "lighting")) {
                for (int i = 0; i < ability_.level; ++i)
                    this.addToBot(new ChannelAction(new Lightning()));
            } else if (Objects.equals(ability_.name, "cold")) {
                for (int i = 0; i < ability_.level; ++i)
                    this.addToBot(new ChannelAction(new Frost()));
            } else if (Objects.equals(ability_.name, "dark")) {
                for (int i = 0; i < ability_.level; ++i)
                    this.addToBot(new ChannelAction(new Dark()));
            } else if (Objects.equals(ability_.name, "block")) {
                this.addToBot(new GainBlockAction(p, p, this.block));
            } else if (Objects.equals(ability_.name, "intangible")) {
                this.addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, ability_.level), ability_.level));
            } else if (Objects.equals(ability_.name, "strength")) {
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, ability_.level), ability_.level));
            } else if (Objects.equals(ability_.name, "dexterity")) {
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, ability_.level), ability_.level));
            } else if (Objects.equals(ability_.name, "tempstrength")) {
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, ability_.level), ability_.level));
                this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, ability_.level), ability_.level));
            } else if (Objects.equals(ability_.name, "tempdexterity")) {
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, ability_.level), ability_.level));
                this.addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, ability_.level), ability_.level));
            } else if (Objects.equals(ability_.name, "platearmour")) {
                this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, ability_.level), ability_.level));
            } else if (Objects.equals(ability_.name, "vigor")) {
                this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber), this.magicNumber));
            } else if (Objects.equals(ability_.name, "divinity")) {
                this.addToBot(new ChangeStanceAction("Divinity"));
            } else if (Objects.equals(ability_.name, "potion")) {
                this.addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
            } else if (Objects.equals(ability_.name, "heal")) {
                this.addToBot(new HealAction(p, p, ability_.level));
            } else if (Objects.equals(ability_.name, "blockkeep")) {
                this.addToBot(new ApplyPowerAction(p, p, new BlurPower(p, ability_.level), ability_.level));
            } else if (Objects.equals(ability_.name, "destrength")) {
                this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -ability_.level), -ability_.level));
            } else if (Objects.equals(ability_.name, "poison")) {
                this.addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
            } else if (Objects.equals(ability_.name, "poisonall")) {
                if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    Iterator var3 = AbstractDungeon.getMonsters().monsters.iterator();

                    while (var3.hasNext()) {
                        AbstractMonster monster = (AbstractMonster) var3.next();
                        if (!monster.isDead && !monster.isDying) {
                            this.addToBot(new ApplyPowerAction(monster, p, new PoisonPower(monster, p, this.magicNumber), this.magicNumber));
                        }
                    }
                }
            } else if (Objects.equals(ability_.name, "destrengthtemp")) {
                this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -ability_.level), -ability_.level));
                if (m != null && !m.hasPower("Artifact")) {
                    this.addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, ability_.level), ability_.level, true, AbstractGameAction.AttackEffect.NONE));
                }
            } else if (Objects.equals(ability_.name, "weak")) {
                if (m != null) {
                    this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, ability_.level, false), ability_.level));
                }
            } else if (Objects.equals(ability_.name, "weakAll")) {
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, ability_.level, false), ability_.level, true, AbstractGameAction.AttackEffect.NONE));
                }
            } else if (Objects.equals(ability_.name, "vulun")) {
                if (m != null) {
                    this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, ability_.level, false), ability_.level));
                }
            } else if (Objects.equals(ability_.name, "vulunAll")) {
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    this.addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, ability_.level, false), ability_.level, true, AbstractGameAction.AttackEffect.NONE));
                }
            } else if (Objects.equals(ability_.name, "weakAndVulun")) {
                if (m != null) {
                    this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, ability_.level, false), ability_.level));
                    this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, ability_.level, false), ability_.level));
                }
            } else if (Objects.equals(ability_.name, "scry")) {
                this.addToBot(new ScryAction(ability_.level));

            } else if (Objects.equals(ability_.name, "draw")) {
                this.addToBot(new DrawCardAction(p, this.magicNumber));
            } else if (Objects.equals(ability_.name, "exhaustOther")) {
                this.addToBot(new ExhaustAction(1, false));
            } else if (Objects.equals(ability_.name, "exhaustOtherRandom")) {
                this.addToBot(new ExhaustAction(1, true, false, false));
            } else if (Objects.equals(ability_.name, "insight")) {
                AbstractCard card = new Insight();
                this.addToBot(new MakeTempCardInDrawPileAction(card, ability_.level, true, true, false));
            } else if (Objects.equals(ability_.name, "discardOther")) {
                this.addToBot(new DiscardAction(p, p, 1, false));
            } else if (Objects.equals(ability_.name, "discardOtherRandom")) {
                this.addToBot(new DiscardAction(p, p, 1, true));
            } else if (Objects.equals(ability_.name, "gainEnergy")) {
                this.addToBot(new GainEnergyAction(1));
            } else if (Objects.equals(ability_.name, "gainEnergyTwo")) {
                this.addToBot(new GainEnergyAction(2));
            } else if (Objects.equals(ability_.name, "gainEnergyNext")) {
                this.addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, 1), 1));
            } else if (Objects.equals(ability_.name, "gainEnergyNextTwo")) {
                this.addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, 2), 2));
            } else if (Objects.equals(ability_.name, "exhaustAll")) {
                this.addToBot(new ExhaustAction(9, true, false, false));
            } else if (Objects.equals(ability_.name, "discardAll")) {
                this.addToBot(new DiscardAction(p, p, 9, true));
            } else if (Objects.equals(ability_.name, "randomAttack")) {
                AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.ATTACK).makeCopy();
                c.setCostForTurn(0);
                this.addToBot(new MakeTempCardInHandAction(c, true));
            } else if (Objects.equals(ability_.name, "randomSkill")) {
                AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.SKILL).makeCopy();
                c.setCostForTurn(0);
                this.addToBot(new MakeTempCardInHandAction(c, true));
            } else if (Objects.equals(ability_.name, "randomPower")) {
                AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.POWER).makeCopy();
                c.setCostForTurn(0);
                this.addToBot(new MakeTempCardInHandAction(c, true));
            } else if (Objects.equals(ability_.name, "randomCard")) {
                AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy();
                c.setCostForTurn(0);
                this.addToBot(new MakeTempCardInHandAction(c, true));
            } else if (Objects.equals(ability_.name, "dagger")) {
                this.addToBot(new MakeTempCardInHandAction(new Shiv(), ability_.level));
            } else if (Objects.equals(ability_.name, "upgrade")) {
                this.addToBot(new ArmamentsAction(false));
            } else if (Objects.equals(ability_.name, "upgradeRandom")) {
                this.addToBot(new RandomUpgradeAction(this, ability_.level));
            } else if (Objects.equals(ability_.name, "upgradeAll")) {
                this.addToBot(new ArmamentsAction(true));
            } else if (Objects.equals(ability_.name, "copy")) {
                this.addToBot(new SelectAndCopyCardAction(p, ability_.level));
            } else if (Objects.equals(ability_.name, "copyThis")) {
                this.addToBot(new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(), ability_.level));
            } else if (Objects.equals(ability_.name, "deckWound")) {
                this.addToBot(new MakeTempCardInDrawPileAction(new Wound(), ability_.level, true, true));
            } else if (Objects.equals(ability_.name, "deckDazed")) {
                this.addToBot(new MakeTempCardInDrawPileAction(new Dazed(), ability_.level, true, true));
            } else if (Objects.equals(ability_.name, "deckVoid")) {
                this.addToBot(new MakeTempCardInDrawPileAction(new VoidCard(), ability_.level, true, true));
            } else if (Objects.equals(ability_.name, "deckBurn")) {
                this.addToBot(new MakeTempCardInDrawPileAction(new Burn(), ability_.level, true, true));
            } else if (Objects.equals(ability_.name, "handWound")) {
                this.addToBot(new MakeTempCardInHandAction(new Wound(), ability_.level));
            } else if (Objects.equals(ability_.name, "handBurn")) {
                this.addToBot(new MakeTempCardInHandAction(new Burn(), ability_.level));
            } else if (Objects.equals(ability_.name, "discardBurn")) {
                this.addToBot(new MakeTempCardInDiscardAction(new Burn(), ability_.level));
            } else if (Objects.equals(ability_.name, "discardWound")) {
                this.addToBot(new MakeTempCardInDiscardAction(new Wound(), ability_.level));
            } else if (Objects.equals(ability_.name, "upgradeEternal")) {
                this.addToBot(new RandomForgeAction());
            } else if (Objects.equals(ability_.name, "maxhpUp")) {
                this.addToBot(new ElixirAction(ability_.level, ability_.level));
            } else if (Objects.equals(ability_.name, "gold")) {
                AbstractDungeon.effectList.add(new RainingGoldEffect(ability_.level, true));
                this.addToBot(new GainGoldAction(ability_.level));
            } else if (Objects.equals(ability_.name, "omega")) {
                this.addToBot(new MakeTempCardInDrawPileAction(new Omega(), ability_.level, true, true));
            } else if (Objects.equals(ability_.name, "dieNextTurn")) {
                this.addToBot(new ApplyPowerAction(p, p, new EndTurnDeathPower(p)));
            } else if (Objects.equals(ability_.name, "handRetain")) {
                this.addToBot(new ApplyPowerAction(p, p, new EquilibriumPower(p, ability_.level), ability_.level));
            }
        }
    }

    public void makeDescription(List<CustomGameCard.ability> currentAbility_) {
            this.rawDescription = "";
            boolean damage_text_hide = false;
            for(ability ability_ : currentAbility_) {
                if(Objects.equals(ability_.name, "dealAll")
                        || Objects.equals(ability_.name, "dealRepeat")
                        || Objects.equals(ability_.name, "dealRandomlyOnce")
                        || Objects.equals(ability_.name, "dealRandomly"))
                    damage_text_hide = true;
            }

            Iterator iter = abilityList.iterator();
            ability check_NL = (ability) iter.next();
            int i = 0;
            boolean need_NL = false;

            if (currentAbility_.size() > 0 && currentAbility_.get(0).name != "innate" && isInnate) {
                this.rawDescription += abilityList.get(0).getDescription();
                need_NL = true;
            }

            for(ability ability_ : currentAbility_) {
                while(iter.hasNext() && !Objects.equals(ability_.name, check_NL.name) ) {
                    check_NL = (ability) iter.next();
                    i++;
                    if(NLstring.contains(i) && need_NL) {
                        this.rawDescription += "NL ";
                        need_NL = false;
                    }
                }
                if(!Objects.equals(ability_.name, "deal") || !damage_text_hide) {
                    this.rawDescription += ability_.getDescription();
                    need_NL = true;
                }
            }
            return;


    }

    public void triggerWhenDrawn(List<CustomGameCard.ability> currentAbility_) {
        for (ability ability_ : currentAbility_) {
            if (Objects.equals(ability_.name, "voidWhenDrawn")) {
                this.addToBot(new LoseEnergyAction(1));
            }
        }
    }


    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if(UPGRADE_PLUS_DMG>0)
                upgradeDamage(UPGRADE_PLUS_DMG);
            if(UPGRADE_PLUS_BLOCK>0)
                upgradeBlock(UPGRADE_PLUS_BLOCK);

            if(UPGRADE_PLUS_BLOCK == 0 && UPGRADE_PLUS_DMG == 0 && UPGRADE_PLUS_MAGIC > 0)  {
                upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            }
            else if(UPGRADE_PLUS_BLOCK == 0 && UPGRADE_PLUS_DMG == 0 && cost == 0)  {
                isInnate = true;
            }
            else if(UPGRADE_PLUS_BLOCK == 0 && UPGRADE_PLUS_DMG == 0 && cost > 1) {
                this.upgradeBaseCost(cost-1);
            }
        }
    }


    static private boolean isAbleAbility(ability baseAbility, List<ability> abilityList, CardType type, boolean isCreated) {
        for(ability ability_ : abilityList) {
            if(Objects.equals(ability_.name, baseAbility.name)) {
                return false;
            }
        }

        for(List<String> excludes : excludeAbliity) {
            boolean checkSkill = false;
            for(String exclude : excludes) {
                if(Objects.equals(baseAbility.name, exclude)) {
                    checkSkill = true;
                }
            }
            if(checkSkill) {
                for(String exclude : excludes) {
                    for(ability ability_ : abilityList) {
                        if(Objects.equals(ability_.name, exclude)) {
                            return false;
                        }
                    }
                }
            }
        }
        if(type == CardType.SKILL) {
            for(String excludes : onlyAttackAbliity) {
                if(Objects.equals(excludes, baseAbility.name)) {
                    return false;
                }
            }
        }
        if(type == CardType.ATTACK) {
            for(String excludes : onlySkillAbliity) {
                if(Objects.equals(excludes, baseAbility.name)) {
                    return false;
                }
            }
        }
        for(String abli_ :onlyExhaustAbliity) {
            if(Objects.equals(abli_, baseAbility.name)) {
                if(!abilityList.contains("exhaust")) {
                    return false;
                }
            }
        }
        for(String abli_ :uncreatableAbliity) {
            if(Objects.equals(abli_, baseAbility.name)) {
                if(isCreated) {
                    return false;
                }
            }
        }

        return true;
    }


    static public AbstractCard makeCustomCard(CustomCard card, int cost, int value, CardType type, List<CustomGameCard.ability> currentAbility_, boolean isCreated) {

        int ability_num = AbstractDungeon.miscRng.random(
                cost==0?2:(AbstractDungeon.miscRng.randomBoolean(0.5f)?2:3),
                cost==3?(AbstractDungeon.miscRng.randomBoolean(0.6f)?4:3):
                        (AbstractDungeon.miscRng.randomBoolean(0.3f)?4:3));
        int panalty_ability_num = AbstractDungeon.miscRng.randomBoolean(0.3f)?1:(AbstractDungeon.miscRng.randomBoolean(0.9f)?0:2);

        List<ability> readyAbility = new ArrayList<ability>();

        ability dealAbil = new ability(CustomGameCard.abilityList.get(5));

        if(type == CardType.ATTACK) {
            int possible_level = value/2/dealAbil.cost.value(dealAbil, readyAbility);
            dealAbil.level = AbstractDungeon.miscRng.random(cost*2+1, possible_level);

            value -= dealAbil.cost.value(dealAbil, readyAbility) * dealAbil.level;
            readyAbility.add(dealAbil);
            ability_num--;
        }

        int multiple = 1;
        int skillmore = max(0,min(4-ability_num,2));


        while ((panalty_ability_num>0 || ability_num>0 ) && value > 10) {
            List<ability> ableAbilList = new ArrayList<ability>();
            int totalWeight = 0;

            for(ability ability_ : abilityList) {
                if(panalty_ability_num > 0) {
                    if(ability_.cost.value(dealAbil, readyAbility) < 0
                            && ability_.weight.value(dealAbil, readyAbility) > 0
                            && isAbleAbility(ability_, readyAbility, type, isCreated)
                    ) {
                        ableAbilList.add(new ability(ability_));
                        totalWeight += ability_.weight.value(dealAbil, readyAbility);
                    }
                } else {
                    if(ability_.cost.value(dealAbil, readyAbility) * ability_.min_level <= value
                            && ability_.weight.value(dealAbil, readyAbility) > 0
                            && ability_.cost.value(dealAbil, readyAbility) > 0
                            && isAbleAbility(ability_, readyAbility, type, isCreated)
                    ) {
                        if(cost == 0 && ability_.name == "innate" && type == CardType.SKILL) {
                            continue;//선천성은 강화용으로 남김
                        }
                        ableAbilList.add(new ability(ability_));
                        totalWeight += ability_.weight.value(dealAbil, readyAbility);
                    }
                }
            }
            if(totalWeight > 0) {
                int rand_weight = AbstractDungeon.miscRng.random(1, totalWeight);

                for(ability ability_ : ableAbilList) {
                    rand_weight-=ability_.weight.value(dealAbil, readyAbility);
                    if(rand_weight <= 0) {
                        if(Objects.equals(ability_.name, "dealRepeat") || Objects.equals(ability_.name, "dealRandomly")) {
                            value+=dealAbil.level * dealAbil.cost.value(dealAbil, readyAbility);
                        }
                        int min_level = ability_.min_level;
                        int max_level =(panalty_ability_num>0) ? ability_.max_level : max(min_level,min(ability_.max_level, value/ability_.cost.value(dealAbil, readyAbility)));
                        ability_.level = AbstractDungeon.miscRng.random(min_level, max_level);

                        if(Objects.equals(ability_.name, "dealRepeat") || Objects.equals(ability_.name, "dealRandomly")) {
                            multiple =ability_.level;
                        }
                        value -= ability_.cost.value(dealAbil, readyAbility) * ability_.level;
                        readyAbility.add(ability_);
                        break;
                    }
                }
            }

            if(panalty_ability_num>0) {
                panalty_ability_num--;
            }
            else if(ability_num>0) {
                ability_num--;
            }

            if (type == CardType.SKILL && ability_num == 0 && skillmore > 0 && value > 100) {
                skillmore--;
                ability_num++;
            }
        }

        if(type == CardType.ATTACK) {
            if(value > dealAbil.cost.value(dealAbil, readyAbility)*multiple) {
                dealAbil.level += value/dealAbil.cost.value(dealAbil, readyAbility)/multiple;
            }
        } else {
            for(ability ability_ : readyAbility) {
                if(value < 10) {
                    break;
                }
                int value_ = ability_.cost.value(dealAbil, readyAbility);
                if(value_ > 0
                        && ability_.level < ability_.max_level
                        && value_ < value
                ) {
                    int levelUP = min(ability_.max_level-ability_.level, (int)(value/value_));
                    ability_.level += levelUP;
                    value-=levelUP*value_;
                }
            }

            if(value>10) {
                logger.info("remain value: " + value);
            }

        }

        currentAbility_.clear();
        for(ability order : abilityList) {
            for(ability ability_ : readyAbility) {
                if(Objects.equals(ability_.name, order.name)) {
                    currentAbility_.add(ability_);
                }
            }
        }

        card.misc = 2;
        card.cost = cost;
        card.costForTurn = cost;
        return card;
    }




    public static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(CustomGameCard.class.getName());



    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(DefaultMod.makeArisID(CustomGameCard.class.getSimpleName()));
        TEXT = cardStrings.EXTENDED_DESCRIPTION;

        abilityList = new ArrayList<>(Arrays.asList(
                new ability("innate", 30, 30, 1, 1, TEXT[0]),
                new ability("ethereal", 100, -10, 1, 1, TEXT[1]),
                new ability("ratain", 10, 16, 1, 1, TEXT[2]),
                new ability("scry", 10, 4, 1, 5, TEXT[3]),
                new ability("loseHealth", 50, -30, 1, 2, TEXT[4]),
                new ability("deal", 0, 8, 1, 50, TEXT[5]), //기본으로 들어가야함
                new ability("dealAll", 100, 25, 1, 1, TEXT[6]),   //광역
                new ability("dealRepeat", 100, (CustomGameCard.ability base, List<CustomGameCard.ability> list) ->
                {
                    return base.level * base.cost.value(base, list);
                }, 2, 5, TEXT[7]),   //멀티딜 (특수: 현재 deal코스트만큼)
                new ability("dealRandomlyOnce", 30, -24, 1, 1, TEXT[8]),//무작위 (특수: 현재 deal코스트만큼)
                new ability("dealRandomly", 20, (CustomGameCard.ability base, List<CustomGameCard.ability> list) ->
                {
                    return base.level * base.cost.value(base, list) * 10 / 7;
                }, 2, 3, TEXT[9]),//무작위 (특수: 현재 deal코스트만큼)
                new ability("lighting", 15, 30, 1, 3, TEXT[10]),
                new ability("cold", 15, 30, 1, 3, TEXT[11]),
                new ability("dark", 10, 36, 1, 1, TEXT[12]),
                new ability("block", 100, 12, 1, 50, TEXT[13]),  //방어도: 하나 10당,
                new ability("intangible", 2, 180, 1, 3, TEXT[14]),
                new ability("strength", 10, 50, 1, 5, TEXT[15]),
                new ability("dexterity", 10, 50, 1, 5, TEXT[16]),
                new ability("tempstrength", 20, 30, 1, 10, TEXT[17]),
                new ability("tempdexterity", 20, 30, 1, 10, TEXT[18]),
                new ability("platearmour", 10, 30, 1, 8, TEXT[19]),
                new ability("vigor", 50, 10, 1, 20, TEXT[20]),
                new ability("divinity", 1, 350, 1, 1, TEXT[21]),
                new ability("potion", 2, 150, 1, 1, TEXT[22]),
                new ability("heal", 3, 15, 1, 10, TEXT[23]),
                new ability("blockkeep", 5, 40, 1, 1, TEXT[24]),
                new ability("destrength", 10, 70, 1, 5, TEXT[25]),
                new ability("poison", 25, 15, 1, 8, TEXT[26]),
                new ability("poisonall", 15, 30, 1, 5, TEXT[27]),
                new ability("destrengthtemp", 10, 15, 1, 20, TEXT[28]),
                new ability("weak", 20, 20, 1, 3, TEXT[29]),
                new ability("weakAll", 10, 30, 1, 3, TEXT[30]),
                new ability("vulun", 20, 20, 1, 3, TEXT[31]),
                new ability("vulunAll", 10, 30, 1, 3, TEXT[32]),
                new ability("weakAndVulun", 20, 35, 1, 3, TEXT[33]),
                new ability("draw", 80, 30, 1, 4, TEXT[34]),   //드로우:
                new ability("exhaustOther", 20, 30, 1, 1, TEXT[35]),
                new ability("exhaustOtherRandom", 10, 20, 1, 1, TEXT[36]), //드로우:
                new ability("insight", 10, 30, 1, 1, TEXT[37]),
                new ability("discardOther", 20, -15, 1, 1, TEXT[38]),
                new ability("discardOtherRandom", 15, -30, 1, 1, TEXT[39]), //드로우:
                new ability("gainEnergy", 20, 60, 1, 1, TEXT[40]),
                new ability("gainEnergyTwo", 10, 150, 1, 1, TEXT[41]),
                new ability("gainEnergyNext", 20, 30, 1, 1, TEXT[42]),
                new ability("gainEnergyNextTwo", 10, 80, 1, 1, TEXT[43]),
                new ability("exhaustAll", 2, 50, 1, 1, TEXT[44]),
                new ability("discardAll", 3, -50, 1, 1, TEXT[45]),
                new ability("randomAttack", 5, 100, 1, 1, TEXT[46]),
                new ability("randomSkill", 5, 100, 1, 1, TEXT[47]),
                new ability("randomPower", 3, 150, 1, 1, TEXT[48]),
                new ability("randomCard", 5, 70, 1, 1, TEXT[49]),
                new ability("dagger", 20, 28, 1, 5, TEXT[50]),
                new ability("upgrade", 20, 30, 1, 1, TEXT[51]),
                new ability("upgradeRandom", 10, 20, 1, 3, TEXT[52]),
                new ability("upgradeAll", 5, 60, 1, 1, TEXT[53]),
                new ability("copy", 2, 120, 1, 1, TEXT[54]),
                new ability("copyThis", 2, 50, 1, 1, TEXT[55]),
                new ability("deckWound", 10, -25, 1, 1, TEXT[56]),
                new ability("deckDazed", 10, -15, 1, 1, TEXT[57]),
                new ability("deckVoid", 5, -50, 1, 1, TEXT[58]),
                new ability("deckBurn", 10, -35, 1, 1, TEXT[59]),
                new ability("handWound", 10, -15, 1, 3, TEXT[60]),
                new ability("handBurn", 10, -25, 1, 3, TEXT[61]),
                new ability("discardBurn", 10, -20, 1, 1, TEXT[62]),
                new ability("discardWound", 10, -15, 1, 1, TEXT[63]),
                new ability("upgradeEternal", 2, 80, 1, 1, TEXT[64]),
                new ability("maxhpUp", 2, 40, 1, 4, TEXT[65]),
                new ability("gold", 2, 8, 1, 20, TEXT[66]),
                new ability("omega", 1, 250, 1, 1, TEXT[67]),
                new ability("dieNextTurn", 1, -200, 1, 1, TEXT[68]),
                new ability("handRetain", 5, 50, 1, 1, TEXT[69]),
                new ability("voidWhenDrawn", 2, -80, 1, 1, TEXT[70]),
                new ability("exhaust", 200, -10, 1, 1, TEXT[71])
        ));

        excludeAbliity = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("dealAll", "dealRepeat", "dealRandomlyOnce", "dealRandomly")),
                new ArrayList<>(Arrays.asList("dealAll", "dealRandomlyOnce", "dealRandomly", "weak", "vulun", "weakAndVulun")), //광역에는 조준하면 안됨
                new ArrayList<>(Arrays.asList("weak", "weakAll", "vulun", "vulunAll", "weakAndVulun")),
                new ArrayList<>(Arrays.asList("exhaustOther", "exhaustOtherRandom", "discardAll", "exhaustAll")),
                new ArrayList<>(Arrays.asList("discardOther", "discardOtherRandom", "discardAll", "exhaustAll")),
                new ArrayList<>(Arrays.asList("discardOther", "exhaustOther", "scry", "upgrade")), //선택을 하나만 한다.
                new ArrayList<>(Arrays.asList("gainEnergy", "gainEnergyTwo")),
                new ArrayList<>(Arrays.asList("ratain", "ethereal")),
                new ArrayList<>(Arrays.asList("loseHealth", "heal")),
                new ArrayList<>(Arrays.asList("strength", "tempstrength")),
                new ArrayList<>(Arrays.asList("dexterity", "tempdexterity")),
                new ArrayList<>(Arrays.asList("gainEnergyNext", "gainEnergyNextTwo")),
                new ArrayList<>(Arrays.asList("randomAttack", "randomSkill", "randomPower", "randomCard")),
                new ArrayList<>(Arrays.asList("upgrade", "upgradeRandom", "upgradeAll")),
                new ArrayList<>(Arrays.asList("copyThis", "exhaust")),
                new ArrayList<>(Arrays.asList("draw", "poison", "poisonall", "vigor")), //매직넘버 사용
                new ArrayList<>(Arrays.asList("dagger", "deckWound", "deckDazed", "deckVoid", "deckBurn", "handWound", "handBurn", "discardBurn", "discardWound", "omega")),
                new ArrayList<>(Arrays.asList("discardAll", "exhaustAll", "handRetain"))
        ));

        onlyAttackAbliity = new ArrayList<>(Arrays.asList("dealAll", "dealRepeat", "dealRandomlyOnce", "dealRandomly"));
        onlySkillAbliity = new ArrayList<>(Arrays.asList("intangible", "strength", "dexterity", "tempstrength", "tempdexterity", "platearmour",
                "divinity", "potion", "copy", "omega", "vigor"));
        targetingAbliity = new ArrayList<>(Arrays.asList("weak", "vulun", "weakAndVulun", "destrength", "poison", "destrengthtemp"));
        onlyExhaustAbliity = new ArrayList<>(Arrays.asList("intangible", "divinity", "potion", "maxhpUp", "upgradeEternal", "gold", "dieNextTurn"));
        uncreatableAbliity = new ArrayList<>(Arrays.asList("heal", "potion", "gold", "maxhpUp", "upgradeEternal"));
        NLstring = new ArrayList<>(Arrays.asList(4, 71));
    }

}
