package BlueArchive_Hoshino.events;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.relics.IOURelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.city.Colosseum;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.List;

import static BlueArchive_Hoshino.DefaultMod.makeEventPath;

public class KaiserCorporationEvent2 extends AbstractImageEvent {


    public static final String ID = DefaultMod.makeID("KaiserCorporationEvent2");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("kaiserCorporationEvent2.png");

    private CurScreen screen;

    private int loan_cost = 300;
    private int card_cost = 30;
    private int shop_cost = 75;


    AbstractRelic relic = null;
    AbstractCard card = null;

    public KaiserCorporationEvent2() {
        super(NAME, DESCRIPTIONS[0], IMG);
        screen = CurScreen.INTRO;

        if (AbstractDungeon.ascensionLevel >= 15) {
            loan_cost = (int)(loan_cost-50);
            card_cost += 5;
            shop_cost += 10;
        }

        relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
        card = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE);

        if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:IOURelic")) {
            imageEventText.setDialogOption(OPTIONS[11], true);
        }
        else {
            imageEventText.setDialogOption(OPTIONS[0] + loan_cost + OPTIONS[1], new IOURelic(loan_cost));
        }

        if (AbstractDungeon.player.gold >= card_cost) {
            this.imageEventText.setDialogOption(OPTIONS[2] + card_cost + OPTIONS[12] + card.name.replace(" ", " #g") + OPTIONS[3], AbstractDungeon.player.gold < card_cost, card);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[8] + card_cost + OPTIONS[9], AbstractDungeon.player.gold < card_cost);
        }
        if (AbstractDungeon.player.gold >= shop_cost) {
            this.imageEventText.setDialogOption(OPTIONS[4] + shop_cost + OPTIONS[12] + relic.name.replace(" ", " #g") + OPTIONS[5], AbstractDungeon.player.gold < shop_cost, relic.makeCopy());
        } else {
            this.imageEventText.setDialogOption(OPTIONS[8] + shop_cost + OPTIONS[9], AbstractDungeon.player.gold < shop_cost);
        }
        if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:ShirokoRelic") || AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:IOURelic")) {
            imageEventText.setDialogOption(OPTIONS[6]);
        } else {
            imageEventText.setDialogOption(OPTIONS[10], true);
        }
        imageEventText.setDialogOption(OPTIONS[7]);
    }

    @Override
    protected void buttonEffect(int i) {
        switch (this.screen) {
            case INTRO:
                switch (i) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new RainingGoldEffect(loan_cost));
                        AbstractDungeon.player.gainGold(loan_cost);
                        AbstractRelic iourelic = new IOURelic(loan_cost);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), iourelic);
                        List<String> relicList = new ArrayList();
                        relicList.add(iourelic.relicId);
                        logMetric("KaiserCorporationEvent2", "Loan", (List)null, (List)null, (List)null, (List)null, relicList, (List)null, (List)null, 0, 0, 0, 0, loan_cost, 0);
                        screen = CurScreen.LEAVE;
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();
                        if (AbstractDungeon.player.gold >= card_cost) {
                            List<String> cardBuy = new ArrayList();
                            cardBuy.add(card.cardID);
                            AbstractEvent.logMetric("KaiserCorporationEvent2", "Buy Card", cardBuy, (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, 0, 0, 0, 0, 0, card_cost);
                            AbstractDungeon.player.loseGold(card_cost);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                        }
                        screen = CurScreen.LEAVE;
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();
                        if (AbstractDungeon.player.gold >= shop_cost) {
                            AbstractEvent.logMetricObtainRelicAtCost("KaiserCorporationEvent2", "Buy Relic", relic, shop_cost);
                            AbstractDungeon.player.loseGold(shop_cost);
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                        }
                        screen = CurScreen.LEAVE;
                        break;
                    case 3:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();
                        screen = CurScreen.FIGHT;
                        break;
                    case 4:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();
                        screen = CurScreen.LEAVE;

                        break;
                }
                break;
            case FIGHT:
            switch (i) {
                case 0:
                    this.screen = CurScreen.POST_COMBAT;
                    //this.logMetric("Fight");
                    AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("Colosseum Slavers");
                    AbstractDungeon.getCurrRoom().rewards.clear();
                    AbstractDungeon.getCurrRoom().rewardAllowed = false;
                    this.enterCombatFromImage();
                    AbstractDungeon.lastCombatMetricKey = "Colosseum Slavers";
                default:
                    this.imageEventText.clearRemainingOptions();
                    return;
            }
            case POST_COMBAT:
                AbstractDungeon.getCurrRoom().rewardAllowed = true;
                switch (i) {
                    case 0:
                    default:
                        openMap();
                        break;
                }
                break;
            case LEAVE:
                switch (i) {
                    case 0:
                        openMap();
                        break;
                }
                break;
        }
    }



    public void reopen() {
        if (this.screen != CurScreen.LEAVE) {
            AbstractDungeon.resetPlayer();
            AbstractDungeon.player.drawX = (float) Settings.WIDTH * 0.25F;
            AbstractDungeon.player.preBattlePrep();
            this.enterImageFromCombat();
            AbstractDungeon.player.gainGold(300);
            AbstractDungeon.effectList.add(new RainingGoldEffect(300));
            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:IOURelic")) {
                AbstractDungeon.player.loseRelic("BlueArchive_Hoshino:IOURelic");
                this.imageEventText.updateBodyText(DESCRIPTIONS[6]);
            } else {
                this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
            }
            this.imageEventText.updateDialogOption(0, OPTIONS[7]);
        }
    }

    public void update() {
        super.update();
    }
    private static enum CurScreen {
        INTRO,
        FIGHT,
        LEAVE,
        POST_COMBAT;

        private CurScreen() {
        }
    }
}
