package BlueArchive_Aris.actions;

import BlueArchive_Aris.powers.JobPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.Iterator;
import java.util.UUID;

public class PermanentExhaustAction extends AbstractGameAction {
    UUID uuid;
    public PermanentExhaustAction(UUID uuid) {
        this.setValues(this.target, this.source, 0);
        this.uuid = uuid;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.uuid == uuid) {
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                AbstractDungeon.player.masterDeck.removeCard(c);
                break;
            }
        }
        this.isDone = true;
    }
}
