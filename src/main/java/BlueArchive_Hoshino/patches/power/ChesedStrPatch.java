package BlueArchive_Hoshino.patches.power;

import BlueArchive_Hoshino.monsters.act3.boss.*;
import BlueArchive_Hoshino.patches.ReloadButtonPatch;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;

import java.util.Iterator;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "calculateDamage",
        paramtypez= {
                int.class
        }
)
public class ChesedStrPatch {
    public static SpireReturn Prefix(AbstractMonster __instance, int dmg) {
        if(__instance instanceof Soldier || __instance instanceof Drone || __instance instanceof Turret || __instance instanceof Goliat) {


            Chesed chesed = Chesed.getChesed();
            if(chesed != null) {
                if (chesed.getPower("BlueArchive_Hoshino:ChesedStrPower") != null) {
                    AbstractPlayer target = AbstractDungeon.player;
                    float tmp = (float)dmg;
                    if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                        float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                        tmp *= mod;
                    }
                    AbstractPower chesedPower = chesed.getPower("BlueArchive_Hoshino:ChesedStrPower");

                    tmp += chesedPower.amount;


                    AbstractPower p;
                    Iterator var6;
                    for(var6 = __instance.powers.iterator(); var6.hasNext(); tmp = p.atDamageGive(tmp, com.megacrit.cardcrawl.cards.DamageInfo.DamageType.NORMAL)) {
                        p = (AbstractPower)var6.next();
                    }

                    for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageReceive(tmp, com.megacrit.cardcrawl.cards.DamageInfo.DamageType.NORMAL)) {
                        p = (AbstractPower)var6.next();
                    }

                    tmp = AbstractDungeon.player.stance.atDamageReceive(tmp, com.megacrit.cardcrawl.cards.DamageInfo.DamageType.NORMAL);
                    ReflectionHacks.RMethod method = ReflectionHacks.privateMethod(AbstractMonster.class, "applyBackAttack");
                    if (method.invoke(__instance)) {
                        tmp = (float)((int)(tmp * 1.5F));
                    }

                    for(var6 = __instance.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalGive(tmp, com.megacrit.cardcrawl.cards.DamageInfo.DamageType.NORMAL)) {
                        p = (AbstractPower)var6.next();
                    }

                    for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalReceive(tmp, com.megacrit.cardcrawl.cards.DamageInfo.DamageType.NORMAL)) {
                        p = (AbstractPower)var6.next();
                    }

                    dmg = MathUtils.floor(tmp);
                    if (dmg < 0) {
                        dmg = 0;
                    }
                    ReflectionHacks.setPrivate(__instance, AbstractMonster.class, "intentDmg",dmg);
                    return SpireReturn.Return();
                }
            }
        }

        return SpireReturn.Continue();
    }
}
