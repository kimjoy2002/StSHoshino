package BlueArchive_Aris.relics;

import BlueArchive_Aris.powers.EndTurnBlockPower;
import BlueArchive_Aris.powers.ShockPower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Hoshino.DefaultMod.makeArisRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeArisRelicPath;

public class GameManual extends CustomRelic {

    public static int AMOUNT = 3;
    // ID, images, text.
    public static final String ID = DefaultMod.makeArisID("GameManual");

    private static final Texture IMG = TextureLoader.getTexture(makeArisRelicPath("GameManual.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeArisRelicOutlinePath("GameManual.png"));

    public GameManual() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    public void atTurnStart() {
        int afterBlock = AbstractDungeon.player.currentBlock / 2;
        if(afterBlock > 0) {
            AbstractDungeon.player.loseBlock(afterBlock);
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EndTurnBlockPower(AbstractDungeon.player,  afterBlock),  afterBlock));
            flash();
        }
    }

}
