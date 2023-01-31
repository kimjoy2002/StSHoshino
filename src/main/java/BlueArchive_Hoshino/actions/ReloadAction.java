package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.UUID;

public class ReloadAction extends AbstractGameAction {
    private UUID uuid;
    private int soul;
    private boolean onlyOne = false;
    private boolean always_reload = true;


    public ReloadAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public ReloadAction(boolean always_reload, boolean onlyOne) {
        this.setValues(this.target, this.source, 0);
        this.onlyOne = onlyOne;
        this.always_reload = always_reload;
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public ReloadAction(int soul) {
        this.setValues(this.target, this.source, 0);
        this.soul = soul;
        this.actionType = ActionType.CARD_MANIPULATION;
    }


    public void update() {
        if(soul > 0 &&
                BulletSubscriber.getMaxBullet() > BulletSubscriber.getBullet()
                && AbstractDungeon.player.hasPower("BlueArchive_Hoshino:FreeReloadPower")) {
            AbstractPower rlPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:FreeReloadPower");
            rlPower.flash();
            rlPower.reducePower(1);
            soul = 0;
            if(rlPower.amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "BlueArchive_Hoshino:FreeReloadPower"));
            }
        }

        if(soul > 0) {
            if(BulletSubscriber.reload(-1)) {
                AbstractDungeon.actionManager.addToBottom(new LoseEnergyAction(soul));
            }
        } else {
            BulletSubscriber.reload(!always_reload?-1:(onlyOne?1:0));
        }
        this.isDone = true;
    }
}
