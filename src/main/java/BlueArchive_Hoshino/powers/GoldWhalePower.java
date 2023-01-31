package BlueArchive_Hoshino.powers;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static BlueArchive_Hoshino.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class GoldWhalePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("GoldWhalePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("GoldWhalePower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("GoldWhalePower32.png"));

    public GoldWhalePower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    public void onVictory() {
        this.flash();
        int total_curse = 0;
        if(AbstractDungeon.getCurrRoom() != null &&
                AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss &&
                AbstractDungeon.actNum == 3) {
            for(int i = 0; i < amount; i ++) {
                AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH) * (0.50F), (float) (Settings.HEIGHT) * (0.50F), relic);
            }
        }


        for(int i = 0; i < amount; i ++) {
            if(AbstractDungeon.cardRandomRng.random(99)<=32) {
                total_curse++;

            }
        }
        for(int i = 0; i < total_curse; i ++) {
            float width_ = 0.4f *(total_curse==1?0.5f:(1.0f*i/(total_curse-1)));
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(AbstractDungeon.returnRandomCurse(), (float)(Settings.WIDTH) * (0.30F + width_), (float)(Settings.HEIGHT / 2)));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new GoldWhalePower(owner, source, amount);
    }
}
