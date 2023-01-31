package BlueArchive_Hoshino;

import BlueArchive_Hoshino.events.*;
import BlueArchive_Hoshino.potions.BulletPotion;
import BlueArchive_Hoshino.potions.SkilledPotion;
import BlueArchive_Hoshino.relics.*;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import basemod.*;
import basemod.eventUtil.AddEventParams;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
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
import java.util.Properties;

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

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Default Mod";
    private static final String AUTHOR = "joy1999"; // And pretty soon - You!
    private static final String DESCRIPTION = "HOSHINO TEST.";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    public static final Color DEFAULT_PINK = CardHelper.getColor(255.0f, 208.0f, 220.0f);
    
    // Potion Colors in RGB
    public static final Color SKILLED_POTION_LIQUID = CardHelper.getColor(53.0f, 203.0f, 18.0f);
    public static final Color SKILLED_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f);
    public static final Color SKILLED_POTION_SPOTS = CardHelper.getColor(53.0f, 203.0f, 18.0f);

    public static final Color BULLET_POTION_LIQUID = CardHelper.getColor(203.0f, 53.0f, 18.0f);
    public static final Color BULLET_POTION_HYBRID = CardHelper.getColor(30.0f, 30.0f, 30.0f);
    public static final Color BULLET_POTION_SPOTS = CardHelper.getColor(166.0f, 88.0f, 2.0f);


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
    
    // Character assets
    private static final String THE_DEFAULT_BUTTON = "BlueArchive_HoshinoResources/images/charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "BlueArchive_HoshinoResources/images/charSelect/HoshinoPortrait.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "BlueArchive_HoshinoResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "BlueArchive_HoshinoResources/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "BlueArchive_HoshinoResources/images/char/defaultCharacter/corpse.png";
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "BlueArchive_HoshinoResources/images/Badge.png";
    
    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = "BlueArchive_HoshinoResources/images/char/defaultCharacter/Hoshino.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "BlueArchive_HoshinoResources/images/char/defaultCharacter/Hoshino.json";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
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
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
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
        
        receiveEditPotions();
        logger.info("Added " + Hoshino.Enums.HOSHINO.toString());
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
        
        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.
        
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
        {
            AddEventParams eventParams = new AddEventParams.Builder(GachaEvent.ID, GachaEvent.class)
                    .dungeonID(TheBeyond.ID)
                    .create();
            BaseMod.addEvent(eventParams);
        }


        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        BaseMod.addPotion(SkilledPotion.class, SKILLED_POTION_LIQUID, SKILLED_POTION_HYBRID, SKILLED_POTION_SPOTS, SkilledPotion.POTION_ID, Hoshino.Enums.HOSHINO);
        BaseMod.addPotion(BulletPotion.class, BULLET_POTION_LIQUID, BULLET_POTION_HYBRID, BULLET_POTION_SPOTS, BulletPotion.POTION_ID, Hoshino.Enums.HOSHINO);





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

        // This adds a relic to the Shared pool. Every character can find this relic.BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);
        BaseMod.addRelic(new IOURelic(), RelicType.SHARED);
        BaseMod.addRelic(new ShirokoRelic(), RelicType.SHARED);
        BaseMod.addRelic(new NonomiRelic(), RelicType.SHARED);
        BaseMod.addRelic(new SerikaRelic(), RelicType.SHARED);
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
        
        logger.info("Adding cards");
        //TODO: Rename the "DefaultMod" with the modid in your ModTheSpire.json file
        //TODO: The artifact mentioned in ModTheSpire.json is the artifactId in pom.xml you should've edited earlier
        new AutoAdd("BlueArchive_Hoshino") // ${project.artifactId}
            .packageFilter("BlueArchive_Hoshino.cards") // filters to any class in the same package as AbstractDefaultCard, nested packages included
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

        String pathByLanguage;
        switch(Settings.language) {
            case KOR:
                pathByLanguage = getModID() + "Resources/localization/" + "kor/";
                break;
            default:
                pathByLanguage = getModID() + "Resources/localization/" + "eng/";
        }

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                pathByLanguage + "Hoshino-Card-Strings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                pathByLanguage + "Hoshino-Power-Strings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                pathByLanguage + "Hoshino-Relic-Strings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                pathByLanguage + "Hoshino-Event-Strings.json");
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                pathByLanguage + "Hoshino-Potion-Strings.json");
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                pathByLanguage + "Hoshino-Character-Strings.json");
        
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                pathByLanguage + "Hoshino-Orb-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                pathByLanguage + "Hoshino-UI-Strings.json");

        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                pathByLanguage + "Hoshino-Tutorial-Strings.json");

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
        String pathByLanguage;
        switch(Settings.language) {
            case KOR:
                pathByLanguage = getModID() + "Resources/localization/" + "kor/";
                break;
            default:
                pathByLanguage = getModID() + "Resources/localization/" + "eng/";
        }

        String json = Gdx.files.internal(pathByLanguage + "Hoshino-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("BlueArchive_Hoshino:Reload", getModID() + "Resources/sound/reload.mp3");
        BaseMod.addAudio("BlueArchive_Hoshino:Atuiyo", getModID() + "Resources/sound/atuiyo.mp3");
        BaseMod.addAudio("BlueArchive_Hoshino:Fire", getModID() + "Resources/sound/shotgunfire.mp3");
        BaseMod.addAudio("BlueArchive_Hoshino:FireLight", getModID() + "Resources/sound/shotgunfirelight.mp3");
        BaseMod.addAudio("BlueArchive_Hoshino:FireHeavy", getModID() + "Resources/sound/shotgunfireheavy.mp3");
    }
}
