package BlueArchive_Hoshino.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class EnumPatch {
    @SpireEnum
    public static AbstractGameAction.AttackEffect HOSHINO_SHOTGUN;
    @SpireEnum
    public static AbstractGameAction.AttackEffect HOSHINO_SHOTGUN_LIGHT;

    @SpireEnum
    public static AbstractGameAction.AttackEffect HOSHINO_SHOTGUN_HEAVY;


    public EnumPatch() {
    }
}
