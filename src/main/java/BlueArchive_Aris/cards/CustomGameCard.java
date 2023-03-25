package BlueArchive_Aris.cards;

import BlueArchive_Aris.cards.custom.GainValue;
import BlueArchive_Hoshino.DefaultMod;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.io.IOException;
import java.util.*;

import static BlueArchive_Aris.events.Object.Paint.saveHandle;
import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;
import static org.apache.commons.lang3.math.NumberUtils.max;
import static org.apache.commons.lang3.math.NumberUtils.min;

public class CustomGameCard extends AbstractDynamicCard {

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

    static List<List<String>> excludeAbliity;
    static SpireConfig config;


    public static final List<ability> abilityList;

    public static List<ability> currentAbility = new ArrayList<ability>();



    public static final String ID = DefaultMod.makeArisID(CustomGameCard.class.getSimpleName());
    public static final String IMG = makeArisCardPath("TempAttack.png");
    private static final CardStrings cardStrings;

    public static final String[] TEXT;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int DMG = 9;
    private int UPGRADE_PLUS_DMG = 3;
    private int UPGRADE_PLUS_BLOCK = 0;
    static int makeCost = 1;

    public boolean alterDamage = false;

    public CustomGameCard() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DMG;
    }

    public CustomGameCard(FileHandle saveHandle, boolean oldRunPaint) {
        super(ID, null, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DMG;
        misc = oldRunPaint?2:1;
        //2 mean -> oldRunPaint Texture
        //1 mean -> CurrentRunPaint Texture
        if(saveHandle.exists()) {
            reloadImage();
        } else{
            this.textureImg = IMG;
            if (textureImg != null) {
                this.loadCardImage(textureImg);
            }
        }
    }

    public void initializeCustomCard() {
        if(misc == 2) {
            cost = makeCost;
            costForTurn = makeCost;

            for(ability ability_ : currentAbility) {
                if(Objects.equals(ability_.name, "deal")) {
                    UPGRADE_PLUS_DMG += max(0,ability_.level/5-1);
                    baseDamage = ability_.level;
                } else if(Objects.equals(ability_.name, "dealAll")) {
                    target = CardTarget.ALL_ENEMY;
                    alterDamage = true;
                } else if(Objects.equals(ability_.name, "dealRepeat")) {
                    UPGRADE_PLUS_DMG = baseDamage/((ability_.level-1)*5+5) + 1;
                    alterDamage = true;
                } else if(Objects.equals(ability_.name, "dealRandomlyOnce")) {
                    target = CardTarget.ALL_ENEMY;
                    alterDamage = true;
                }  else if(Objects.equals(ability_.name, "dealRandomly")) {
                    UPGRADE_PLUS_DMG = baseDamage/ ((ability_.level-1)*5+5) + 1;
                    target = CardTarget.ALL_ENEMY;
                    alterDamage = true;
                } else if(Objects.equals(ability_.name, "block")) {
                    int block_upgrade = 2;
                    block_upgrade += max(0,ability_.level/5-1);
                    UPGRADE_PLUS_DMG-=block_upgrade;
                    if(UPGRADE_PLUS_DMG<=0) {
                        UPGRADE_PLUS_DMG=1;
                    }
                    UPGRADE_PLUS_BLOCK = block_upgrade;
                    baseBlock = ability_.level;
                } else if(Objects.equals(ability_.name, "ethereal")) {
                    isEthereal = true;
                } else if(Objects.equals(ability_.name, "exhaust")) {
                    exhaust = true;
                }
            }
        }
        makeDescription();
        initializeDescription();
    }

    public void addAlphaMapping(Pixmap pixmap) {
        if(TYPE == CardType.ATTACK) {
            Pixmap.setBlending(Pixmap.Blending.None);
            pixmap.setColor(new Color(0.5f, 0.5f, 1.0f, 0.0f));
            pixmap.fillTriangle(0, 307, 204, 372, 0, 372);
            pixmap.fillTriangle(500, 307, 303, 372, 500, 372);
            pixmap.fillRectangle(0, 372, 500, 8);
            pixmap.fillRectangle(499, 0, 2, 380);
            Pixmap.setBlending(Pixmap.Blending.SourceOver);
        }
    }


    public void reloadImage() {
        if(misc >= 1) {
            if(saveHandle.exists()) {
                Pixmap currentPixmap = new Pixmap(saveHandle);
                addAlphaMapping(currentPixmap);
                Pixmap pixmap250 = new Pixmap(250, 190, currentPixmap.getFormat());
                pixmap250.drawPixmap(currentPixmap,
                        0, 0, currentPixmap.getWidth(), currentPixmap.getHeight(),
                        0, 0, pixmap250.getWidth(), pixmap250.getHeight()
                );
                Texture texture = new Texture(pixmap250);
                currentPixmap.dispose();
                pixmap250.dispose();
                loadCustomImage(texture);
            }
        }
    }

    protected Texture getPortraitImage() {
        if(misc >= 1) {
            if(saveHandle.exists()) {
                Pixmap currentPixmap = new Pixmap(saveHandle);
                addAlphaMapping(currentPixmap);
                Texture texture = new Texture(currentPixmap);
                currentPixmap.dispose();
                return texture;
            }
        }
        return super.getPortraitImage();
    }


    public void loadCustomImage(Texture texture) {
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        int tw = texture.getWidth();
        int th = texture.getHeight();
        this.portrait = new TextureAtlas.AtlasRegion(texture, 0, 0, tw, th);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(misc == 2) {
            for (ability ability_ : currentAbility) {
                if (Objects.equals(ability_.name, "loseHealth")) {
                    this.addToBot(new LoseHPAction(p, p, ability_.level*3));
                } else if (Objects.equals(ability_.name, "deal") && !alterDamage) {
                    AbstractDungeon.actionManager.addToBottom(
                            new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                } else if (Objects.equals(ability_.name, "dealAll")) {
                    this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
                } else if (Objects.equals(ability_.name, "dealRepeat")) {
                    for(int i = 0; i < ability_.level;i++) {
                        AbstractDungeon.actionManager.addToBottom(
                                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    }
                } else if (Objects.equals(ability_.name, "dealRandomlyOnce")) {
                    this.addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                } else if (Objects.equals(ability_.name, "dealRandomly")) {
                    for(int i = 0; i < ability_.level;i++) {
                        this.addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    }
                } else if (Objects.equals(ability_.name, "block")) {
                    this.addToBot(new GainBlockAction(p, p, this.block));
                } else if (Objects.equals(ability_.name, "weak")) {
                    if(m!=null) {
                        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m,  ability_.level, false),  ability_.level));
                    }
                } else if (Objects.equals(ability_.name, "weakAll")) {
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, ability_.level, false), ability_.level, true, AbstractGameAction.AttackEffect.NONE));
                    }
                } else if (Objects.equals(ability_.name, "vulun")) {
                    if(m!=null) {
                        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m,  ability_.level, false),  ability_.level));
                    }
                }else if (Objects.equals(ability_.name, "vulunAll")) {
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        this.addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, ability_.level, false), ability_.level, true, AbstractGameAction.AttackEffect.NONE));
                    }
                } else if (Objects.equals(ability_.name, "weakAndVulun")) {
                    if(m!=null) {
                        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m,  ability_.level, false),  ability_.level));
                        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m,  ability_.level, false),  ability_.level));
                    }
                } else if (Objects.equals(ability_.name, "draw")) {
                    this.addToBot(new DrawCardAction(p, ability_.level));
                } else if (Objects.equals(ability_.name, "exhaustOther")) {
                    this.addToBot(new ExhaustAction(1, false));
                } else if (Objects.equals(ability_.name, "exhaustOtherRandom")) {
                    this.addToBot(new ExhaustAction(1, true, false, false));
                } else if (Objects.equals(ability_.name, "discardOther")) {
                    this.addToBot(new DiscardAction(p, p, 1, false));
                } else if (Objects.equals(ability_.name, "discardOtherRandom")) {
                    this.addToBot(new DiscardAction(p, p, 1, true));
                } else if (Objects.equals(ability_.name, "gainEnergy")) {
                    this.addToBot(new GainEnergyAction(1));
                } else if (Objects.equals(ability_.name, "gainEnergyTwo")) {
                    this.addToBot(new GainEnergyAction(2));
                }
            }
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }



    public void makeDescription() {
        if(misc == 2) {
            this.rawDescription = "";
            boolean damage_text_hide = false;
            for(ability ability_ : currentAbility) {
                if(Objects.equals(ability_.name, "dealAll")
                        || Objects.equals(ability_.name, "dealRepeat")
                        || Objects.equals(ability_.name, "dealRandomlyOnce")
                        || Objects.equals(ability_.name, "dealRandomly"))
                    damage_text_hide = true;
            }
            for(ability ability_ : currentAbility) {
                if(!Objects.equals(ability_.name, "deal") || !damage_text_hide) {
                    this.rawDescription += ability_.getDescription();
                }
            }
            return;
        }

        this.rawDescription = cardStrings.DESCRIPTION;

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
            makeDescription();
            initializeDescription();
        }
    }
    public AbstractCard makeStatEquivalentCopy() {
        CustomGameCard card  = (CustomGameCard)super.makeStatEquivalentCopy();
        card.reloadImage();
        card.initializeCustomCard();
        return card;
    }

    static public void saveAbility(int cost) {
        try {
            SpireConfig config = new SpireConfig("BlueArchive_Hoshino", "CurrentCustomCardAbility");
            config.clear();
            config.save();
            for(ability ability_ : currentAbility) {
                config.setInt(ability_.name, ability_.level);
            }
            config.setInt("cost", cost);
            config.save();
        } catch (IOException ignored) {
        }
    }


    static private boolean isAbleAbility(ability baseAbility, List<ability> abilityList) {
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
        return true;
    }


    static public AbstractCard makeCustomCard(int cost, int value, CardType type) {
        makeCost = cost;
        CustomGameCard card = new CustomGameCard();
        int ability_num = AbstractDungeon.miscRng.random(cost==0?2:3,cost==3?5:4);
        int panalty_ability_num = AbstractDungeon.miscRng.randomBoolean(0.4f)?1:(AbstractDungeon.miscRng.randomBoolean(0.9f)?0:2);

        List<ability> readyAbility = new ArrayList<ability>();

        ability dealAbil = new ability(CustomGameCard.abilityList.get(2));

        int possible_level = value/2/dealAbil.cost.value(dealAbil, readyAbility);
        dealAbil.level = AbstractDungeon.miscRng.random(cost*2+1, possible_level);

        value -= dealAbil.cost.value(dealAbil, readyAbility) * dealAbil.level;
        readyAbility.add(dealAbil);
        ability_num--;
        int multiple = 1;


        while ((panalty_ability_num>0 || ability_num>0) && value > 10) {
            List<ability> ableAbilList = new ArrayList<ability>();
            int totalWeight = 0;

            for(ability ability_ : abilityList) {
                if(panalty_ability_num > 0) {
                    if(ability_.cost.value(dealAbil, readyAbility) < 0
                            && ability_.weight.value(dealAbil, readyAbility) > 0
                            && isAbleAbility(ability_, readyAbility)
                    ) {
                        ableAbilList.add(new ability(ability_));
                        totalWeight += ability_.weight.value(dealAbil, readyAbility);
                    }
                } else {
                    if(ability_.cost.value(dealAbil, readyAbility) * ability_.min_level <= value
                            && ability_.weight.value(dealAbil, readyAbility) > 0
                            && isAbleAbility(ability_, readyAbility)
                    ) {
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
                            value+=dealAbil.level * dealAbil.cost.value(dealAbil, currentAbility);
                        }
                        int min_level = ability_.min_level;
                        int max_level =(panalty_ability_num>0) ? ability_.max_level : max(min_level,min(ability_.max_level, value/ability_.cost.value(dealAbil, currentAbility)));
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
        }

        if(value > dealAbil.cost.value(dealAbil, readyAbility)*multiple) {
            dealAbil.level += value/dealAbil.cost.value(dealAbil, readyAbility)/multiple;
        }

        currentAbility.clear();
        for(ability order : abilityList) {
            for(ability ability_ : readyAbility) {
                if(Objects.equals(ability_.name, order.name)) {
                    currentAbility.add(ability_);
                }
            }
        }


        card.misc = 2;
        card.cost = cost;
        card.costForTurn = cost;
        card.reloadImage();
        card.initializeCustomCard();
        return card;
    }



    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        TEXT = cardStrings.EXTENDED_DESCRIPTION;

        abilityList = new ArrayList<>(Arrays.asList(
                new ability("ethereal",10,-10,1, 1,TEXT[0]),
                new ability("loseHealth",5, -30,1, 2,TEXT[1]),
                new ability("deal",0, 8, 1, 30,TEXT[2]), //기본으로 들어가야함
                new ability("dealAll",10, 25,1, 1,TEXT[3]),   //광역
                new ability("dealRepeat",10,(CustomGameCard.ability base, List<CustomGameCard.ability> list)->
                {return base.level * base.cost.value(base,list);},2, 5,TEXT[4]),   //멀티딜 (특수: 현재 deal코스트만큼)
                new ability("dealRandomlyOnce",3, -24,1, 1,TEXT[5]),//무작위 (특수: 현재 deal코스트만큼)
                new ability("dealRandomly",2, (CustomGameCard.ability base, List<CustomGameCard.ability> list)->
                {return base.level * base.cost.value(base,list)*10/7;}, 2, 3,TEXT[6]),//무작위 (특수: 현재 deal코스트만큼)
                new ability("block",10, 10,1, 30,TEXT[7]),  //방어도: 하나 10당
                new ability("weak",2, 20,1, 3,TEXT[8]),
                new ability("weakAll",1, 30,1, 3,TEXT[9]),
                new ability("vulun",2, 20,1, 3,TEXT[10]),
                new ability("vulunAll",1, 30,1, 3,TEXT[11]),
                new ability("weakAndVulun",2, 35,1, 3,TEXT[12]),
                new ability("draw",10, 30,1, 4,TEXT[13]),   //드로우:
                new ability("exhaustOther",5, 20,1, 1,TEXT[14]),
                new ability("exhaustOtherRandom",3, 10,1, 1,TEXT[15]), //드로우:
                new ability("discardOther",5, -15,1, 1,TEXT[16]),
                new ability("discardOtherRandom",3, -30,1, 1,TEXT[17]), //드로우:
                new ability("gainEnergy",3, 60,1, 1,TEXT[18]),
                new ability("gainEnergyTwo",1, 150,1, 1,TEXT[19]),
                new ability("exhaust",10, -10,1, 1,TEXT[20])
        ));

        excludeAbliity = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("dealAll", "dealRepeat", "dealRandomlyOnce", "dealRandomly")),
                new ArrayList<>(Arrays.asList("dealAll", "dealRandomlyOnce", "dealRandomly", "weak", "vulun", "weakAndVulun")), //광역에는 조준하면 안됨
                new ArrayList<>(Arrays.asList("weak", "weakAll", "vulun", "vulunAll", "weakAndVulun")),
                new ArrayList<>(Arrays.asList("exhaustOther", "exhaustOtherRandom")),
                new ArrayList<>(Arrays.asList("discardOther", "discardOtherRandom")),
                new ArrayList<>(Arrays.asList("discardOther", "exhaustOther")), //선택을 하나만 한다.
                new ArrayList<>(Arrays.asList("gainEnergy", "gainEnergyTwo"))
        ));


        try {
            config = new SpireConfig("BlueArchive_Hoshino", "CurrentCustomCardAbility");
            config.load();
            CustomGameCard.currentAbility.clear();
            for(ability order : abilityList) {
                if(config.has(order.name)) {
                    int level = config.getInt(order.name);
                    if(level != 0) {
                        ability a = new CustomGameCard.ability(order);
                        a.level = level;
                        CustomGameCard.currentAbility.add(a);
                    }
                }
            }
            if(config.has("cost")) {
                makeCost = config.getInt("cost");
            }
        } catch (Throwable ignored) {
        }

    }
}
