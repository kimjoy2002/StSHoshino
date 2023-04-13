package BlueArchive_Aris.potions;

import BlueArchive_Aris.powers.ChargePower;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.ExpertPower;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.DEFAULT_BLUE;
import static BlueArchive_Hoshino.DefaultMod.DEFAULT_PINK;

public class ChargePotion extends CustomPotion  {
    public static final String POTION_ID = DefaultMod.makeArisID("ChargePotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public ChargePotion() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.BOLT, PotionColor.ENERGY);
        this.labOutlineColor = DEFAULT_BLUE;
        potency = getPotency();

        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

        isThrown = false;

        tips.add(new PowerTip(name, description));

    }


    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new ChargePower(target, potency), potency));
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new ChargePotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 2;
    }

    public void initializeData() {
        this.potency = this.getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        this.tips.clear();
        tips.add(new PowerTip(name, description));
    }
}
