package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.monsters.act1.boss.KaitengerCommon;
import BlueArchive_Hoshino.monsters.act4.boss.PeroroHifumi;
import BlueArchive_Hoshino.powers.KaitengerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

import static BlueArchive_Hoshino.monsters.act1.boss.KaitengerCommon.remainKaitenger;
import static BlueArchive_Hoshino.powers.KaitengerPower.remain_kaitenger;

public class PeroroUpgradeAction extends AbstractGameAction {
    public PeroroUpgradeAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.SPECIAL;
    }


    public void update() {
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if(m instanceof PeroroHifumi) {
                PeroroHifumi k = (PeroroHifumi)m;
                ((DamageInfo)k.damage.get(0)).base+=5;
                ((DamageInfo)k.damage.get(1)).base+=1;
                k.maxHealth += 5;
                if(!k.halfDead) {
                    k.heal(5);
                }
                k.sizeUp();
                k.updateMove();
                AbstractDungeon.onModifyPower();
            }
        }

        this.isDone = true;
    }
}
