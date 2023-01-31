package BlueArchive_Hoshino.patches;

import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.ui.ReloadButton;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;

import java.util.WeakHashMap;

public class ReloadButtonPatch {
    private static final WeakHashMap<EndTurnButton, Float> currentXMap;
    public static ReloadButton reloadButton;

    public ReloadButtonPatch() {
    }

    static {
        currentXMap = new WeakHashMap();
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "render",
            paramtypez = {SpriteBatch.class}
    )
    public static class RenderPatcher {
        public RenderPatcher() {
        }

        public static void Postfix(EndTurnButton __instance, SpriteBatch sb) {
            if(AbstractDungeon.player instanceof Hoshino) {
                ReloadButtonPatch.reloadButton.render(sb);
            }
        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "render",
            paramtypez = {SpriteBatch.class}
    )

    public static class RenderInsertPatcher2 {
        public RenderInsertPatcher2() {
        }

        @SpireInsertPatch(
                rloc = 157
        )
        public static void Insert(EndTurnButton __instance, SpriteBatch sb) {
            if(AbstractDungeon.player instanceof Hoshino) {
                if (ReloadButtonPatch.currentXMap.get(__instance) != null) {
                    ReflectionHacks.setPrivate(__instance, EndTurnButton.class, "current_x", ReloadButtonPatch.currentXMap.get(__instance));
                }
            }

        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "render",
            paramtypez = {SpriteBatch.class}
    )
    public static class RenderInsertPatcher1 {
        public RenderInsertPatcher1() {
        }

        @SpireInsertPatch(
                rloc = 149,
                localvars = {"tmpY"}
        )
        public static void Insert(EndTurnButton __instance, SpriteBatch sb, @ByRef float[] tmpY) {
            if(AbstractDungeon.player instanceof Hoshino) {
                float valueX = (Float) ReflectionHacks.getPrivate(__instance, EndTurnButton.class, "current_x");
                ReloadButtonPatch.currentXMap.put(__instance, valueX);
                //ReflectionHacks.setPrivate(__instance, EndTurnButton.class, "current_x", valueX + -7.0F * Settings.scale);
                tmpY[0] -= 1.5F * Settings.scale;
            }
        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "hide"
    )
    public static class HidePatcher {
        public HidePatcher() {
        }

        public static void Postfix(EndTurnButton __instance) {
            ReloadButtonPatch.reloadButton.hide();
        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "show"
    )
    public static class ShowPatcher {
        public ShowPatcher() {
        }

        public static void Postfix(EndTurnButton __instance) {
            ReloadButtonPatch.reloadButton.show();
        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "disable",
            paramtypez = {}
    )
    public static class DisablePatcher2 {
        public DisablePatcher2() {
        }

        public static void Postfix(EndTurnButton __instance) {
            ReloadButtonPatch.reloadButton.disable();
        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "disable",
            paramtypez = {boolean.class}
    )
    public static class DisablePatcher1 {
        public DisablePatcher1() {
        }

        public static void Postfix(EndTurnButton __instance, boolean isEnemyTurn) {
            ReloadButtonPatch.reloadButton.disable();
        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "enable"
    )
    public static class EnablePatcher {
        public EnablePatcher() {
        }

        public static void Postfix(EndTurnButton __instance) {
            ReloadButtonPatch.reloadButton.enable();
        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "update"
    )
    public static class UpdatePatcher {
        public UpdatePatcher() {
        }

        public static void Postfix(EndTurnButton __instance) {
            ReloadButtonPatch.reloadButton.update();
        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "<ctor>"
    )
    public static class ConstructorPatcher {
        public ConstructorPatcher() {
        }

        public static void Postfix(EndTurnButton __instance, @ByRef(type = "helpers.Hitbox") Object[] ___hb) {
            ReloadButtonPatch.reloadButton = new ReloadButton(__instance);
            ___hb[0] = new Hitbox(0.0F, 0.0F, 185.0F * Settings.scale, 100.0F * Settings.scale);

            if(AbstractDungeon.player instanceof Hoshino) {
                float valueY = (Float) ReflectionHacks.getPrivate(__instance, EndTurnButton.class, "current_y");
                ReflectionHacks.setPrivate(__instance, EndTurnButton.class, "current_y", valueY + -75.0F * Settings.scale);
            }
        }
    }
}
