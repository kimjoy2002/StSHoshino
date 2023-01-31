package BlueArchive_Hoshino.relics;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.NonomiRelicAction;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeRelicPath;

public class NonomiRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     */
    private boolean firstTurn = true;

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("NonomiRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("nonomi_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("nonomi_relic.png"));

    public NonomiRelic() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    public void atTurnStartPostDraw() {
        if (this.firstTurn) {
            AbstractDungeon.actionManager.addToBottom(new NonomiRelicAction());
            this.firstTurn = false;
            this.grayscale = true;
        }
    }

    public void atPreBattle() {
        this.firstTurn = true;
    }
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

}
