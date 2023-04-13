package BlueArchive_Aris.characters;

import BlueArchive_Aris.cards.ArisDefend;
import BlueArchive_Aris.cards.ArisStrike;
import BlueArchive_Aris.cards.EnergyCharge;
import BlueArchive_Aris.cards.SuperNova;
import BlueArchive_Aris.relics.ArisBaseRelic;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.relics.HoshinoBaseRelic;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
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

import java.util.ArrayList;
import java.util.List;

import static BlueArchive_Aris.characters.Aris.Enums.COLOR_BLUE;
import static BlueArchive_Hoshino.DefaultMod.*;
import static BlueArchive_Hoshino.DefaultMod.ARIS_SHOULDER_2;

public class Aris extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());

    public static final String BASE_ANIMATION = "baseAnimation";
    public static boolean ending_fade = false;

    public static class Enums {
        @SpireEnum
        public static PlayerClass ARIS;
        @SpireEnum(name = "DEFAULT_BLUE_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_BLUE;
        @SpireEnum(name = "DEFAULT_BLUE_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 80;
    public static final int MAX_HP = 80;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeArisID("Tendou_Aris");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer1.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer2.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer3.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer4.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer5.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer6.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer1d.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer2d.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer3d.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer4d.png",
            "BlueArchive_ArisResources/images/char/defaultCharacter/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public Aris(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
         "BlueArchive_ArisResources/images/char/defaultCharacter/orb/vfx.png", null,
                new SpineAnimation("BlueArchive_ArisResources/images/char/defaultCharacter/Aris.atlas",
                        "BlueArchive_ArisResources/images/char/defaultCharacter/Aris.json", 1f));





        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                ARIS_SHOULDER_2, // campfire pose
                ARIS_SHOULDER_1, // another campfire pose
                ARIS_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        loadAnimation(
                ARIS_SKELETON_ATLAS,
                ARIS_SKELETON_JSON,
                0.9f);
        AnimationState.TrackEntry e = state.setAnimation(0,BASE_ANIMATION, true);
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

        retVal.add(ArisStrike.ID);
        retVal.add(ArisStrike.ID);
        retVal.add(ArisStrike.ID);
        retVal.add(ArisStrike.ID);

        retVal.add(ArisDefend.ID);
        retVal.add(ArisDefend.ID);
        retVal.add(ArisDefend.ID);
        retVal.add(ArisDefend.ID);

        retVal.add(SuperNova.ID);
        retVal.add(EnergyCharge.ID);
        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(ArisBaseRelic.ID);

        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        UnlockTracker.markRelicAsSeen(ArisBaseRelic.ID);

        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        //AbstractDungeon.actionManager.addToBottom(new SFXAction("BlueArchive_Hoshino:Reload"));
        CardCrawlGame.sound.playA("BlueArchive_Aris:ArisLight", 0.0F); // Sound Effect
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
        return COLOR_BLUE;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return DefaultMod.DEFAULT_BLUE;
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
        return new ArisStrike();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new Aris(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return DefaultMod.DEFAULT_BLUE;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return DefaultMod.DEFAULT_PINK;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.LIGHTNING,
                AbstractGameAction.AttackEffect.LIGHTNING,
                AbstractGameAction.AttackEffect.LIGHTNING};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    public void newAnimation(String animation) {

        loadAnimation(
                ARIS_SKELETON_ATLAS,
                ARIS_SKELETON_JSON,
                0.9f);
        AnimationState.TrackEntry e = state.setAnimation(0,animation.isEmpty()?BASE_ANIMATION:animation, true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }



    public Texture getCutsceneBg() {
        return ImageMaster.loadImage("images/scenes/purpleBg.jpg");
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        ending_fade = false;
        List<CutscenePanel> panels = new ArrayList();
        panels.add(new CutscenePanel("BlueArchive_ArisResources/images/ending/ending_1.png"));
        panels.add(new CutscenePanel("BlueArchive_ArisResources/images/ending/ending_2.png"));
        panels.add(new CutscenePanel("BlueArchive_ArisResources/images/ending/ending_3.png"));
        return panels;
    }


    public void updateVictoryVfx(ArrayList<AbstractGameEffect> effects) {
        if (!ending_fade) {
            //effects.add(new HoshinoSmile());
            ending_fade = true;
        }

    }
}
