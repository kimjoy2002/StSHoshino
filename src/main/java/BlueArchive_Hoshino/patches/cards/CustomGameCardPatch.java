package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Aris.cards.CustomGameCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import javassist.CtBehavior;

public class CustomGameCardPatch {

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
            if(retVal instanceof CustomGameCard) {
                ((CustomGameCard) retVal).initializeCustomCard();
                ((CustomGameCard) retVal).reloadImage();
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "cardID");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

    }
}
