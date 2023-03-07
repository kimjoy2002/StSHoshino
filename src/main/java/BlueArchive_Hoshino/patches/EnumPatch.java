package BlueArchive_Hoshino.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class EnumPatch {
    @SpireEnum
    public static AbstractGameAction.AttackEffect HOSHINO_SHOTGUN;
    @SpireEnum
    public static AbstractGameAction.AttackEffect HOSHINO_SHOTGUN_LIGHT;

    @SpireEnum
    public static AbstractGameAction.AttackEffect HOSHINO_SHOTGUN_HEAVY;

    @SpireEnum
    public static AbstractCard.CardTags GOZ_BOMB;


    public EnumPatch() {
    }
}
