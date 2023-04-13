package BlueArchive_Aris.potions;

import BlueArchive_Aris.powers.ChargePower;
import BlueArchive_Aris.powers.ShockPower;
import BlueArchive_Hoshino.DefaultMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.DEFAULT_BLUE;

public class ShockPotion extends CustomPotion  {
    public static final String POTION_ID = DefaultMod.makeArisID("ShockPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public ShockPotion() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.M, PotionColor.POISON);
        this.labOutlineColor = DEFAULT_BLUE;
        potency = getPotency();

        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

        isThrown = true;

        this.targetRequired = true;
        tips.add(new PowerTip(name, description));

    }


    public void use(AbstractCreature target) {
        this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new ShockPower(target, AbstractDungeon.player, this.potency), this.potency));
    }


    @Override
    public AbstractPotion makeCopy() {
        return new ShockPotion();
    }

    // This is your potency.
    @Override
    public int getPotency(int ascensionLevel) {
        return 6;
    }

    public void initializeData() {
        this.potency = this.getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        this.tips.clear();
        tips.add(new PowerTip(name, description));
    }
}
