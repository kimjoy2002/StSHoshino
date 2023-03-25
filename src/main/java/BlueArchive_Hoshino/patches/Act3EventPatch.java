package BlueArchive_Hoshino.patches;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.patches.power.HodGloryPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static BlueArchive_Aris.events.SaibaMomoiEvent.hasCustomGameCard;

@SpirePatch(
        clz = EventRoom.class,
        method = "onPlayerEntry"
)
public class Act3EventPatch {

    private static final Logger logger = LogManager.getLogger(Act3EventPatch.class.getName());
    private static String peroro = "BlueArchive_Hoshino:PeroroEvent";
    private static String momoi =  "BlueArchive_Aris:SaibaMomoiEvent";
    public static SpireReturn Prefix(EventRoom __instance) {
        if( AbstractDungeon.eventList.contains(momoi) && hasCustomGameCard()) {
            if(AbstractDungeon.floorNum > 25 || AbstractDungeon.miscRng.randomBoolean(0.3F)){
                AbstractDungeon.overlayMenu.proceedButton.hide();
                AbstractDungeon.eventList.remove(momoi);
                __instance.event = EventHelper.getEvent(momoi);
                if(__instance.event == null) {
                    return SpireReturn.Continue();
                }
                logger.info("Removed event: " + momoi + " from pool.");
                __instance.event.onEnterRoom();
                return SpireReturn.Return();
            }
        }

        if(DefaultMod.enableAct3Event && AbstractDungeon.eventList.contains(peroro)){
            AbstractDungeon.overlayMenu.proceedButton.hide();
            AbstractDungeon.eventList.remove(peroro);
            __instance.event = EventHelper.getEvent(peroro);
            if(__instance.event == null) {
                return SpireReturn.Continue();
            }
            logger.info("Removed event: " + peroro + " from pool.");
            __instance.event.onEnterRoom();
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}
