package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.DrowsyCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;
import java.util.UUID;

public class DrowsyAction extends AbstractGameAction {
    private UUID uuid;

    public DrowsyAction(UUID targetUUID, int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update() {
        Iterator var1 = GetAllInBattleInstances.get(this.uuid).iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            if(DrowsyCard.isDrowsyCard(c)) {
                int prev_misc = c.misc;
                c.misc += this.amount;
                if (c.misc < 0) {
                    c.misc = 0;
                }
                if(c.misc > 9) {
                    c.misc = 9;
                }

                if(prev_misc > c.misc && AbstractDungeon.player.hasPower("BlueArchive_Hoshino:HorusOfEyePower2")) {
                    AbstractPower hsPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:HorusOfEyePower2");
                    int loop = prev_misc - c.misc;
                    hsPower.flash();
                    for(int i = 0; i < loop ; i++) {
                        this.addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, hsPower.amount, DamageInfo.DamageType.THORNS), AttackEffect.SLASH_HORIZONTAL));
                    }
                }
                DrowsyCard.whenDrowsyChange(c, prev_misc);
            }
        }

        this.isDone = true;
    }
}
