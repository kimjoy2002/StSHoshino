package BlueArchive_Aris.actions;

import BlueArchive_Aris.powers.JobPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

public class StartingEquipmentAction extends AbstractGameAction {
    public StartingEquipmentAction(int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        this.addToBot(new EquipsmithAction(amount, true));
        this.addToBot(new EquipsmithAction(amount, false));
        this.isDone = true;
    }
}
