package BlueArchive_Aris.relics;

import BlueArchive_Aris.cards.QuestCard;
import BlueArchive_Aris.characters.Aris;
import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.ChooseNervous;
import BlueArchive_Hoshino.cards.ChooseRepose;
import BlueArchive_Hoshino.cards.HoshinoStrike;
import BlueArchive_Hoshino.cards.ShotCard;
import BlueArchive_Hoshino.patches.relics.BottledPlaceholderField;
import BlueArchive_Hoshino.relics.IOURelic;
import BlueArchive_Hoshino.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.blue.Strike_Blue;
import com.megacrit.cardcrawl.cards.green.Strike_Green;
import com.megacrit.cardcrawl.cards.purple.Strike_Purple;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeArisRelicOutlinePath;
import static BlueArchive_Hoshino.DefaultMod.makeArisRelicPath;

public class CopyCat extends CustomRelic implements CustomSavable<Integer> {

    // ID, images, text.
    public static final String ID = DefaultMod.makeArisID("CopyCat");
    public static AbstractCard.CardColor CopyCatColor =  Aris.Enums.COLOR_BLUE;
    public boolean cardSelected = true;
    private static final Texture IMG = TextureLoader.getTexture(makeArisRelicPath("CopyCat.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeArisRelicOutlinePath("CopyCat.png"));

    public CopyCat() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.SOLID);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public Integer onSave() {
        if (CopyCatColor !=  Aris.Enums.COLOR_BLUE) {
            return CopyCatColor.ordinal();
        } else {
            return -1;
        }
    }

    @Override
    public void onLoad(Integer cardColor) {
        if (cardColor == null) {
            return;
        }
        if(AbstractCard.CardColor.values().length <= cardColor) {
            return;
        }
        CopyCatColor = AbstractCard.CardColor.values()[cardColor];
    }


    public void onEquip() {
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        retVal.addToRandomSpot(new Strike_Red());
        retVal.addToRandomSpot(new Strike_Blue());
        retVal.addToRandomSpot(new Strike_Green());
        retVal.addToRandomSpot(new Strike_Purple());
        retVal.addToRandomSpot(new HoshinoStrike());

        retVal.removeTopCard();
        retVal.removeTopCard();
        AbstractDungeon.gridSelectScreen.open(retVal, 1, DESCRIPTIONS[1], false, false, false, false);
    }

    @Override
    public void update() {
        super.update();
        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            CopyCatColor = card.color;

            {
                AbstractDungeon.combatRewardScreen.open();
                AbstractDungeon.combatRewardScreen.rewards.clear();

                for(AbstractPlayer char_ :CardCrawlGame.characterManager.getAllCharacters()) {
                    if(char_.getCardColor() == CopyCatColor) {
                        for(String relic_ : char_.getStartingRelics()) {
                            AbstractRelic relic = RelicLibrary.getRelic(relic_).makeCopy();
                            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(relic));
                            //AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                        }
                        break;
                    }
                }

                AbstractDungeon.combatRewardScreen.positionRewards();
                AbstractDungeon.overlayMenu.proceedButton.setLabel(this.DESCRIPTIONS[2]);
                AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
            }


            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

        }
    }

    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + 1;
    }


}
