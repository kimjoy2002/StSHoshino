package BlueArchive_Hoshino.patches.relics;

import BlueArchive_Aris.cards.CustomGameCard;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Aris.relics.CopyCat;
import BlueArchive_Hoshino.patches.cards.CustomGameCardPatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CopyCatPatch {
    public static AbstractCard getCopyCatColorCard( AbstractCard.CardColor color, AbstractCard.CardRarity rarity) {
        CardGroup anyCard = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator var2 = CardLibrary.cards.entrySet().iterator();
        AbstractCard.CardColor player_ = AbstractDungeon.player.getCardColor();

        while(true) {
            Map.Entry c;
            do {
                do {
                    do {
                        do {
                            do {
                                if (!var2.hasNext()) {
                                    anyCard.shuffle(AbstractDungeon.cardRng);
                                    return anyCard.getRandomCard(true, rarity).makeCopy();
                                }

                                c = (Map.Entry)var2.next();
                            } while(((AbstractCard)c.getValue()).rarity != rarity);
                        } while(((AbstractCard)c.getValue()).type == AbstractCard.CardType.CURSE);
                    } while(((AbstractCard)c.getValue()).color != color && ((AbstractCard)c.getValue()).color != player_);
                } while(((AbstractCard)c.getValue()).type == AbstractCard.CardType.STATUS);
            } while(UnlockTracker.isCardLocked((String)c.getKey()) && !Settings.treatEverythingAsUnlocked());

            anyCard.addToBottom((AbstractCard)c.getValue());
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static class getCopyPatcher {


        @SpireInsertPatch(
                loc=1837,
                localvars={"card", "rarity"}
        )
        public static void Insert(@ByRef AbstractCard[] card, AbstractCard.CardRarity rarity) {
            if(card != null) {
                if (AbstractDungeon.player.hasRelic(CopyCat.ID)) {
                    AbstractCard.CardColor color_ = ((CopyCat)AbstractDungeon.player.getRelic(CopyCat.ID)).CopyCatColor;
                    if (color_ !=  Aris.Enums.COLOR_BLUE) {
                        card[0] = getCopyCatColorCard(color_, rarity);
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCard");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }

    }
}
