package BlueArchive_Aris.actions;


import BlueArchive_Aris.cards.ChooseArmoursmith;
import BlueArchive_Aris.cards.ChooseRefuse;
import BlueArchive_Aris.cards.ChooseWeaponsmith;
import BlueArchive_Aris.cards.GameScenario;
import BlueArchive_Aris.powers.JobPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.ArrayList;
import java.util.Iterator;

public class MomoiAction extends AbstractGameAction {
    AbstractCard card;
    int amount;
    public MomoiAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    private void chooseOption(int amount) {
        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> optionChoices = new ArrayList();
        GameScenario game = (GameScenario) GameScenario.makeCustomCard(card, amount, AbstractDungeon.miscRng.randomBoolean() ? AbstractCard.CardType.ATTACK: AbstractCard.CardType.SKILL);

        optionChoices.add(game);
        optionChoices.add(new ChooseRefuse(card));
        this.addToBot(new ChooseOneAction(optionChoices));
    }

    public void update() {
        chooseOption(amount);
        this.isDone = true;
    }

}
