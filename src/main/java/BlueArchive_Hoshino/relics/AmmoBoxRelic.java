package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.MilitarySupplies;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class AmmoBoxRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    private boolean firstTurn = true;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("AmmoBoxRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ammo_box_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ammo_box_relic.png"));

    public AmmoBoxRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }
    public void atPreBattle() {
        this.firstTurn = true;
    }

    public void atTurnStart() {
        if (this.firstTurn) {
            this.flash();
            AbstractCard card = MilitarySupplies.returnRandomBulletCardInCombat();
            card.freeToPlayOnce = true;
            this.addToBot(new MakeTempCardInHandAction(card, true));
            this.firstTurn = false;
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
