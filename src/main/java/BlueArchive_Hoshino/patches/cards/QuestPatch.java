package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Aris.cards.QuestCard;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.SingingBowlButton;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

import static BlueArchive_Aris.cards.QuestCard.hasCardSkipQuest;
import static java.lang.Math.min;

public class QuestPatch {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    @SpirePatch(
            clz = SoulGroup.class,
            method = "obtain",
            paramtypez= {
                    AbstractCard.class,
                    boolean.class
            }
    )
    public static class obtainPatcher {
        public static void Prefix(SoulGroup __instance, AbstractCard card, boolean obtainCard) {
            if(card instanceof QuestCard && obtainCard) {
                ((QuestCard) card).questInit();
            }
            if(obtainCard) {
                QuestCard.questCheck(QuestCard.QuestProcess.GAIN_CARD);
            }
        }

    }
    @SpirePatch(
            clz = CardLibrary.class,
            method = "getCopy",
            paramtypez = {
                    String.class,
                    int.class,
                    int.class
            }
    )
    public static class getCopyPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"retVal"}
        )
        public static void Insert(String key, int upgradeTime, int misc, AbstractCard retVal) {
            if(retVal instanceof QuestCard) {
                ((QuestCard) retVal).makeDescription();
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "cardID");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

    }
    @SpirePatch(
            clz = PotionPopUp.class,
            method = "updateInput"
    )
    public static class discardPotionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(PotionPopUp __instance) {
            //출력 키워드가 있는지 확인
            Hitbox hbTop = (Hitbox) ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "hbTop");
            Hitbox hbBot = (Hitbox) ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "hbBot");
            if(!hbTop.hovered && hbBot.hovered) {
                QuestCard.questCheck(QuestCard.QuestProcess.POTION_DROP);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "gainGold"
    )
    public static class gainGoldPatcher {
        public static void Postfix(AbstractPlayer __instance) {
            QuestCard.questCheck(QuestCard.QuestProcess.GAIN_GOLD);
        }

    }

    @SpirePatch(
            clz = MonsterRoomElite.class,
            method = "dropReward"
    )
    public static class eliteSlainPatcher {
        public static void Prefix(MonsterRoomElite __instance) {
            QuestCard.questCheck(QuestCard.QuestProcess.SLAIN_ELITE);
            QuestCard.questCheck(QuestCard.QuestProcess.FINISH_BATTLE);
        }

    }

    @SpirePatch(
            clz = MonsterRoom.class,
            method = "dropReward"
    )
    public static class monsterSlainPatcher {
        public static void Prefix(MonsterRoom __instance) {
            QuestCard.questCheck(QuestCard.QuestProcess.FINISH_BATTLE);
        }

    }

    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "reopen"
    )
    public static  class reopenPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(CardRewardScreen __instance)
        {
            if(hasCardSkipQuest()) {
                SkipCardButton skipButton = (SkipCardButton) ReflectionHacks.getPrivate(__instance, CardRewardScreen.class, "skipButton");
                SingingBowlButton bowlButton = (SingingBowlButton) ReflectionHacks.getPrivate(__instance, CardRewardScreen.class, "bowlButton");
                boolean skippable = (boolean)  ReflectionHacks.getPrivate(__instance, CardRewardScreen.class, "skippable");
                if (skippable) {
                    skipButton.show(true);
                } else {
                    skipButton.hide();
                }
                bowlButton.show(__instance.rItem);
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "unhoverHitboxes");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "open"
    )
    public static  class openPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(CardRewardScreen __instance) {
            if(hasCardSkipQuest()) {
                SkipCardButton skipButton = (SkipCardButton) ReflectionHacks.getPrivate(__instance, CardRewardScreen.class, "skipButton");
                SingingBowlButton bowlButton = (SingingBowlButton) ReflectionHacks.getPrivate(__instance, CardRewardScreen.class, "bowlButton");
                skipButton.show(true);
                bowlButton.show(__instance.rItem);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "unhoverHitboxes");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = SingingBowlButton.class,
            method = "onClick"
    )
    public static class skipCard2Patcher {
        public static SpireReturn Prefix(SingingBowlButton __instance) {
            RewardItem rItem = (RewardItem) ReflectionHacks.getPrivate(__instance, SingingBowlButton.class, "rItem");
            QuestCard.questCheck(QuestCard.QuestProcess.SKIP_CARD);
            if(AbstractDungeon.player.hasRelic("Singing Bowl")) {
                AbstractDungeon.player.getRelic("Singing Bowl").flash();
                CardCrawlGame.sound.playA("SINGING_BOWL", MathUtils.random(-0.2F, 0.1F));
                AbstractDungeon.player.increaseMaxHp(2, true);
            }
            AbstractDungeon.combatRewardScreen.rewards.remove(rItem);
            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz = SingingBowlButton.class,
            method = "render",
            paramtypez = {
                    SpriteBatch.class
            }
    )
    public static class bowlRenderPatcher {
        public static SpireReturn Prefix(SingingBowlButton __instance, SpriteBatch sb) {
            if(!AbstractDungeon.player.hasRelic("Singing Bowl")) {
                boolean isHidden = (boolean) ReflectionHacks.getPrivate(__instance, SingingBowlButton.class, "isHidden");
                float current_x = (float) ReflectionHacks.getPrivate(__instance, SingingBowlButton.class, "current_x");
                Color textColor = (Color) ReflectionHacks.getPrivate(__instance, SingingBowlButton.class, "textColor");
                ReflectionHacks.RMethod renderButton = ReflectionHacks.privateMethod(SingingBowlButton.class, "renderButton", SpriteBatch.class);

                if (!isHidden) {
                    renderButton.invoke(__instance, sb);
                    FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, TEXT[0], current_x, SkipCardButton.TAKE_Y, textColor);
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "nextRoomTransition",
            paramtypez = {
                    SaveFile.class
            }
    )
    public static class nextRoomTransitionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SaveFile saveFile) {
            QuestCard.questCheck(QuestCard.QuestProcess.PROGRESS_FLOOR);
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(Random.class);
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Aris:SkipCardUI");
        TEXT = uiStrings.TEXT;
    }
}
