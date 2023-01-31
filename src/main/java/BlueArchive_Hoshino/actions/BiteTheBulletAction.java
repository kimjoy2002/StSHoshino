package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.cards.ChooseNervous;
import BlueArchive_Hoshino.cards.ChooseRepose;
import BlueArchive_Hoshino.cards.DrowsyCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;

public class BiteTheBulletAction extends AbstractGameAction {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;

    public BiteTheBulletAction() {
        this.p = AbstractDungeon.player;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    }

    private void chooseOption() {
        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> optionChoices = new ArrayList();
        optionChoices.add(new ChooseRepose());
        optionChoices.add(new ChooseNervous());
        this.addToBot(new ChooseOneAction(optionChoices));
    }

    public void update() {
        boolean hasDrowsy = false;
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            if (DrowsyCard.isDrowsyCard(c)) {
                hasDrowsy = true;
            }
        }
        if(hasDrowsy) {
            chooseOption();
        }
        this.isDone = true;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:BDNoteAction");
        TEXT = uiStrings.TEXT;
    }
}
