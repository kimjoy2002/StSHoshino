package BlueArchive_Hoshino.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HalfDeadAction extends AbstractGameAction {
    public AbstractMonster m;
    public HalfDeadAction(AbstractMonster m) {
        this.m = m;
    }

    public void update() {
        m.halfDead = true;
        this.isDone = true;
    }
}
