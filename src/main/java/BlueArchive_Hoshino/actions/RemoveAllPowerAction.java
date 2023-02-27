package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.DrowsyCard;
import BlueArchive_Hoshino.monsters.act1.boss.KaitengerCommon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;

public class RemoveAllPowerAction extends AbstractGameAction {
    public String powerName;
    public RemoveAllPowerAction(String powerName) {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.REDUCE_POWER;
        this.powerName = powerName;
    }


    public void update() {

        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();

            this.addToBot(new RemoveSpecificPowerAction(m, m, powerName));
        }
        this.isDone = true;
    }
}
