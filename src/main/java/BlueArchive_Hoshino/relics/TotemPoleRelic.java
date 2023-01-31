package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.DrowsyCard;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class TotemPoleRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("TotemPoleRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("totem_pole_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("totem_pole_relic.png"));

    public TotemPoleRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.SOLID);
    }

    public void onEquip() {
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (DrowsyCard.isCurseDrowsyCard(c)) {
                c.misc = 2;
                c.initializeDescription();
            }
        }
    }

    public void onUnequip() {
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (DrowsyCard.isCurseDrowsyCard(c)) {
                c.misc = 0;
                c.initializeDescription();
            }
        }
    }


    public void onObtainCard(AbstractCard c) {
        if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
            c.misc = 2;
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
