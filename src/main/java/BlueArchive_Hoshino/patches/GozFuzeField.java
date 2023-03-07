package BlueArchive_Hoshino.patches;


import BlueArchive_Hoshino.effects.GozeFuzeParticalEffect;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

@SpirePatch(
        clz= AbstractCard.class,
        method=SpirePatch.CLASS
)
public class GozFuzeField {
    public static SpireField<ArrayList<GozeFuzeParticalEffect>> fuzeList = new SpireField<>(() -> new ArrayList());
    public static SpireField<Float> fuzeTimer = new SpireField<Float>(() -> 0.0F);
    public static SpireField<Color> fuzeColor = new SpireField<>(() -> new Color(1.0F, 1.0F, 1.0F, 1.0F));
}
