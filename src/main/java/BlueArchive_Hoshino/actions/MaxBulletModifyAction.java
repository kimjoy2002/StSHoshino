package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.UUID;

public class MaxBulletModifyAction extends AbstractGameAction {
    private UUID uuid;

    public MaxBulletModifyAction(int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if(amount > 0) {
            BulletSubscriber.addMaxBullet(amount);
        } else if(amount < 0) {
            BulletSubscriber.removeMaxBullet(-amount);
        }
        this.isDone = true;
    }
}
