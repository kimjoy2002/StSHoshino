package BlueArchive_Aris.relics;

import BlueArchive_Aris.powers.EndTurnBlockPower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Hoshino.DefaultMod.makeArisRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeArisRelicPath;

public class CoveredKnifeSwitch extends CustomRelic implements OverloadRelic {

    public static int AMOUNT = 3;
    // ID, images, text.
    public static final String ID = DefaultMod.makeArisID("CoveredKnifeSwitch");

    private static final Texture IMG = TextureLoader.getTexture(makeArisRelicPath("CoveredKnifeSwitch.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeArisRelicOutlinePath("CoveredKnifeSwitch.png"));

    public CoveredKnifeSwitch() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }

    public void onOverload() {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, AMOUNT));
        flash();
    }
}
