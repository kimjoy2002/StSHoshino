package BlueArchive_Aris.relics;

import BlueArchive_Aris.powers.ShockPower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.makeArisRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeArisRelicPath;

public class Battery extends CustomRelic {

    public static int AMOUNT = 3;
    // ID, images, text.
    public static final String ID = DefaultMod.makeArisID("Battery");

    private static final Texture IMG = TextureLoader.getTexture(makeArisRelicPath("Battery.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeArisRelicOutlinePath("Battery.png"));

    public Battery() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }


    public void onTrigger(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new ShockPower(target, AbstractDungeon.player, AMOUNT)));
    }
}
