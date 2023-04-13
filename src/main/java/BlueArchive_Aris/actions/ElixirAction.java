package BlueArchive_Aris.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.Iterator;
import java.util.UUID;

public class ElixirAction extends AbstractGameAction {
    int hp;
    int maxHealth;
    public ElixirAction(int hp, int maxHealth) {
        this.setValues(this.target, this.source, 0);
        this.hp = hp;
        this.maxHealth = maxHealth;
        this.actionType = ActionType.HEAL;
    }

    public void update() {
        AbstractDungeon.player.increaseMaxHp(maxHealth, false);
        AbstractDungeon.player.heal(hp==-1?AbstractDungeon.player.maxHealth:hp);
        this.isDone = true;
    }
}
