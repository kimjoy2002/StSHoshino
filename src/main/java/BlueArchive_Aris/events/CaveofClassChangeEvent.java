package BlueArchive_Aris.events;

import BlueArchive_Aris.cards.Mask;
import BlueArchive_Aris.cards.IdolRibbon;
import BlueArchive_Aris.cards.Rogue;
import BlueArchive_Aris.cards.WizardHat;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.patches.EnumPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static BlueArchive_Hoshino.DefaultMod.makeArisEventPath;

public class CaveofClassChangeEvent extends AbstractImageEvent {

    public static final String ID = DefaultMod.makeArisID("CaveofClassChangeEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeArisEventPath("CaveofClassChangeEvent.png");


    public CaveofClassChangeEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.imageEventText.setDialogOption(OPTIONS[0], CardLibrary.getCopy(Mask.ID));
        imageEventText.setDialogOption(OPTIONS[1], CardLibrary.getCopy(Rogue.ID));
        imageEventText.setDialogOption(OPTIONS[2], CardLibrary.getCopy(IdolRibbon.ID));
        imageEventText.setDialogOption(OPTIONS[3], CardLibrary.getCopy(WizardHat.ID));
        imageEventText.setDialogOption(OPTIONS[4]);
    }

    public void removeAllEquipment() {

        for(int i = AbstractDungeon.player.masterDeck.group.size() - 1; i >= 0; --i) {
            if (((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)).hasTag(EnumPatch.EQUIPMENT) && !((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)).inBottleFlame && !((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)).inBottleLightning) {
                AbstractDungeon.effectList.add(new PurgeCardEffect((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)));
                //Eu.add(((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)).cardID);
                AbstractDungeon.player.masterDeck.removeCard((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i));
            }
        }
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        removeAllEquipment();
                        AbstractCard crowbar = new Mask();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(crowbar, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        this.imageEventText.clearRemainingOptions();

                        AbstractEvent.logMetricObtainCard("CaveofClassChangeEvent", "Warrior",new Mask());
                        screenNum = 1;
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        removeAllEquipment();
                        AbstractCard rogue = new Rogue();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(rogue, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        this.imageEventText.clearRemainingOptions();

                        AbstractEvent.logMetricObtainCard("CaveofClassChangeEvent", "Rogue",new Rogue());
                        screenNum = 1;
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        removeAllEquipment();
                        AbstractCard idolRibbon = new IdolRibbon();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(idolRibbon, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        this.imageEventText.clearRemainingOptions();

                        AbstractEvent.logMetricObtainCard("CaveofClassChangeEvent", "Idol",new IdolRibbon());
                        screenNum = 1;
                        break;
                    case 3:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        removeAllEquipment();
                        AbstractCard wizardHat = new WizardHat();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(wizardHat, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        this.imageEventText.clearRemainingOptions();

                        AbstractEvent.logMetricObtainCard("CaveofClassChangeEvent", "Wizard",new WizardHat());
                        screenNum = 1;
                        break;
                    case 4:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
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
