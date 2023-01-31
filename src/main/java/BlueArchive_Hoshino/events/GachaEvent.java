package BlueArchive_Hoshino.events;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.effects.GachaCardEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.*;

import static BlueArchive_Hoshino.DefaultMod.makeEventPath;

public class GachaEvent extends AbstractImageEvent {

    public static final String ID = DefaultMod.makeID("GachaEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("GachaEvent.png");

    private int screenNum = 0;

    private int gachaCost = 12;
    private List<Integer> gacha_result = new ArrayList<Integer>();
    boolean good_ = false;
    boolean very_good_ = false;

    int max_gacha = 0;


    public GachaEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel >= 15) {
            gachaCost = 15;
        }

        restart();
    }


    public void restart() {
        if (AbstractDungeon.player.gold >= gachaCost) {
            this.imageEventText.setDialogOption(OPTIONS[0] + gachaCost + OPTIONS[1], AbstractDungeon.player.gold < gachaCost);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[5] + gachaCost + OPTIONS[6], AbstractDungeon.player.gold < gachaCost);
        }
        if (AbstractDungeon.player.gold >= gachaCost *10) {
            this.imageEventText.setDialogOption(OPTIONS[2] + gachaCost *10 + OPTIONS[3], AbstractDungeon.player.gold < gachaCost *10);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[5] + gachaCost *10 + OPTIONS[6], AbstractDungeon.player.gold < gachaCost *10);
        }
        imageEventText.setDialogOption((max_gacha==1 || max_gacha==2)?OPTIONS[7]:OPTIONS[4]);
    }

    protected int gecha() {
        int rand_ = AbstractDungeon.cardRandomRng.random(99);
        if (rand_ < 3) {
            return 3;
        } else if (rand_ < 21){
            return 2;
        } else {
            return 1;
        }
    }

    private List<String> acquireRelics(int number) {
        List<String> acquiredRelics = new ArrayList();
        for(int i = 0; i < number ; i++) {
            AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.RARE);
            acquiredRelics.add(relic.relicId);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
        }
        return acquiredRelics;
    }
    private List<String> upgradeCards(int number) {
        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        ArrayList<AbstractCard> upgradableCards = new ArrayList();
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while (var2.hasNext()) {
            AbstractCard c = (AbstractCard) var2.next();
            if (c.canUpgrade()) {
                upgradableCards.add(c);
            }
        }

        List<String> cardMetrics = new ArrayList();
        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!upgradableCards.isEmpty()) {
            for (int i = 0; i < number; i++) {
                if (i < upgradableCards.size()) {
                    ((AbstractCard) upgradableCards.get(i)).upgrade();
                    cardMetrics.add(((AbstractCard) upgradableCards.get(i)).cardID);
                    AbstractDungeon.player.bottledCardUpgradeCheck((AbstractCard) upgradableCards.get(i));
                    ShowCardBrieflyEffect e = new ShowCardBrieflyEffect(((AbstractCard) upgradableCards.get(i)).makeStatEquivalentCopy());
                    AbstractDungeon.effectList.add(e);
                }
            }
        }
        return cardMetrics;
    }


    private String resultGacha(boolean ten) {
        String resultString = OPTIONS[8];
        boolean comma = false;
        int ssr_ = 0, sr_ = 0, r_ = 0;
        for(Integer result_ : gacha_result) {
            if(result_==3)
                ssr_++;
            else if(result_==2)
                sr_++;
            else
                r_++;
        }

        List<String> relic_ = null;
        if(ssr_ > 0) {
            relic_ = acquireRelics(ssr_);
            resultString += "#ySSR " + ssr_ + OPTIONS[9];
            comma=true;
        }

        List<String> upgrade_ = null;
        if(sr_ > 0) {
            if(comma){
                resultString += ", ";
            }
            upgrade_ = upgradeCards(sr_);
            resultString += "#ySR " + sr_ + OPTIONS[9];
            comma=true;
        }

        if(r_ >0) {
            if(comma){
                resultString += ", ";
            }
            AbstractDungeon.player.heal(r_, true);
            resultString += "#yR " + r_ + OPTIONS[9];
        }
        resultString += ") NL ";

        AbstractEvent.logMetric("GachaEvent", ten?"10 pull":"single pull", (List)null, (List)null, (List)null, upgrade_, relic_, (List)null, (List)null, 0, r_, 0, 0, 0,
                ten?10*gachaCost:gachaCost);
        return resultString;
    }




    protected  void gachaAndEffect(boolean ten) {
        gacha_result.clear();
        good_ = false;
        very_good_ = false;
        if(ten) {
            for(int i = 0; i < 10; i ++) {
                int rare = gecha();
                if(rare == 1 && i == 9 && !good_ && !very_good_) {
                    rare = 2;
                }
                if(max_gacha < rare)
                    max_gacha = rare;
                gacha_result.add(rare);
                if(rare==3)very_good_=true;
                if(rare==2)good_=true;
                float width_ = 0.15f *(i%5);
                float height_ = 0.4f * (((i/5) >= 1) ?1:0);
                AbstractDungeon.topLevelEffects.add(new GachaCardEffect(
                        rare,
                        (float)(Settings.WIDTH) * (0.20F + width_),
                        (float)(Settings.HEIGHT) * (0.30F + height_)));
            }
        } else {
            int rare = gecha();
            gacha_result.add(rare);
            if(rare==3)very_good_=true;
            if(rare==2)good_=true;
            if(max_gacha < rare)
                max_gacha = rare;

            AbstractDungeon.topLevelEffects.add(new GachaCardEffect(
                    rare,
                    (float)(Settings.WIDTH) * 0.2F,
                    (float)(Settings.HEIGHT) * 0.7F));
        }

    }




    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0: {
                        AbstractDungeon.player.loseGold(gachaCost);
                        gachaAndEffect(false);
                        String resultString = resultGacha(false);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateBodyText(resultString + (very_good_?DESCRIPTIONS[1]:(good_?DESCRIPTIONS[2]:DESCRIPTIONS[3])));
                        restart();
                        screenNum = 0;
                        break;
                    }
                    case 1: {
                        AbstractDungeon.player.loseGold(gachaCost*10);
                        gachaAndEffect(true);
                        String resultString = resultGacha(true);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateBodyText(resultString + (very_good_?DESCRIPTIONS[1]:(good_?DESCRIPTIONS[2]:DESCRIPTIONS[3])));
                        restart();
                        screenNum = 0;
                        break;
                    }
                    case 2:
                        this.imageEventText.updateBodyText( max_gacha==0?DESCRIPTIONS[4]:max_gacha==3?DESCRIPTIONS[5]:DESCRIPTIONS[6]);
                        if(max_gacha==1 || max_gacha==2) {
                            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                            CardCrawlGame.sound.play("BLUNT_FAST");
                        }
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
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

}
