package BlueArchive_Aris.actions;

import BlueArchive_Aris.characters.Aris;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ChangeArisAniAction extends AbstractGameAction {
    String animation;
    public ChangeArisAniAction(String animation) {
        this.setValues(this.target, this.source, 0);
        this.animation = animation;
        this.actionType = ActionType.TEXT;
    }

    public void update() {
        try {
            if(AbstractDungeon.player instanceof Aris){
                ((Aris)AbstractDungeon.player).newAnimation(animation);
            }
        }
        catch(Exception ignore) {

        }
        this.isDone = true;
    }
}
