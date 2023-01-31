package BlueArchive_Hoshino.patches.cards;

import BlueArchive_Hoshino.cards.QuickShotAttack;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

@SpirePatch(
        clz = UseCardAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez={
                AbstractCard.class,
                AbstractCreature.class
        }
)
public class QuickShotPatch {
    public static void Postfix(UseCardAction __instance, AbstractCard card, AbstractCreature creature)
    {
        if(card instanceof QuickShotAttack) {
            __instance.reboundCard = true;
        }
    }
}
