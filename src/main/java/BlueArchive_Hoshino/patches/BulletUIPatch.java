package BlueArchive_Hoshino.patches;

import BlueArchive_Hoshino.ui.BulletUI;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel;
import javassist.CtBehavior;

public class BulletUIPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "<class>"
    )
    public static class BulletFields {
        public static SpireField<BulletUI> panel = new SpireField(BulletUI::new);

        public BulletFields() {
        }
        public static BulletUI getBulletUI() {
            return (BulletUI) panel.get(AbstractDungeon.player);
        }

        public static boolean shouldRenderBullet() {
            return CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
        }
    }

    @SpirePatch(
            clz = OverlayMenu.class,
            method = "update"
    )
    public static class PanelUpdate {
        public PanelUpdate() {
        }

        @SpireInsertPatch(
                locator = BulletUIPatch.PanelUpdate.Locator.class
        )
        public static void patch(OverlayMenu __instance) {
            BulletFields.getBulletUI().update();
        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(OverlayMenu.class, "updateBlackScreen");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = OverlayMenu.class,
            method = "render"
    )
    public static class PanelRender {
        public PanelRender() {
        }

        @SpireInsertPatch(
                locator = BulletUIPatch.PanelRender.Locator.class
        )
        public static void patch(OverlayMenu __instance, SpriteBatch sb) {
            BulletFields.getBulletUI().render(sb);
        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ExhaustPanel.class, "render");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = OverlayMenu.class,
            method = "showCombatPanels"
    )
    public static class ShowPanel {
        public ShowPanel() {
        }

        @SpirePrefixPatch
        public static void patch(OverlayMenu __instance) {
            BulletFields.getBulletUI().show();
        }
    }

    @SpirePatch(
            clz = OverlayMenu.class,
            method = "hideCombatPanels"
    )
    public static class HidePanel {
        public HidePanel() {
        }

        @SpirePrefixPatch
        public static void patch(OverlayMenu __instance) {
            BulletFields.getBulletUI().hide();
        }
    }
}
