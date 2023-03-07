package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Hoshino.cards.GozBomb;
import BlueArchive_Hoshino.effects.GozeFuzeParticalEffect;
import BlueArchive_Hoshino.patches.EnumPatch;
import BlueArchive_Hoshino.patches.GozFuzeField;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import com.megacrit.cardcrawl.vfx.scene.*;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hoshino.patches.EnumPatch.GOZ_BOMB;
import static com.badlogic.gdx.math.MathUtils.*;

public class GozBombPatch {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static String tempString = "";
    private static Texture goz_fuse = new Texture("BlueArchive_HoshinoResources/images/512/goz_fuse.png");


    @SpirePatch(
            clz = AbstractCard.class,
            method = "initializeDescription"
    )
    public static class gozBombPrePatcher {

        public static void Prefix(AbstractCard __instance) {
            if (__instance.hasTag(EnumPatch.GOZ_BOMB)) {
                if(tempString.isEmpty()) {
                    tempString = __instance.rawDescription;
                    __instance.rawDescription += TEXT[0];
                }
            }

        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "initializeDescription"
    )
    public static class gozBombPostPatcher {

        public static void Postfix(AbstractCard __instance) {
            if (__instance.hasTag(EnumPatch.GOZ_BOMB)) {
                if(!tempString.isEmpty()) {
                    __instance.rawDescription = tempString;
                    tempString = "";
                }
            }
        }
    }







    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCard",
            paramtypez = {SpriteBatch.class, boolean.class, boolean.class}
    )
    public static class gozBombRenderPatcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected) {
            if (__instance.hasTag(GOZ_BOMB) && /*!__instance.darken && */!__instance.isLocked && __instance.isSeen) {
                Color fuzeColor = GozFuzeField.fuzeColor.get(__instance);
                sb.setColor(fuzeColor);
                float w = goz_fuse.getWidth();
                float h = goz_fuse.getHeight();
                sb.draw(goz_fuse, __instance.current_x-256, __instance.current_y-256, w/2.0f, h/2.0f, w, h, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, (int)w, (int)h, false, false);

                //sb.setBlendFunction(770, 1);


                /*Color costColor = Color.WHITE.cpy();
                if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(this) && !this.hasEnoughEnergy()) {
                    costColor = ENERGY_COST_RESTRICTED_COLOR;
                } else if (this.isCostModified || this.isCostModifiedForTurn || this.freeToPlay()) {
                    costColor = ENERGY_COST_MODIFIED_COLOR;
                }

                costColor.a = this.transparency;
                String text = this.getCost();
                BitmapFont font = this.getEnergyFont();
                if ((this.type != AbstractCard.CardType.STATUS || this.cardID.equals("Slimed")) && (this.color != AbstractCard.CardColor.CURSE || this.cardID.equals("Pride"))) {
                    FontHelper.renderRotatedText(sb, font, text, this.current_x, this.current_y, -132.0F * this.drawScale * Settings.scale, 192.0F * this.drawScale * Settings.scale, this.angle, false, costColor);
                }*/

            }
            ArrayList<GozeFuzeParticalEffect> fuzeList = GozFuzeField.fuzeList.get(__instance);

            Iterator var2 = fuzeList.iterator();

            while(var2.hasNext()) {
                AbstractGameEffect e = (AbstractGameEffect)var2.next();
                e.render(sb);
            }

        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "renderEnergy");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }



    @SpirePatch(
            clz = AbstractCard.class,
            method = "update"
    )
    public static class gozBombUpdatePatcher {
        static float particleTimer1 = 0.1f;

        public static void Postfix(AbstractCard __instance)
        {
            ArrayList<GozeFuzeParticalEffect> fuzeList = GozFuzeField.fuzeList.get(__instance);
            if (__instance.hasTag(GOZ_BOMB) && /*!__instance.darken && */!__instance.isLocked && __instance.isSeen) {
                Float fuzeTimer = GozFuzeField.fuzeTimer.get(__instance);

                //angle고려하기
                float angle = __instance.angle;
                float angle_x = 110 * cosDeg(angle) - 180 * sinDeg(angle);
                float angle_y = 110 * sinDeg(angle) + 180 * cosDeg(angle);
                float x = __instance.current_x+angle_x*__instance.drawScale * Settings.scale;
                float y = __instance.current_y+angle_y*__instance.drawScale * Settings.scale;

                fuzeTimer -= Gdx.graphics.getDeltaTime();
                GozFuzeField.fuzeTimer.set(__instance, fuzeTimer);
                if (fuzeTimer < 0.0F) {
                    fuzeList.add(new GozeFuzeParticalEffect(x,y));
                    fuzeList.add(new GozeFuzeParticalEffect(x,y));
                    GozFuzeField.fuzeTimer.set(__instance, 0.05F);
                }


                /*__instance.newGlowTimer -= Gdx.graphics.getDeltaTime();
                if (__instance.newGlowTimer < 0.0F) {
                    __instance.newGlowTimer = 0.1F;
                    TorchParticleMEffect e1 = new TorchParticleMEffect();
                    e1.renderBehind = false;
                    AbstractDungeon.topLevelEffects.add(e1);
                    LightFlareMEffect e2 = new LightFlareMEffect(__instance.current_x+186.0f*__instance.drawScale * Settings.scale, __instance.current_y+250.0f*__instance.drawScale * Settings.scale);
                    e2.renderBehind = false;
                    AbstractDungeon.topLevelEffects.add(e2);
                    AbstractDungeon.topLevelEffects.add(e2);
                    //이러면 UI다 씹어버리니 그냥 평범하게 render에서 하자
                }*/
            }
            Iterator<GozeFuzeParticalEffect> i = fuzeList.iterator();

            while(i.hasNext()) {
                GozeFuzeParticalEffect e = (GozeFuzeParticalEffect)i.next();
                e.update();
                if (e.isDone) {
                    i.remove();
                }
            }

        }
    }

    @SpirePatch(
            clz = Soul.class,
            method = "isCarryingCard"
    )
    public static class soulEffectPatcher {

        public static SpireReturn Prefix(Soul __instance)
        {
            if(__instance.card != null && __instance.card instanceof GozBomb) {
                return SpireReturn.Return((boolean) true);
            }
            return SpireReturn.Continue();
        }

    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCardTip"
    )
    public static class renderGozBombPatcher {
        static AbstractCard temp = null;
        static AbstractCard gozbomb = new GozBomb();
        static  boolean useTemp = false;
        public static void Prefix(AbstractCard __instance)
        {
            if(__instance.hasTag(GOZ_BOMB)) {
                temp = __instance.cardsToPreview;
                __instance.cardsToPreview = gozbomb;
                useTemp  = true;
            }
            if(__instance instanceof GozBomb && !__instance.keywords.contains("bluearchive_hoshino:GOZBOMB")) {
                __instance.keywords.add("bluearchive_hoshino:GOZBOMB");
            }
        }
        public static void Postfix(AbstractCard __instance)
        {
            if(useTemp) {
                __instance.cardsToPreview = temp;
                temp = null;
                useTemp = false;
            }

        }

    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard",
            paramtypez = {AbstractCard.class, AbstractMonster.class, int.class}
    )
    public static class gozBombUsePatcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if (c.hasTag(GOZ_BOMB) ) {
                AbstractCard card = new GozBomb();
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, true));
                c.tags.remove(GOZ_BOMB);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }




    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:GozBombText");
        TEXT = uiStrings.TEXT;
    }
}
