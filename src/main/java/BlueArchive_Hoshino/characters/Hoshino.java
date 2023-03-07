package BlueArchive_Hoshino.characters;

import BlueArchive_Hoshino.effects.HoshinoSmile;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.*;
import BlueArchive_Hoshino.relics.HoshinoBaseRelic;

import java.util.ArrayList;
import java.util.List;

import static BlueArchive_Hoshino.DefaultMod.*;
import static BlueArchive_Hoshino.characters.Hoshino.Enums.COLOR_PINK;

public class Hoshino extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());

    public static final String BASE_ANIMATION = "baseAnimation";
    public static final String SHIELD_BASE_ANIMATION = "shield_baseAnimation";
    public static final String RELOAD_ANIMATION = "reloadAnimation";
    public static final String SHIELD_RELOAD_ANIMATION = "shieldReloadAnimation";
    public static final String SHIELD_UP_ANIMATION = "shieldUpAnimation";
    public static final String SWIMSUIT_RELEASE_ANIMATION = "swimsuitReleaseAnimation";
    public static boolean ending_fade = false;

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass HOSHINO;
        @SpireEnum(name = "DEFAULT_PINK_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_PINK;
        @SpireEnum(name = "DEFAULT_PINK_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("Takanashi_Hoshino");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer1.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer2.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer3.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer4.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer5.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer6.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer1d.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer2d.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer3d.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer4d.png",
            "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public Hoshino(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
         "BlueArchive_HoshinoResources/images/char/defaultCharacter/orb/vfx.png", null,
                new SpineAnimation("BlueArchive_HoshinoResources/images/char/defaultCharacter/Hoshino.atlas",
                        "BlueArchive_HoshinoResources/images/char/defaultCharacter/Hoshino.json", 1f));





        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                THE_DEFAULT_SHOULDER_2, // campfire pose
                THE_DEFAULT_SHOULDER_1, // another campfire pose
                THE_DEFAULT_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        loadAnimation(
                THE_DEFAULT_SKELETON_ATLAS,
                THE_DEFAULT_SKELETON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, Hoshino.getNutralTextureName(false, true), true);
        e.setTime(e.getEndTime() * MathUtils.random());

        // =============== /ANIMATIONS/ =================


        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        retVal.add(HoshinoStrike.ID);
        retVal.add(HoshinoStrike.ID);
        retVal.add(HoshinoStrike.ID);
        retVal.add(HoshinoStrike.ID);
        retVal.add(HoshinoStrike.ID);

        retVal.add(HoshinoDefend.ID);
        retVal.add(HoshinoDefend.ID);
        retVal.add(HoshinoDefend.ID);
        retVal.add(HoshinoDefend.ID);

        retVal.add(QuickReloadSkill.ID);
        retVal.add(FullBarrelFire.ID);
        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(HoshinoBaseRelic.ID);

        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        UnlockTracker.markRelicAsSeen(HoshinoBaseRelic.ID);

        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        //AbstractDungeon.actionManager.addToBottom(new SFXAction("BlueArchive_Hoshino:Reload"));
        CardCrawlGame.sound.playA("BlueArchive_Hoshino:Atuiyo", 0.0F); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return COLOR_PINK;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return BlueArchive_Hoshino.DefaultMod.DEFAULT_PINK;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new HoshinoStrike();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new Hoshino(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return BlueArchive_Hoshino.DefaultMod.DEFAULT_PINK;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return BlueArchive_Hoshino.DefaultMod.DEFAULT_PINK;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }


    public static String getShieldUpTextureName() {
        boolean isSwimSuit = AbstractDungeon.player.hasPower("BlueArchive_Hoshino:SwimsuitFormPower");
        return SHIELD_UP_ANIMATION + (isSwimSuit?"_swimsuit":"");
    }

    public static String getBaseTextureName(){
        return getBaseTextureName(false);
    }
    public static String getBaseTextureName(boolean force_unswimsuit) {
        boolean isSwimSuit = force_unswimsuit ?false:  AbstractDungeon.player.hasPower("BlueArchive_Hoshino:SwimsuitFormPower");
        return BASE_ANIMATION + (isSwimSuit?"_swimsuit":"");
    }
    public static String getShieldBaseTextureName() {
        boolean isSwimSuit = AbstractDungeon.player.hasPower("BlueArchive_Hoshino:SwimsuitFormPower");
        return SHIELD_BASE_ANIMATION + (isSwimSuit?"_swimsuit":"");
    }
    public static String getNutralTextureName(){
        return getNutralTextureName(false, false);
    }
    public static String getReleaseSwimSuitTextureName() {
        return SWIMSUIT_RELEASE_ANIMATION;
    }



    public Texture getCutsceneBg() {
        return ImageMaster.loadImage("images/scenes/purpleBg.jpg");
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        ending_fade = false;
        List<CutscenePanel> panels = new ArrayList();
        panels.add(new CutscenePanel("BlueArchive_HoshinoResources/images/ending/ending_1.png"));
        panels.add(new CutscenePanel("BlueArchive_HoshinoResources/images/ending/ending_2.png"));
        panels.add(new CutscenePanel("BlueArchive_HoshinoResources/images/ending/ending_3.png"));
        return panels;
    }


    public void updateVictoryVfx(ArrayList<AbstractGameEffect> effects) {
        if (!ending_fade) {
            effects.add(new HoshinoSmile());
            ending_fade = true;
        }

    }

    public static String getNutralTextureName(boolean force_swimsuit_ , boolean force_unswimsuit_) {
        boolean isSwimSuit = force_swimsuit_ ?true :(force_unswimsuit_?false:(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:SwimsuitFormPower")));
        if(AbstractDungeon.player == null)
            return BASE_ANIMATION + (isSwimSuit?"_swimsuit":"");
        if(!AbstractDungeon.player.hasPower("BlueArchive_Hoshino:ShieldPower")) {
            return BASE_ANIMATION + (isSwimSuit?"_swimsuit":"");
        }
        else {
            return SHIELD_BASE_ANIMATION + (isSwimSuit?"_swimsuit":"");
        }
    }

    public static String getReloadTextureName() {
        boolean isSwimSuit = AbstractDungeon.player.hasPower("BlueArchive_Hoshino:SwimsuitFormPower");
        if(AbstractDungeon.player == null)
            return RELOAD_ANIMATION + (isSwimSuit?"_swimsuit":"");
        if(!AbstractDungeon.player.hasPower("BlueArchive_Hoshino:ShieldPower")) {
            return RELOAD_ANIMATION + (isSwimSuit?"_swimsuit":"");
        }
        else {
            return SHIELD_RELOAD_ANIMATION + (isSwimSuit?"_swimsuit":"");
        }
    }
}
