package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.ElixirAction;
import BlueArchive_Aris.actions.RandomForgeAction;
import BlueArchive_Aris.actions.RandomUpgradeAction;
import BlueArchive_Aris.actions.SelectAndCopyCardAction;
import BlueArchive_Aris.cards.custom.GainValue;
import BlueArchive_Aris.powers.RestoreStrPower;
import BlueArchive_Hoshino.DefaultMod;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Logger;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.actions.unique.DualWieldAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
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
import com.megacrit.cardcrawl.vfx.SpotlightPlayerEffect;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.*;

import static BlueArchive_Aris.events.Object.Paint.saveHandle;
import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;
import static org.apache.commons.lang3.math.NumberUtils.max;
import static org.apache.commons.lang3.math.NumberUtils.min;

public class CustomGameCard extends CustomCard {
    public static SpireConfig config;

    public static List<ability> currentAbility = new ArrayList<ability>();

    public static final String ID = DefaultMod.makeArisID(CustomGameCard.class.getSimpleName());
    public static final String IMG = makeArisCardPath("TempAttack.png");
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int DMG = 9;
    private static final int MAGIC = 0;
    static int makeCost = 1;
    static CardType makeType = CardType.ATTACK;


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
            type = makeType;

            initializeCustomCard(this, currentAbility);
        }
        makeDescription();
        initializeDescription();
    }
    public void addAlphaMapping(Pixmap pixmap) {
        if(type == CardType.ATTACK) {
            Pixmap.setBlending(Pixmap.Blending.None);
            pixmap.setColor(new Color(0.5f, 0.5f, 1.0f, 0.0f));
            pixmap.fillTriangle(0, 307, 204, 372, 0, 372);
            pixmap.fillTriangle(500, 307, 303, 372, 500, 372);
            pixmap.fillRectangle(0, 372, 500, 8);
            pixmap.fillRectangle(499, 0, 2, 380);
            Pixmap.setBlending(Pixmap.Blending.SourceOver);
        } else if (type == CardType.SKILL) {
            Pixmap.setBlending(Pixmap.Blending.None);
            pixmap.setColor(new Color(0.5f, 0.5f, 1.0f, 0.0f));
            pixmap.fillRectangle(0, 0, 4, 380);
            pixmap.fillRectangle(0, 363, 500, 17);
            pixmap.fillRectangle(492, 0, 8, 380);
            Pixmap.setBlending(Pixmap.Blending.SourceOver);
        }
    }


    public void reloadImage() {
        if(misc >= 1) {
            if(saveHandle.exists()) {
                try {
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
                } catch (Exception ignore) {
                    logger.warn("fail loading custom image:" + ignore.getMessage());
                }
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
            super.use(p, m, currentAbility);
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    public void triggerWhenDrawn() {
        if(misc == 2) {
            super.triggerWhenDrawn(currentAbility);
        }
    }

    public void makeDescription() {
        if (misc == 2) {
            makeDescription(currentAbility);
            return;
        }
        this.rawDescription = cardStrings.DESCRIPTION;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            super.upgrade();
            makeDescription();
            initializeDescription();
        }
    }
    public AbstractCard makeStatEquivalentCopy() {
        CustomGameCard card  = (CustomGameCard)super.makeStatEquivalentCopy();
        card.initializeCustomCard();
        card.reloadImage();
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

    static public AbstractCard makeCustomCard(int cost, int value, CardType type) {
        CustomGameCard card = new CustomGameCard();
        makeCost = cost;
        makeType = type;
        card = (CustomGameCard) makeCustomCard(card, cost, value, type, currentAbility, false);
        card.initializeCustomCard();
        card.reloadImage();
        return card;
    }
    static {
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
            if(config.has("type")) {
                String type = config.getString("type");
                if(type.equals("Skill")) {
                    makeType = CardType.SKILL;
                }
                else {
                    makeType = CardType.ATTACK;
                }

            }
        } catch (Throwable ignored) {
        }

    }
}
