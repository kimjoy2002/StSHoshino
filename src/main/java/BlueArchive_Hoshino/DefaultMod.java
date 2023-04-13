package BlueArchive_Hoshino;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.events.CaveofClassChangeEvent;
import BlueArchive_Aris.events.SaibaMidoriEvent;
import BlueArchive_Aris.events.SaibaMomoiEvent;
import BlueArchive_Aris.potions.ChargePotion;
import BlueArchive_Aris.potions.ShockPotion;
import BlueArchive_Aris.relics.*;
import BlueArchive_Aris.variables.SecondMagicNumber;
import BlueArchive_Hoshino.events.*;
import BlueArchive_Hoshino.monsters.act1.boss.*;
import BlueArchive_Hoshino.monsters.act2.boss.*;
import BlueArchive_Hoshino.monsters.act3.boss.*;
import BlueArchive_Hoshino.monsters.act4.boss.Hifumi;
import BlueArchive_Hoshino.monsters.act4.boss.PeroroHifumi;
import BlueArchive_Hoshino.potions.BulletPotion;
import BlueArchive_Hoshino.potions.SkilledPotion;
import BlueArchive_Hoshino.relics.*;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import basemod.*;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.eventUtil.util.Condition;
import basemod.eventUtil.util.ConditionalEvent;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.util.IDCheckDontTouchPls;
import BlueArchive_Hoshino.util.TextureLoader;
import BlueArchive_Hoshino.variables.DefaultCustomVariable;
import BlueArchive_Hoshino.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static basemod.eventUtil.EventUtils.eventIDs;
import static basemod.eventUtil.EventUtils.normalEvents;

//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 4 places.
// I comment those places below, under the place where you set your ID.

//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault", and change to "yourmodname" rather than "thedefault".
// You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories, and press alt+c to make the replace case sensitive (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class DefaultMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
    private static String modID;
    private static String arisModID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties defaultSettings = new Properties();
    public static final String DISABLE_BLUEARCHIVE_BOSS = "disableBluearchiveBoss";
    public static final String ENABLE_ONLY_BLUEARCHIVE_BOSS = "enableOnlyBluearchiveBoss";
    public static final String ENABLE_ACT3_EVENT = "enableAct3Event";
    public static final String RELOAD_BUTTON_KEY = "reloadButton";
    public static final String DISABLE_COMMON_EVENT = "disableCommonEvent";
    public static boolean enableBoss = true;
    public static boolean onlyBluearchiveBoss = false;
    public static boolean enableAct3Event = false;
    public static boolean disableCommonEvent = false;
    public static int reloadKey = 46; //R
    private InputProcessor oldInputProcessor;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Default Mod";
    private static final String AUTHOR = "joy1999"; // And pretty soon - You!
    private static final String DESCRIPTION = "HOSHINO TEST.";
    ModLabeledToggleButton disableBossButton = null;
    ModLabeledToggleButton enableBossOnlyButton = null;
    ModLabeledToggleButton enableAct3EventButton = null;
    ModLabeledToggleButton disableCommonEventButton = null;

    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    public static final Color DEFAULT_PINK = CardHelper.getColor(255.0f, 208.0f, 220.0f);
    public static final Color DEFAULT_BLUE = CardHelper.getColor(10.0f, 10.0f, 200.0f);
    
    // Potion Colors in RGB
    public static final Color SKILLED_POTION_LIQUID = CardHelper.getColor(53.0f, 203.0f, 18.0f);
    public static final Color SKILLED_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f);
    public static final Color SKILLED_POTION_SPOTS = CardHelper.getColor(53.0f, 203.0f, 18.0f);

    public static final Color BULLET_POTION_LIQUID = CardHelper.getColor(203.0f, 53.0f, 18.0f);
    public static final Color BULLET_POTION_HYBRID = CardHelper.getColor(30.0f, 30.0f, 30.0f);
    public static final Color BULLET_POTION_SPOTS = CardHelper.getColor(166.0f, 88.0f, 2.0f);

    public static final Color CHARGE_POTION_LIQUID = CardHelper.getColor(53.0f, 53.0f, 203.0f);
    public static final Color CHARGE_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f);
    public static final Color CHARGE_POTION_SPOTS = CardHelper.getColor(203.0f, 203.0f, 203.0f);

    public static final Color SHOCK_POTION_LIQUID = CardHelper.getColor(100.0f, 100.0f, 233.0f);
    public static final Color SHOCK_POTION_HYBRID = CardHelper.getColor(100.0f, 100.0f, 230.0f);
    public static final Color SHOCK_POTION_SPOTS = CardHelper.getColor(233.0f, 233.0f, 233.0f);
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
  
    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_DEFAULT_GRAY = "BlueArchive_HoshinoResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = "BlueArchive_HoshinoResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = "BlueArchive_HoshinoResources/images/512/bg_power_default_gray.png";
    
    private static final String ENERGY_ORB_DEFAULT_GRAY = "BlueArchive_HoshinoResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "BlueArchive_HoshinoResources/images/512/card_small_orb.png";
    
    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "BlueArchive_HoshinoResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "BlueArchive_HoshinoResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = "BlueArchive_HoshinoResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "BlueArchive_HoshinoResources/images/1024/card_default_gray_orb.png";




    private static final String ATTACK_ARIS = "BlueArchive_ArisResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_ARIS = "BlueArchive_ArisResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_ARIS = "BlueArchive_ArisResources/images/512/bg_power_default_gray.png";

    private static final String ENERGY_ORB_ARIS = "BlueArchive_ArisResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ARIS_ENERGY_ORB = "BlueArchive_ArisResources/images/512/card_small_orb.png";

    private static final String ATTACK_ARIS_PORTRAIT = "BlueArchive_ArisResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_ARIS_PORTRAIT = "BlueArchive_ArisResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_ARIS_PORTRAIT = "BlueArchive_ArisResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_ARIS_PORTRAIT = "BlueArchive_ArisResources/images/1024/card_default_gray_orb.png";


    // Character assets
    private static final String THE_DEFAULT_BUTTON = "BlueArchive_HoshinoResources/images/charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "BlueArchive_HoshinoResources/images/charSelect/HoshinoPortrait.png";
    private static final String ARIS_BUTTON = "BlueArchive_ArisResources/images/charSelect/ArisButton.png";
    private static final String ARIS_PORTRAIT = "BlueArchive_ArisResources/images/charSelect/ArisPortrait.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "BlueArchive_HoshinoResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "BlueArchive_HoshinoResources/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "BlueArchive_HoshinoResources/images/char/defaultCharacter/corpse.png";

    public static final String ARIS_SHOULDER_1 = "BlueArchive_ArisResources/images/char/defaultCharacter/shoulder.png";
    public static final String ARIS_SHOULDER_2 = "BlueArchive_ArisResources/images/char/defaultCharacter/shoulder2.png";
    public static final String ARIS_CORPSE = "BlueArchive_ArisResources/images/char/defaultCharacter/corpse.png";


    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "BlueArchive_HoshinoResources/images/Badge.png";
    
    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = "BlueArchive_HoshinoResources/images/char/defaultCharacter/Hoshino.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "BlueArchive_HoshinoResources/images/char/defaultCharacter/Hoshino.json";

    public static final String ARIS_SKELETON_ATLAS = "BlueArchive_ArisResources/images/char/defaultCharacter/Aris.atlas";
    public static final String ARIS_SKELETON_JSON = "BlueArchive_ArisResources/images/char/defaultCharacter/Aris.json";

    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    public static String makeArisCardPath(String resourcePath) {
        return getArisModID() + "Resources/images/cards/" + resourcePath;
    }


    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    public static String makeArisRelicPath(String resourcePath) {
        return getArisModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    public static String makeArisRelicOutlinePath(String resourcePath) {
        return getArisModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeArisPowerPath(String resourcePath) {
        return getArisModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    public static String makeArisEventPath(String resourcePath) {
        return getArisModID() + "Resources/images/events/" + resourcePath;
    }
    public static String makeMonstersPath(String resourcePath) {
        return getModID() + "Resources/images/monsters/" + resourcePath;
    }
    public static String makeBgmPath(String resourcePath) {
        return getModID() + "Resources/bgm/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public DefaultMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        BaseMod.subscribe(new BulletSubscriber());

        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID("BlueArchive_Hoshino");
        setArisModID("BlueArchive_Aris");
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project) and press alt+c (or mark the match case option)
        // replace all instances of theDefault with yourModID, and all instances of thedefault with yourmodid (the same but all lowercase).
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        // It's important that the mod ID prefix for keywords used in the cards descriptions is lowercase!

        // 3. Scroll down (or search for "ADD CARDS") till you reach the ADD CARDS section, and follow the TODO instructions

        // 4. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + Hoshino.Enums.COLOR_PINK.toString());
        
        BaseMod.addColor(Hoshino.Enums.COLOR_PINK, DEFAULT_PINK, DEFAULT_PINK, DEFAULT_PINK,
                DEFAULT_PINK, DEFAULT_PINK, DEFAULT_PINK, DEFAULT_PINK,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);
        logger.info("Creating the color " + Aris.Enums.COLOR_BLUE.toString());
        BaseMod.addColor(Aris.Enums.COLOR_BLUE, DEFAULT_BLUE, DEFAULT_BLUE, DEFAULT_BLUE,
                DEFAULT_BLUE, DEFAULT_BLUE, DEFAULT_BLUE, DEFAULT_BLUE,
                ATTACK_ARIS, SKILL_ARIS, POWER_ARIS, ENERGY_ORB_ARIS,
                ATTACK_ARIS_PORTRAIT, SKILL_ARIS_PORTRAIT, POWER_ARIS_PORTRAIT,
                ENERGY_ORB_ARIS_PORTRAIT, CARD_ARIS_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");

        defaultSettings.setProperty(DISABLE_BLUEARCHIVE_BOSS, "FALSE");
        defaultSettings.setProperty(ENABLE_ONLY_BLUEARCHIVE_BOSS, "FALSE");
        defaultSettings.setProperty(ENABLE_ACT3_EVENT, "FALSE");
        defaultSettings.setProperty(DISABLE_COMMON_EVENT, "FALSE");
        defaultSettings.setProperty(RELOAD_BUTTON_KEY, "46");

        try {
            SpireConfig config = new SpireConfig("BlueArchive_Hoshino", "BlueArchiveConfig", defaultSettings);
            config.load();
            enableBoss = !config.getBool(DISABLE_BLUEARCHIVE_BOSS);
            onlyBluearchiveBoss = config.getBool(ENABLE_ONLY_BLUEARCHIVE_BOSS);
            enableAct3Event = config.getBool(ENABLE_ACT3_EVENT);
            disableCommonEvent = config.getBool(DISABLE_COMMON_EVENT);
            reloadKey = config.getInt(RELOAD_BUTTON_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = DefaultMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======


    public static void setArisModID(String ID) {
        Gson coolG = new Gson();
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        logger.info("You are attempting to set your Aris mod ID as: " + ID);
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) {
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION);
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) {
            arisModID = EXCEPTION_STRINGS.DEFAULTID;
        } else {
            arisModID = ID;
        }
        logger.info("Success! Aris ID is " + modID);
    }


    public static String getArisModID() {
        return arisModID;
    }


    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        DefaultMod defaultmod = new DefaultMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Hoshino.Enums.HOSHINO.toString());
        
        BaseMod.addCharacter(new Hoshino("Hoshino", Hoshino.Enums.HOSHINO),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, Hoshino.Enums.HOSHINO);
        logger.info("Added " + Hoshino.Enums.HOSHINO.toString());

        BaseMod.addCharacter(new Aris("Aris", Aris.Enums.ARIS),
                ARIS_BUTTON, ARIS_PORTRAIT, Aris.Enums.ARIS);
        logger.info("Added " + Aris.Enums.ARIS.toString());
        receiveEditPotions();
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        ModLabel buttonLabel = new ModLabel("", 475.0F, 700.0F, settingsPanel, (me) -> {
            if (me.parent.waitingOnEvent) {
                me.text = "Press key";
            } else {
                me.text = "Change reload hotkey (" + Input.Keys.toString(reloadKey) + ")";
            }

        });
        settingsPanel.addUIElement(buttonLabel);
        ModButton consoleKeyButton = new ModButton(350.0F, 650.0F, settingsPanel, (me) -> {
            me.parent.waitingOnEvent = true;
            this.oldInputProcessor = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(new InputAdapter() {
                public boolean keyUp(int keycode) {
                    reloadKey = keycode;
                    try {
                        SpireConfig config = new SpireConfig("BlueArchive_Hoshino", "BlueArchiveConfig", defaultSettings);
                        config.setInt(RELOAD_BUTTON_KEY, keycode);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    me.parent.waitingOnEvent = false;
                    Gdx.input.setInputProcessor(oldInputProcessor);
                    return true;
                }
            });
        });
        settingsPanel.addUIElement(consoleKeyButton);


        disableBossButton = new ModLabeledToggleButton("BlueArchive Boss doesn't appear.",
                350.0f, 550.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                !enableBoss,
                settingsPanel,
                (label) -> {},
                (button) -> {
            
            enableBoss = !button.enabled;
            if(enableBossOnlyButton != null && button.enabled && enableBossOnlyButton.toggle.enabled) {
                enableBossOnlyButton.toggle.toggle();
            }
            try {
                if(enableBoss){
                        enableBoss();
                }else{
                        disableBoss();
                }
            } catch (Exception e) {
                        e.printStackTrace();
            }

            try {
                SpireConfig config = new SpireConfig("BlueArchive_Hoshino", "BlueArchiveConfig", defaultSettings);
                config.setBool(DISABLE_BLUEARCHIVE_BOSS, !enableBoss);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        enableBossOnlyButton = new ModLabeledToggleButton("If possible, only the Blue Archive Boss comes out.",
                350.0f, 450.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                onlyBluearchiveBoss,
                settingsPanel,
                (label) -> {},
                (button) -> {
                    onlyBluearchiveBoss = button.enabled;
                    if(disableBossButton != null && button.enabled && disableBossButton.toggle.enabled) {
                        disableBossButton.toggle.toggle();
                    }
                    try {
                        SpireConfig config = new SpireConfig("BlueArchive_Hoshino", "BlueArchiveConfig", defaultSettings);
                        config.setBool(ENABLE_ONLY_BLUEARCHIVE_BOSS, onlyBluearchiveBoss);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        enableAct3EventButton = new ModLabeledToggleButton("The first event room in Act 3 always has a hifumi event.",
                350.0f, 350.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                enableAct3Event,
                settingsPanel,
                (label) -> {},
                (button) -> {
                    enableAct3Event = button.enabled;
                    try {
                        SpireConfig config = new SpireConfig("BlueArchive_Hoshino", "BlueArchiveConfig", defaultSettings);
                        config.setBool(ENABLE_ACT3_EVENT, enableAct3Event);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        disableCommonEventButton = new ModLabeledToggleButton("Disable Common BlueArchive Event.",
                350.0f, 250.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                disableCommonEvent,
                settingsPanel,
                (label) -> {},
                (button) -> {

                    disableCommonEvent = button.enabled;
                    try {
                        if(disableCommonEvent){
                            removeEvent();
                        }else{
                            addCommonEvent();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        SpireConfig config = new SpireConfig("BlueArchive_Hoshino", "BlueArchiveConfig", defaultSettings);
                        config.setBool(DISABLE_COMMON_EVENT, !disableCommonEvent);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });




        settingsPanel.addUIElement(disableBossButton);
        settingsPanel.addUIElement(enableBossOnlyButton);
        settingsPanel.addUIElement(enableAct3EventButton);
        settingsPanel.addUIElement(disableCommonEventButton);
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        
        // =============== EVENTS =================
        // https://github.com/daviscook477/BaseMod/wiki/Custom-Events

        // You can add the event like so:
        // BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        // Then, this event will be exclusive to the City (act 2), and will show up for all characters.
        // If you want an event that's present at any part of the game, simply don't include the dungeon ID

        // If you want to have more specific event spawning (e.g. character-specific or so)
        // deffo take a look at that basemod wiki link as well, as it explains things very in-depth
        // btw if you don't provide event type, normal is assumed by default

        // Create a new event builder
        // Since this is a builder these method calls (outside of create()) can be skipped/added as necessary
        if(!disableCommonEvent)
        {
        AddEventParams eventParams = new AddEventParams.Builder(KaiserCorporationEvent.ID, KaiserCorporationEvent.class)
                .dungeonID(Exordium.ID)
                .create();
        BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(ShibasekiRamenEvent.ID, ShibasekiRamenEvent.class)
                    .dungeonID(Exordium.ID)
                    .playerClass(Hoshino.Enums.HOSHINO)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        if(!disableCommonEvent)
        {
            AddEventParams eventParams = new AddEventParams.Builder(SaibaMidoriEvent.ID, SaibaMidoriEvent.class)
                    .dungeonID(Exordium.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(CaveofClassChangeEvent.ID, CaveofClassChangeEvent.class)
                    .dungeonID(Exordium.ID)
                    .playerClass(Aris.Enums.ARIS)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        if(!disableCommonEvent)
        {
            AddEventParams eventParams = new AddEventParams.Builder(KaiserCorporationEvent2.ID, KaiserCorporationEvent2.class)
                    .dungeonID(TheCity.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(AbydosEvent.ID, AbydosEvent.class)
                    .dungeonID(TheCity.ID)
                    .playerClass(Hoshino.Enums.HOSHINO)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        if(!disableCommonEvent)
        {
            AddEventParams eventParams = new AddEventParams.Builder(SaibaMomoiEvent.ID, SaibaMomoiEvent.class)
                    .dungeonID(TheCity.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        if(!disableCommonEvent)
        {
            AddEventParams eventParams = new AddEventParams.Builder(GachaEvent.ID, GachaEvent.class)
                    .dungeonID(TheBeyond.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(PeroroEvent.ID, PeroroEvent.class)
                    .dungeonID(TheBeyond.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }


        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
        addCustomMonster();
    }

    public  void addCommonEvent() {
        {
            AddEventParams eventParams = new AddEventParams.Builder(KaiserCorporationEvent.ID, KaiserCorporationEvent.class)
                    .dungeonID(Exordium.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(SaibaMidoriEvent.ID, SaibaMidoriEvent.class)
                    .dungeonID(Exordium.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(KaiserCorporationEvent2.ID, KaiserCorporationEvent2.class)
                    .dungeonID(TheCity.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(SaibaMomoiEvent.ID, SaibaMomoiEvent.class)
                    .dungeonID(TheCity.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(GachaEvent.ID, GachaEvent.class)
                    .dungeonID(TheBeyond.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }

    }
    public  void removeEvent() {
        removeEvent(KaiserCorporationEvent.ID);
        removeEvent(SaibaMidoriEvent.ID);
        removeEvent(KaiserCorporationEvent2.ID);
        removeEvent(SaibaMomoiEvent.ID);
        removeEvent(GachaEvent.ID);
    }

    public  void removeEvent(String ID) {
        ID = ID.replace(' ', '_');
        eventIDs.remove(ID);
        EventUtils.normalEvents.remove(ID);
    }






    public void enableBoss() {
        if(BaseMod.getBossInfo(Siro.ID) == null)
            BaseMod.addBoss("Exordium", Siro.ID, makeMonstersPath("Siro_boss.png"), makeMonstersPath("Siro_out.png"));
        if(BaseMod.getBossInfo(Binah.ID) == null)
            BaseMod.addBoss("Exordium", Binah.ID, makeMonstersPath("Binah_boss.png"), makeMonstersPath("Binah_out.png"));
        if(BaseMod.getBossInfo(KaitengerRed.ID) == null)
            BaseMod.addBoss("Exordium", KaitengerRed.ID, makeMonstersPath("Kaitenger_boss.png"), makeMonstersPath("Kaitenger_out.png"));
        if(BaseMod.getBossInfo(Hod.ID) == null)
            BaseMod.addBoss("TheCity", Hod.ID, makeMonstersPath("Hod_boss.png"), makeMonstersPath("Hod_out.png"));
        if(BaseMod.getBossInfo(Perorodzilla.ID) == null)
            BaseMod.addBoss("TheCity", Perorodzilla.ID, makeMonstersPath("Perorodzilla_boss.png"), makeMonstersPath("Perorodzilla_out.png"));
        if(BaseMod.getBossInfo(KaitenFXMK0.ID) == null)
            BaseMod.addBoss("TheCity", KaitenFXMK0.ID, makeMonstersPath("KaitenFXMK0_boss.png"), makeMonstersPath("KaitenFXMK0_out.png"));
        if(BaseMod.getBossInfo(Goz.ID) == null)
            BaseMod.addBoss("TheBeyond", Goz.ID, makeMonstersPath("Goz_boss.png"), makeMonstersPath("Goz_out.png"));
        if(BaseMod.getBossInfo(Chesed.ID) == null)
            BaseMod.addBoss("TheBeyond", Chesed.ID, makeMonstersPath("Chesed_boss.png"), makeMonstersPath("Chesed_out.png"));
        if(BaseMod.getBossInfo(Hieronymus.ID) == null)
            BaseMod.addBoss("TheBeyond", Hieronymus.ID, makeMonstersPath("Hieronymus_boss.png"), makeMonstersPath("Hieronymus_out.png"));
        if(BaseMod.getBossInfo(Hifumi.ID) == null)
            BaseMod.addBoss("TheEnding", Hifumi.ID, makeMonstersPath("Hifumi_boss.png"), makeMonstersPath("Hifumi_out.png"));

    }

    public void disableBoss(String dungeonId) {
        HashMap<String, List<BaseMod.BossInfo>> customBosses = (HashMap<String, List<BaseMod.BossInfo>>) ReflectionHacks.getPrivateStatic(BaseMod.class, "customBosses");

        if(customBosses.containsKey(dungeonId)) {
            List<BaseMod.BossInfo> boss = customBosses.get(dungeonId);
            boss.removeIf(element -> element.id.startsWith(getModID()));
        }

    }
    public void disableBoss() {
        disableBoss("Exordium");
        disableBoss("TheCity");
        disableBoss("TheBeyond");
    }

    public void addCustomMonster() {
        BaseMod.addMonster(Binah.ID, () -> {
            return new Binah();
        });
        BaseMod.addMonster(Siro.ID, () -> {
            return new Siro();
        });
        BaseMod.addMonster(Kuro.ID, () -> {
            return new Kuro();
        });
        BaseMod.addMonster(Hod.ID, () -> {
            return new Hod();
        });
        //BaseMod.addMonster(HodPillar.ID, () -> {
       //     return new HodPillar();
       // });
        BaseMod.addMonster(KaitengerRed.ID, () -> {
            return new KaitengerRed();
        });
        BaseMod.addMonster(Perorodzilla.ID, () -> {
            return new Perorodzilla();
        });
        BaseMod.addMonster(Peroro.ID, () -> {
            return new Peroro();
        });
        BaseMod.addMonster(KaitenFXMK0.ID, () -> {
            return new KaitenFXMK0();
        });
        BaseMod.addMonster(Goz.ID, () -> {
            return new Goz();
        });
        BaseMod.addMonster(Chesed.ID, () -> {
            AbstractMonster[] monsters = new AbstractMonster[] {
                    new Chesed(200.0f, 0.0f)
            };
            return new MonsterGroup(monsters);
        });
        BaseMod.addMonster(Hieronymus.ID, () -> {
            AbstractMonster[] monsters = new AbstractMonster[] {
                    new RedRelic(-450.0f, 0.0f),
                    new Hieronymus(-100.0f, 0.0f),
                    new GreenRelic(200.0f, 0.0f)
            };
            return new MonsterGroup(monsters);
        });
        BaseMod.addMonster(Hifumi.ID, () -> {
            AbstractMonster[] monsters = new AbstractMonster[] {
                    new PeroroHifumi(-400.0f, 0.0f),
                    new Hifumi()
            };

            return new MonsterGroup(monsters);
        });

        if(enableBoss) {
            enableBoss();
        }

        //TheBeyond
    }


    // =============== / POST-INITIALIZE/ =================
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        BaseMod.addPotion(SkilledPotion.class, SKILLED_POTION_LIQUID, SKILLED_POTION_HYBRID, SKILLED_POTION_SPOTS, SkilledPotion.POTION_ID, Hoshino.Enums.HOSHINO);
        BaseMod.addPotion(BulletPotion.class, BULLET_POTION_LIQUID, BULLET_POTION_HYBRID, BULLET_POTION_SPOTS, BulletPotion.POTION_ID, Hoshino.Enums.HOSHINO);



        BaseMod.addPotion(ChargePotion.class, CHARGE_POTION_LIQUID, CHARGE_POTION_HYBRID, CHARGE_POTION_SPOTS, ChargePotion.POTION_ID, Aris.Enums.ARIS);
        BaseMod.addPotion(ShockPotion.class, SHOCK_POTION_LIQUID, SHOCK_POTION_HYBRID, SHOCK_POTION_SPOTS, ShockPotion.POTION_ID, Aris.Enums.ARIS);



        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // Take a look at https://github.com/daviscook477/BaseMod/wiki/AutoAdd
        // as well as
        // https://github.com/kiooeht/Bard/blob/e023c4089cc347c60331c78c6415f489d19b6eb9/src/main/java/com/evacipated/cardcrawl/mod/bard/BardMod.java#L319
        // for reference as to how to turn this into an "Auto-Add" rather than having to list every relic individually.
        // Of note is that the bard mod uses it's own custom relic class (not dissimilar to our AbstractDefaultCard class for cards) that adds the 'color' field,
        // in order to automatically differentiate which pool to add the relic too.

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new HoshinoBaseRelic(), Hoshino.Enums.COLOR_PINK);
        BaseMod.addRelicToCustomPool(new HoshinoBaseRelicPlus(), Hoshino.Enums.COLOR_PINK);

        BaseMod.addRelicToCustomPool(new EyeOfHorusRelic(), Hoshino.Enums.COLOR_PINK);

        BaseMod.addRelicToCustomPool(new AutoReloaderRelic(), Hoshino.Enums.COLOR_PINK);
        BaseMod.addRelicToCustomPool(new AmmoBoxRelic(), Hoshino.Enums.COLOR_PINK);

        BaseMod.addRelicToCustomPool(new WhaleTubeRelic(), Hoshino.Enums.COLOR_PINK);
        BaseMod.addRelicToCustomPool(new ShellBeltRelic(), Hoshino.Enums.COLOR_PINK);
        BaseMod.addRelicToCustomPool(new TacticalSatchelBagRelic(), Hoshino.Enums.COLOR_PINK);

        BaseMod.addRelicToCustomPool(new DoubleBarrelShotgunRelic(), Hoshino.Enums.COLOR_PINK);
        BaseMod.addRelicToCustomPool(new TotemPoleRelic(), Hoshino.Enums.COLOR_PINK);

        BaseMod.addRelicToCustomPool(new IronHorusRelic(), Hoshino.Enums.COLOR_PINK);

        BaseMod.addRelicToCustomPool(new ArisBaseRelic(), Aris.Enums.COLOR_BLUE);
        BaseMod.addRelicToCustomPool(new ArisBaseRelicPlus(), Aris.Enums.COLOR_BLUE);

        BaseMod.addRelicToCustomPool(new HPPotion(), Aris.Enums.COLOR_BLUE);

        BaseMod.addRelicToCustomPool(new StartingEquipment(), Aris.Enums.COLOR_BLUE);

        BaseMod.addRelicToCustomPool(new Battery(), Aris.Enums.COLOR_BLUE);
        BaseMod.addRelicToCustomPool(new GameManual(), Aris.Enums.COLOR_BLUE);
        BaseMod.addRelicToCustomPool(new CoveredKnifeSwitch(), Aris.Enums.COLOR_BLUE);

        BaseMod.addRelicToCustomPool(new CopyCat(), Aris.Enums.COLOR_BLUE);
        BaseMod.addRelicToCustomPool(new Gameboy(), Aris.Enums.COLOR_BLUE);

        BaseMod.addRelicToCustomPool(new TASRelic(), Aris.Enums.COLOR_BLUE);


        // This adds a relic to the Shared pool. Every character can find this relic.BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);
        BaseMod.addRelic(new IOURelic(), RelicType.SHARED);
        BaseMod.addRelic(new ShirokoRelic(), RelicType.SHARED);
        BaseMod.addRelic(new NonomiRelic(), RelicType.SHARED);
        BaseMod.addRelic(new SerikaRelic(), RelicType.SHARED);
        BaseMod.addRelic(new PeroroRelic(), RelicType.SHARED);
        //BaseMod.addRelic(new HoshinoRelic(), RelicType.SHARED);
        BaseMod.addRelic(new FireSupportDroneRelic(), RelicType.SHARED);
        BaseMod.addRelic(new GoldCardRelic(), RelicType.SHARED);
        BaseMod.addRelic(new SupporterExpressRelic(), RelicType.SHARED);


        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        // (the others are all starters so they're marked as seen in the character file)
        //UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        pathCheck();
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        
        logger.info("Adding cards");
        //TODO: Rename the "DefaultMod" with the modid in your ModTheSpire.json file
        //TODO: The artifact mentioned in ModTheSpire.json is the artifactId in pom.xml you should've edited earlier
        new AutoAdd("BlueArchive_Hoshino") // ${project.artifactId}
            .packageFilter("BlueArchive_Hoshino.cards") // filters to any class in the same package as AbstractDefaultCard, nested packages included
            .setDefaultSeen(true)
            .cards();

        new AutoAdd("BlueArchive_Hoshino") // ${project.artifactId}
                .packageFilter("BlueArchive_Aris.cards") // filters to any class in the same package as AbstractDefaultCard, nested packages included
                .setDefaultSeen(true)
                .cards();


        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        String pathByLanguage, pathByLanguageAris;
        switch(Settings.language) {
            case KOR:
                pathByLanguage = getModID() + "Resources/localization/" + "kor/";
                pathByLanguageAris = getArisModID() + "Resources/localization/" + "kor/";
                break;
            case ZHS:
                pathByLanguage = getModID() + "Resources/localization/" + "zhs/";
                pathByLanguageAris = getArisModID() + "Resources/localization/" + "zhs/";
                break;
            default:
                pathByLanguage = getModID() + "Resources/localization/" + "eng/";
                pathByLanguageAris = getArisModID() + "Resources/localization/" + "eng/";
        }



        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                pathByLanguage + "Hoshino-Card-Strings.json");
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                pathByLanguageAris + "Aris-Card-Strings.json");

        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                pathByLanguage + "Hoshino-Power-Strings.json");
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                pathByLanguageAris + "Aris-Power-Strings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                pathByLanguage + "Hoshino-Relic-Strings.json");
        // CardStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                pathByLanguageAris + "Aris-Relic-Strings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                pathByLanguage + "Hoshino-Event-Strings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class,
                pathByLanguageAris + "Aris-Event-Strings.json");
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                pathByLanguage + "Hoshino-Potion-Strings.json");
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                pathByLanguageAris + "Aris-Potion-Strings.json");
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                pathByLanguage + "Hoshino-Character-Strings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                pathByLanguageAris + "Aris-Character-Strings.json");
        
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                pathByLanguage + "Hoshino-Orb-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                pathByLanguage + "Hoshino-UI-Strings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class,
                pathByLanguageAris + "Aris-UI-Strings.json");

        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                pathByLanguage + "Hoshino-Tutorial-Strings.json");

        BaseMod.loadCustomStringsFile(MonsterStrings.class,
                pathByLanguage + "Hoshino-Monster-Strings.json");

        logger.info("Done edittting strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        Gson gson = new Gson();
        String pathByLanguage, pathByLanguageAris;
        switch (Settings.language) {
            case KOR:
                pathByLanguage = getModID() + "Resources/localization/" + "kor/";
                pathByLanguageAris = getArisModID() + "Resources/localization/" + "kor/";
                break;
            case ZHS:
                pathByLanguage = getModID() + "Resources/localization/" + "zhs/";
                pathByLanguageAris = getArisModID() + "Resources/localization/" + "zhs/";
                break;
            default:
                pathByLanguage = getModID() + "Resources/localization/" + "eng/";
                pathByLanguageAris = getArisModID() + "Resources/localization/" + "eng/";
        }

        {
            String json = Gdx.files.internal(pathByLanguage + "Hoshino-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
            com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

            if (keywords != null) {
                for (Keyword keyword : keywords) {
                    BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                    //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
                }
            }
        }
        {
            String json = Gdx.files.internal(pathByLanguageAris + "Aris-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
            com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

            if (keywords != null) {
                for (Keyword keyword : keywords) {
                    BaseMod.addKeyword(getArisModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    public static String makeArisID(String idText) {
        return getArisModID() + ":" + idText;
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("BlueArchive_Hoshino:Reload", getModID() + "Resources/sound/reload.mp3");
        BaseMod.addAudio("BlueArchive_Hoshino:Atuiyo", getModID() + "Resources/sound/atuiyo.mp3");
        BaseMod.addAudio("BlueArchive_Hoshino:Fire", getModID() + "Resources/sound/shotgunfire.mp3");
        BaseMod.addAudio("BlueArchive_Hoshino:FireLight", getModID() + "Resources/sound/shotgunfirelight.mp3");
        BaseMod.addAudio("BlueArchive_Hoshino:FireHeavy", getModID() + "Resources/sound/shotgunfireheavy.mp3");

        BaseMod.addAudio("BlueArchive_Aris:ArisLight", getArisModID() + "Resources/sound/Aris_light.mp3");
    }
}
