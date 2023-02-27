package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.monsters.act1.boss.KaitengerCommon;
import BlueArchive_Hoshino.powers.KaitengerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

import static BlueArchive_Hoshino.monsters.act1.boss.KaitengerCommon.remainKaitenger;
import static BlueArchive_Hoshino.powers.KaitengerPower.remain_kaitenger;

public class CallingKaitengerAction extends AbstractGameAction {
    public int num;
    public CallingKaitengerAction(int num) {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.REDUCE_POWER;
        this.num = num;
    }


    public void update() {
        if(remain_kaitenger==0) {
            this.isDone = true;
            return;
        }

        boolean front = false;
        boolean middle = false;
        boolean back = false;


        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if(m.halfDead || m.isDying || m.isDead)
                continue;

            if(m instanceof KaitengerCommon) {
                KaitengerCommon k = (KaitengerCommon)m;
                if(k.kaitenger_position == 0)
                    front = true;
                if(k.kaitenger_position == 1)
                    middle = true;
                if(k.kaitenger_position == 2)
                    back = true;
            }
        }
        if(front && middle && back) {
            this.isDone = true;
            return;
        }

        if(!front && !middle && !back && remain_kaitenger == 4) {
            //시작하자마자 레드를 죽인 특수한 상황
            num = 3;
        }

        if(remainKaitenger.size() == 0){
            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
            this.isDone = true;
            return;
        }
        KaitengerCommon tempKaitenger = remainKaitenger.get(0);
        remainKaitenger.remove(tempKaitenger);
        KaitengerCommon newKaitenger = tempKaitenger.copyKaitenger(!front?-250:(!middle?0:250),0);

        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(newKaitenger, false));

        remain_kaitenger--;

        if(remain_kaitenger != 0) {
            var1 = AbstractDungeon.getMonsters().monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster m = (AbstractMonster) var1.next();
                Iterator var2 =m.powers.iterator();
                while(var2.hasNext()) {
                    AbstractPower p = (AbstractPower) var2.next();
                    if(p instanceof KaitengerPower){
                        p.amount = remain_kaitenger;
                        p.updateDescription();
                    }
                }
            }


            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newKaitenger, newKaitenger, new KaitengerPower(newKaitenger,newKaitenger)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new RemoveAllPowerAction("BlueArchive_Hoshino:KaitengerPower"));
            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
        }
        if(num > 1) {
            AbstractDungeon.actionManager.addToBottom(new CallingKaitengerAction(num-1));
        }

        this.isDone = true;
    }
}
