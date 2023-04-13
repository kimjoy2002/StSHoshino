package BlueArchive_Aris.actions;


import BlueArchive_Aris.cards.ChooseArmoursmith;
import BlueArchive_Aris.cards.ChooseWeaponsmith;
import BlueArchive_Aris.cards.LevelUp;
import BlueArchive_Aris.powers.JobPower;
import BlueArchive_Hoshino.cards.ChooseNervous;
import BlueArchive_Hoshino.cards.ChooseRepose;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class BlacksmithAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    int amount;
    public BlacksmithAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    private void chooseOption(int amount) {
        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> optionChoices = new ArrayList();
        optionChoices.add(new ChooseWeaponsmith(amount));
        optionChoices.add(new ChooseArmoursmith(amount));
        this.addToBot(new ChooseOneAction(optionChoices));
    }

    public void update() {
        Iterator powerIter = AbstractDungeon.player.powers.iterator();
        while(powerIter.hasNext()) {
            AbstractPower p = (AbstractPower) powerIter.next();
            if (p instanceof JobPower) {
                chooseOption(amount);
                this.isDone = true;
                return;
            }
        }
        AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        this.isDone = true;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Aris:BlacksmithAction");
        TEXT = uiStrings.TEXT;
    }
}
