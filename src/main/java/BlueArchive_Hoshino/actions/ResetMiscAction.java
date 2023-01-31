package BlueArchive_Hoshino.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.Iterator;
import java.util.UUID;

public class ResetMiscAction extends AbstractGameAction {
    private int miscValue;
    private UUID uuid;

    public ResetMiscAction(UUID targetUUID, int miscValue) {
        this.miscValue = miscValue;
        this.uuid = targetUUID;
    }

    public void update() {
        Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.uuid.equals(this.uuid)) {
                c.misc = this.miscValue;
                c.applyPowers();
                c.baseBlock = c.misc;
                c.isBlockModified = false;
            }
        }

        for(var1 = GetAllInBattleInstances.get(this.uuid).iterator(); var1.hasNext(); c.baseBlock = c.misc) {
            c = (AbstractCard)var1.next();
            c.misc = this.miscValue;
            c.applyPowers();
        }

        this.isDone = true;
    }
}
