package BlueArchive_Hoshino.potions;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.MaxBulletModifyAction;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hoshino.DefaultMod.DEFAULT_PINK;

public class BulletPotion extends CustomPotion  {
    public static final String POTION_ID = DefaultMod.makeID("BulletPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public BulletPotion() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.M, PotionColor.SMOKE);
        this.labOutlineColor = DEFAULT_PINK;

        potency = getPotency();

        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

        isThrown = false;

        tips.add(new PowerTip(name, description));
        //this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.POISON.NAMES[0]), (String)GameDictionary.keywords.get(GameDictionary.POISON.NAMES[0])));

    }


    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new MaxBulletModifyAction(potency));
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BulletPotion();
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
