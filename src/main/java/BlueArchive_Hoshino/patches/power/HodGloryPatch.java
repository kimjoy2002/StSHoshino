package BlueArchive_Hoshino.patches.power;

import BlueArchive_Hoshino.cards.ShuffleCard;
import BlueArchive_Hoshino.powers.FreeReloadPower;
import BlueArchive_Hoshino.relics.ShirokoRelic;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;


public class HodGloryPatch {
    private static final Logger logger = LogManager.getLogger(HodGloryPatch.class.getName());


    @SpirePatch(
            clz = AbstractPower.class,
            method = "onRemove"
    )
    public static class onModifyPowerPatcher {
        public static void Postfix(AbstractPower __instance)
        {
            if(__instance instanceof ArtifactPower) {
                if(__instance.owner != null && __instance.owner.hasPower("BlueArchive_Hoshino:HodGloryPower")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance.owner, __instance.owner, new VulnerablePower(__instance.owner, 3, true), 3));
                }
            }
        }
    }



}
