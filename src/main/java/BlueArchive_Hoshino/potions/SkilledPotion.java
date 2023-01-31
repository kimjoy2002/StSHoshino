package BlueArchive_Hoshino.potions;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.ExpertPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.abstracts.CustomPotion;

import static BlueArchive_Hoshino.DefaultMod.DEFAULT_PINK;

public class SkilledPotion  extends CustomPotion  {
    public static final String POTION_ID = DefaultMod.makeID("SkilledPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public SkilledPotion() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.M, PotionColor.SMOKE);
        this.labOutlineColor = DEFAULT_PINK;
        potency = getPotency();

        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

        isThrown = false;

        tips.add(new PowerTip(name, description));

    }


    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new ExpertPower(target, target, potency), potency));
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new SkilledPotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 3;
    }

    public void upgradePotion()
    {
        potency += 1;
        tips.clear();
        tips.add(new PowerTip(name, description));
    }
}
