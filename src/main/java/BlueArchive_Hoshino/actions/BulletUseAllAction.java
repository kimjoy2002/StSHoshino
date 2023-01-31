package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.UUID;

public class BulletUseAllAction extends AbstractGameAction {
    private UUID uuid;

    public BulletUseAllAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        BulletSubscriber.setBullet(0);
        this.isDone = true;
    }
}
