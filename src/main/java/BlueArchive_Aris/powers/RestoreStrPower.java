package BlueArchive_Aris.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_Hoshino.DefaultMod.makeArisPowerPath;

public class RestoreStrPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeArisID("RestoreStrPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture tex84 = TextureLoader.getTexture(makeArisPowerPath("RestoreStrPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makeArisPowerPath("RestoreStrPower32.png"));


    private static int afterPowerIdOffset;
    private int ramain_turn;
    public RestoreStrPower(AbstractCreature owner, int armorAmt, int turn) {
        name = NAME;
        ID = POWER_ID + afterPowerIdOffset;
        ++afterPowerIdOffset;
        this.owner = owner;
        this.amount = armorAmt;
        this.ramain_turn = turn;
        type = PowerType.BUFF;
        isTurnBased = false;
        this.updateDescription();
        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_SHACKLE", 0.05F);
    }

    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
        if(--ramain_turn<=0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        }
        updateDescription();
    }


    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + ramain_turn + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }
}
