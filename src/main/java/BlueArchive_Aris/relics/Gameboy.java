package BlueArchive_Aris.relics;

import BlueArchive_Aris.powers.ShockPower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import static BlueArchive_Hoshino.DefaultMod.makeArisRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeArisRelicPath;

public class Gameboy extends CustomRelic {

    public static int AMOUNT = 50;
    // ID, images, text.
    public static final String ID = DefaultMod.makeArisID("Gameboy");

    private static final Texture IMG = TextureLoader.getTexture(makeArisRelicPath("Gameboy.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeArisRelicOutlinePath("Gameboy.png"));

    public Gameboy() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }


    public void onEquip() {
        int maxHpLoss = MathUtils.ceil((float)AbstractDungeon.player.maxHealth * AMOUNT / 100);
        if (maxHpLoss >= AbstractDungeon.player.maxHealth) {
            maxHpLoss = AbstractDungeon.player.maxHealth - 1;
        }
        AbstractDungeon.player.decreaseMaxHealth(maxHpLoss);
    }

    public void onVictory() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth > 0) {
            p.heal(AbstractDungeon.player.maxHealth);
        }

    }
}
