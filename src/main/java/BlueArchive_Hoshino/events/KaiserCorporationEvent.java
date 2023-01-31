package BlueArchive_Hoshino.events;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.relics.IOURelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.WarpedTongs;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

import static BlueArchive_Hoshino.DefaultMod.makeEventPath;

public class KaiserCorporationEvent extends AbstractImageEvent {


    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public static final String ID = DefaultMod.makeID("KaiserCorporationEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("kaiserCorporationEvent1.png");

    private int screenNum = 0;

    private int rare_card_cost;

    public KaiserCorporationEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel >= 15) {
            rare_card_cost = 300;
        } else {
            rare_card_cost = 250;
        }

        if (AbstractDungeon.player.gold >= rare_card_cost) {
            this.imageEventText.setDialogOption(OPTIONS[0] + rare_card_cost + OPTIONS[1], AbstractDungeon.player.gold < rare_card_cost);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[4] + rare_card_cost + OPTIONS[5], AbstractDungeon.player.gold < rare_card_cost);
        }
        imageEventText.setDialogOption(OPTIONS[2], new IOURelic(rare_card_cost));
        imageEventText.setDialogOption(OPTIONS[3]);
    }

    protected void rewardRareCard(){
        ArrayList<AbstractCard> rewardCards = new ArrayList<AbstractCard>();
        while(rewardCards.size() < 3) {
            AbstractCard rewardCard = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE);
            if(!rewardCards.contains(rewardCard))
                rewardCards.add(rewardCard);
        }
        if (rewardCards != null && !rewardCards.isEmpty()) {
            AbstractDungeon.cardRewardScreen.open(rewardCards, (RewardItem)null, TEXT[4]);
        }
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(rare_card_cost);
                        rewardRareCard();
                        screenNum = 1;
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();

                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new IOURelic(rare_card_cost));
                        logMetricObtainRelic("KaiserCorporationEvent", "Sign", new IOURelic(rare_card_cost));
                        rewardRareCard();
                        screenNum = 1;
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();

                        screenNum = 1;
                        break;
                }
                break;
            case 1:
                switch (i) {
                    case 0:
                        openMap();
                        break;
                }
                break;
        }
    }

    public void update() {
        super.update();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("RewardItem");
        TEXT = uiStrings.TEXT;
    }
}
