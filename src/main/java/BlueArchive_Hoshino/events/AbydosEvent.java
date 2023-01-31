package BlueArchive_Hoshino.events;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.relics.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;

import static BlueArchive_Hoshino.DefaultMod.makeEventPath;

public class AbydosEvent extends AbstractImageEvent {


    public static final String ID = DefaultMod.makeID("AbydosEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("AbydosEvent1.png");

    private int arbeit = 100;
    private int screenNum = 0;

    public AbydosEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel >= 15) {
            arbeit = 75;
        }

        if(AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:SerikaRelic")) {
            imageEventText.setDialogOption(OPTIONS[7], true);
        } else {
            imageEventText.setDialogOption(OPTIONS[0], new SerikaRelic());
        }
        if(AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:NonomiRelic")) {
            imageEventText.setDialogOption(OPTIONS[7], true);
        } else {
            imageEventText.setDialogOption(OPTIONS[1], new NonomiRelic());
        }
        if(AbstractDungeon.player.hasRelic("PrismaticShard")) {
            imageEventText.setDialogOption(OPTIONS[7], true);
        } else {
            imageEventText.setDialogOption(OPTIONS[2], new PrismaticShard());
        }
        if(AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:ShirokoRelic")) {
            imageEventText.setDialogOption(OPTIONS[7], true);
        } else {
            imageEventText.setDialogOption(OPTIONS[3], new ShirokoRelic());
        }
        imageEventText.setDialogOption(OPTIONS[4] + arbeit + OPTIONS[5]);
    }

    public static AbstractRelic getPrism() {
        String retVal = null;
        if(AbstractDungeon.shopRelicPool.contains("PrismaticShard")) {
            AbstractDungeon.shopRelicPool.remove("PrismaticShard");
        }
        return RelicLibrary.getRelic("PrismaticShard").makeCopy();
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new SerikaRelic());
                        logMetricObtainRelic("AbydosEvent", "Serika", new SerikaRelic());

                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new NonomiRelic());
                        logMetricObtainRelic("AbydosEvent", "Nonomi", new NonomiRelic());
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), getPrism());
                        logMetricObtainRelic("AbydosEvent", "Hoshino", new PrismaticShard());
                        break;
                    case 3:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new ShirokoRelic());
                        logMetricObtainRelic("AbydosEvent", "Shiroko", new ShirokoRelic());

                        break;
                    case 4:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.gainGold(arbeit);
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
