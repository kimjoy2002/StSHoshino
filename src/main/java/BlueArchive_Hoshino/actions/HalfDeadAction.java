package BlueArchive_Hoshino.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HalfDeadAction extends AbstractGameAction {
    public AbstractMonster m;
    public boolean half_dead = true;
    public HalfDeadAction(AbstractMonster m) {
        this.m = m;
        this.half_dead = true;
    }
    public HalfDeadAction(AbstractMonster m, boolean half_dead) {
        this.m = m;
        this.half_dead = half_dead;
    }

    public void update() {
        m.halfDead = this.half_dead;
        this.isDone = true;
    }
}
