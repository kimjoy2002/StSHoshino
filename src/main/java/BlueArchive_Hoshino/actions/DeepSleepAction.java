package BlueArchive_Hoshino.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.Iterator;

public class DeepSleepAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private int damage;
    private AbstractPlayer p;
    private AbstractMonster m;
    private DamageInfo.DamageType damageTypeForTurn;
    private int energyOnUse;
    private int effect = -1;
    private boolean actions = false;



    public DeepSleepAction(AbstractPlayer p, AbstractMonster m, int damage, DamageInfo.DamageType damageTypeForTurn, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.m = m;
        this.damage = damage;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.damageTypeForTurn = damageTypeForTurn;
        this.energyOnUse = energyOnUse;
    }

    public void update() {
        if(effect == -1) {
            effect = EnergyPanel.totalCount;
            if (this.energyOnUse != -1) {
                effect = this.energyOnUse;
            }

            if (this.p.hasRelic("Chemical X")) {
                effect += 2;
                this.p.getRelic("Chemical X").flash();
            }
            this.duration -= Gdx.graphics.getDeltaTime();
            return;
        } else if(effect > 0 && actions == false) {
            if(reduceOnce()) {
                effect --;
            }
            else {
                effect = 0;
            }
            actions = true;
            this.duration -= Gdx.graphics.getDeltaTime();
            return;
        } else if(effect > 0 && actions) {
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0.0F) {
                this.duration = Settings.ACTION_DUR_XFAST;
                actions = false;
            }
            return;
        }
        if (!this.freeToPlayOnce) {
            this.p.energy.use(EnergyPanel.totalCount);
        }
        this.isDone = true;
    }


    public boolean reduceOnce() {
        boolean betterPossible = false;
        boolean possible = false;
        Iterator var3 = this.p.hand.group.iterator();

        while (var3.hasNext()) {
            AbstractCard c = (AbstractCard) var3.next();
            if (c.costForTurn > 0) {
                betterPossible = true;
            } else if (c.cost > 0) {
                possible = true;
            }
        }

        if (betterPossible || possible) {
            this.findAndModifyCard(betterPossible);
            return true;
        }
        return false;
    }

    private void findAndModifyCard(boolean better) {
        AbstractCard c = this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
        if (better) {
            if (c.costForTurn > 0) {
                if(c.cost>0)c.cost--;
                if(c.costForTurn>0)c.costForTurn--;
                c.isCostModified = true;
                c.superFlash(Color.GOLD.cpy());
            } else {
                this.findAndModifyCard(better);
            }
        } else if (c.cost > 0) {
            if(c.cost>0)c.cost--;
            if(c.costForTurn>0)c.costForTurn--;
            c.isCostModified = true;
            c.superFlash(Color.GOLD.cpy());
        } else {
            this.findAndModifyCard(better);
        }
    }
}
