package BlueArchive_Aris.relics;

import BlueArchive_Aris.cards.QuestCard;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.makeArisRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeArisRelicPath;

public class TASRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeArisID("TASRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeArisRelicPath("TAS.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeArisRelicOutlinePath("TAS.png"));

    public TASRelic() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    public void onEquip() {
        QuestCard.questCheck(QuestCard.QuestProcess.TAS);
    }

}
