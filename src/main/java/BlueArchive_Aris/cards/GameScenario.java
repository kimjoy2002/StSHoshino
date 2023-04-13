package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.*;
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
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
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

import java.io.IOException;
import java.util.*;

import static BlueArchive_Aris.events.Object.Paint.saveHandle;
import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;
import static org.apache.commons.lang3.math.NumberUtils.max;
import static org.apache.commons.lang3.math.NumberUtils.min;

public class GameScenario extends CustomCard {

    public List<CustomGameCard.ability> currentAbility = new ArrayList<CustomGameCard.ability>();



    public static final String ID = DefaultMod.makeArisID(GameScenario.class.getSimpleName());
    public static final String ATTACKIMG = makeArisCardPath("GameScenarioA.png");
    public static final String SKILLIMG = makeArisCardPath("GameScenario.png");
    private static final CardStrings cardStrings_gs;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int DMG = 0;
    private static final int MAGIC = 0;
    private int UPGRADE_PLUS_DMG = 0;
    private int UPGRADE_PLUS_BLOCK = 0;
    private int UPGRADE_PLUS_MAGIC = 0;

    public boolean alterDamage = false;
    public AbstractCard card;

    public GameScenario(AbstractCard card) {
        super(ID, ATTACKIMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DMG;
        this.card = card;
    }
    public GameScenario() {
        super(ID, ATTACKIMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DMG;
    }

    public void reloadImage() {
        if (type == CardType.SKILL) {
            this.textureImg = SKILLIMG;
        } else  {
            this.textureImg = ATTACKIMG;
        }
        if (textureImg != null) {
            this.loadCardImage(textureImg);
        }
    }
    public void initializeCustomCard() {
        initializeCustomCard(this, currentAbility);
        makeDescription();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m, currentAbility);
    }

    public void triggerWhenDrawn() {
        super.triggerWhenDrawn(currentAbility);
    }
    public void makeDescription() {
        if(!currentAbility.isEmpty()) {
            makeDescription(currentAbility);
            return;
        }
        this.rawDescription = cardStrings_gs.DESCRIPTION;
    }

    static public AbstractCard makeCustomCard(AbstractCard owner, int value, CardType type) {
        GameScenario card = new GameScenario(owner);
        card.type = type;
        card = (GameScenario) makeCustomCard(card, 1, value, type, card.currentAbility, true);
        card.initializeCustomCard();
        card.reloadImage();
        return card;
    }

    public void onChoseThisOption() {
        this.addToBot(new MakeTempCardInHandAction(this, 1));
        this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.discardPile));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            super.upgrade();
            initializeDescription();
        }
    }
    public AbstractCard makeStatEquivalentCopy() {
        GameScenario card  = (GameScenario)super.makeStatEquivalentCopy();
        card.currentAbility = currentAbility;
        card.initializeCustomCard();
        card.reloadImage();
        return card;
    }
    static {
        cardStrings_gs = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
