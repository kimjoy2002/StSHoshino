package BlueArchive_Hoshino.patches.power;

import BlueArchive_Aris.cards.OutputCard;
import BlueArchive_Aris.powers.FreeCardPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

import static java.lang.Math.min;

public class FreeCardPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "freeToPlay"
    )
    public static class freeToPlayPatcher {
        public static SpireReturn Prefix(AbstractCard __instance) {
            if ( AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.hasPower(FreeCardPower.POWER_ID)) {
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }
}
