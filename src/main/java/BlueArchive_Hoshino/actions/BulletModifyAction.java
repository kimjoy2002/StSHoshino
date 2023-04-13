package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import BlueArchive_Hoshino.ui.BulletUI;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.UUID;

public class BulletModifyAction extends AbstractGameAction {
    private UUID uuid;

    public BulletModifyAction(int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        BulletUI.useBulletBooleanTurn = true;
        if(amount > 0) {
            BulletSubscriber.addBullet(amount);
        } else if(amount < 0) {
            BulletSubscriber.removeBullet(-amount);
        }
        this.isDone = true;
    }
}
