package BlueArchive_Aris.relics;

import BlueArchive_Aris.actions.EquipsmithAction;
import BlueArchive_Aris.actions.StartingEquipmentAction;
import BlueArchive_Aris.powers.ChargePower;
import BlueArchive_Aris.powers.ShockPower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.makeArisRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeArisRelicPath;

public class StartingEquipment extends CustomRelic implements ClassChangeRelic {

    public static final int AMOUNT = 1;
    private static boolean usedThisCombat = false;
    // ID, images, text.
    public static final String ID = DefaultMod.makeArisID("StartingEquipment");

    private static final Texture IMG = TextureLoader.getTexture(makeArisRelicPath("StartingEquipment.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeArisRelicOutlinePath("StartingEquipment.png"));

    public StartingEquipment() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }

    public void atPreBattle() {
        usedThisCombat = false;
    }
    @Override
    public void onClassChange() {
        if(!usedThisCombat) {
            flash();
            this.addToBot(new StartingEquipmentAction(AMOUNT));
            this.grayscale = true;
            usedThisCombat = true;
        }
    }
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }
}
