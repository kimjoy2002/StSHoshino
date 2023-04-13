package BlueArchive_Aris.actions;


import BlueArchive_Aris.cards.ChooseArmoursmith;
import BlueArchive_Aris.cards.ChooseWeaponsmith;
import BlueArchive_Aris.powers.JobPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.ArrayList;
import java.util.Iterator;

public class EquipsmithAction extends AbstractGameAction {
    int amount;
    boolean isStrength;
    public EquipsmithAction(int amount, boolean isStrength) {
        this.amount = amount;
        this.isStrength = isStrength;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }
    public void update() {
        Iterator powerIter = AbstractDungeon.player.powers.iterator();
        while(powerIter.hasNext()) {
            AbstractPower p = (AbstractPower) powerIter.next();
            if (p instanceof JobPower) {
                if(isStrength) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.amount), this.amount));
                    ((JobPower)p).str += amount;
                }
                else {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, this.amount), this.amount));
                    ((JobPower)p).dex += amount;
                }
                this.isDone = true;
                return;
            }
        }
        this.isDone = true;
    }
}
