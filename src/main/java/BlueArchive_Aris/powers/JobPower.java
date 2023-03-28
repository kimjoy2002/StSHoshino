package BlueArchive_Aris.powers;

import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.characters.Hoshino;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashSet;
import java.util.Set;

public abstract class JobPower extends AbstractPower {
    public AbstractCard equip;
    public static Set<String> jobThisCombat = new HashSet<String>();
    public abstract void levelUp();
    public abstract String getAnimation();

    public void onJobChange(boolean withEquip) {
        jobThisCombat.add(ID);
        if(AbstractDungeon.player.hasPower(JobMasteryPower.POWER_ID)) {
            ((JobMasteryPower) AbstractDungeon.player.getPower(JobMasteryPower.POWER_ID)).onClassChange(withEquip);
        }
        if(AbstractDungeon.player.hasPower(WeaponMasterPower2.POWER_ID)) {
            ((WeaponMasterPower2) AbstractDungeon.player.getPower(WeaponMasterPower2.POWER_ID)).onClassChange(withEquip);
        }
    };

    public void onVictory() {
        if(AbstractDungeon.player instanceof Aris){
            ((Aris)AbstractDungeon.player).newAnimation("");
        }
    }

    public void onRemove() {
        if(AbstractDungeon.player instanceof Aris){
            ((Aris)AbstractDungeon.player).newAnimation("");
        }

    }
}
