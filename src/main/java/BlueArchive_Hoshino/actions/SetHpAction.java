package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.monsters.act3.boss.GreenRelic;
import BlueArchive_Hoshino.monsters.act3.boss.RedRelic;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SetHpAction extends AbstractGameAction {
    public AbstractMonster m;
    public int hp;
    public SetHpAction(AbstractMonster m, int hp) {
        this.m = m;
        this.hp = hp;
    }

    public void update() {
        m.currentHealth = hp;
        m.healthBarUpdatedEvent();
        if( m instanceof RedRelic) {
            AnimationState.TrackEntry e = m.state.setAnimation(0, "red_relic", false);
            float relic_endtimes = e.getEndTime();
            e.setTime(relic_endtimes/m.maxHealth);
            e.setEndTime(relic_endtimes/m.maxHealth);
        }
        if( m instanceof GreenRelic) {
            AnimationState.TrackEntry e = m.state.setAnimation(0, "green_relic", false);
            float relic_endtimes = e.getEndTime();
            e.setTime(relic_endtimes/m.maxHealth);
            e.setEndTime(relic_endtimes/m.maxHealth);
        }
        this.isDone = true;
    }
}
