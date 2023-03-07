package BlueArchive_Hoshino.patches.power;

import BlueArchive_Hoshino.cards.ShuffleCard;
import BlueArchive_Hoshino.monsters.act3.boss.Hieronymus;
import BlueArchive_Hoshino.monsters.act3.boss.RedRelic;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import javassist.CtBehavior;

import java.util.Iterator;

public class RedRelicPatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "draw",
            paramtypez= {
                    int.class
            }
    )
    public static class RedRelicPowerPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"c"}
        )
        public static void Insert(AbstractPlayer __instance, int numCards, AbstractCard c) {
            if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null  &&
                    AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
            {
                Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

                while(var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster) var1.next();
                    if (m instanceof RedRelic) {
                        if (m.getPower("BlueArchive_Hoshino:RedRelicPower") != null) {
                            AbstractPower redPower = m.getPower("BlueArchive_Hoshino:RedRelicPower");
                            redPower.onCardDraw(c);
                        }
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "removeTopCard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
