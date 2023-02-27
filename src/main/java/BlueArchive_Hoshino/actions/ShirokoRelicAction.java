package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.TriggerHappy;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;

public class ShirokoRelicAction extends AbstractGameAction {
    public ShirokoRelicAction(int amount) {
        this.amount = amount;
    }

    public void update() {
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster mo = null;
        while(var3.hasNext()) {
            AbstractMonster temp = (AbstractMonster)var3.next();
            if((mo == null || mo.currentHealth > temp.currentHealth) && temp.currentHealth > 0 && !temp.isDead && !temp.isDying && !temp.halfDead ) {
                mo = temp;
            }
        }
        if(mo != null) {
            AbstractDungeon.actionManager.addToTop(
                    new DamageAction(mo, new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.THORNS),
                            AbstractGameAction.AttackEffect.FIRE));
        }
        this.isDone = true;
    }
}
