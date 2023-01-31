package BlueArchive_Hoshino.events;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.relics.IOURelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.List;

import static BlueArchive_Hoshino.DefaultMod.makeEventPath;

public class ShibasekiRamenEvent extends AbstractImageEvent {

    public static final String ID = DefaultMod.makeID("ShibasekiRamenEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("ShibasekiRamenEvent.png");

    private int screenNum = 0;

    private int shirokoHpUp = 15;

    private int nonomiGoldUp = 100;
    private int nonomiHpDown = 6;

    private int RamenHpHeal = 10;

    public ShibasekiRamenEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel >= 15) {
            shirokoHpUp = 12;
            nonomiGoldUp = 75;
            RamenHpHeal = 8;
        }

        this.imageEventText.setDialogOption(OPTIONS[0] + shirokoHpUp + OPTIONS[1], CardLibrary.getCopy("Doubt"));
        imageEventText.setDialogOption(OPTIONS[2]+nonomiGoldUp+OPTIONS[3]+nonomiHpDown+OPTIONS[4]);
        imageEventText.setDialogOption(OPTIONS[5]+RamenHpHeal+ OPTIONS[6]);
    }


    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        AbstractDungeon.player.increaseMaxHp(shirokoHpUp, true);
                        AbstractCard curse = new Doubt();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        this.imageEventText.clearRemainingOptions();

                        List<String> cardList = new ArrayList();
                        cardList.add(curse.cardID);
                        AbstractEvent.logMetric("ShibasekiRamenEvent", "Sit next to Shiroko", cardList, (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, 0, 0, 0, shirokoHpUp, 0, 0);
                        screenNum = 1;
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        AbstractDungeon.effectList.add(new RainingGoldEffect(this.nonomiGoldUp));
                        AbstractDungeon.player.gainGold(this.nonomiGoldUp);
                        AbstractDungeon.player.decreaseMaxHealth(this.nonomiHpDown);
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractGameAction.AttackEffect.POISON));
                        this.imageEventText.clearRemainingOptions();
                        AbstractEvent.logMetric("ShibasekiRamenEvent", "Sit next to Nonomi", (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, (List)null, 0, 0, this.nonomiHpDown, 0, nonomiGoldUp, 0);
                        screenNum = 1;
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        AbstractDungeon.player.heal(RamenHpHeal, true);
                        AbstractEvent.logMetricHeal("ShibasekiRamenEvent", "Sit Alone", this.RamenHpHeal);
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
