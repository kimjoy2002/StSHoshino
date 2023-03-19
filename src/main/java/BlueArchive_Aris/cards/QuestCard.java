package BlueArchive_Aris.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class QuestCard extends AbstractDynamicCard {
    private static final Logger logger = LogManager.getLogger(QuestCard.class.getName());
    public QuestCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

     public enum QuestProcess {
         POTION_DROP,
         GAIN_GOLD,
         GAIN_CARD,
         SKIP_CARD,
         SLAIN_ELITE,
         PROGRESS_FLOOR

    }
    static ArrayList<AbstractCard> removeList = new ArrayList<AbstractCard>();

    public abstract boolean onQuestCheck(QuestProcess process);
    public void onQuestComplete() {
        AbstractCard card = makeCopy();
        //AbstractCard card = (AbstractCard)i.next();
        //card.untip();
        //card.unhover();

        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (float) Settings.WIDTH / 3.0F , (float)Settings.HEIGHT / 2.0F));
        removeList.add(this);
    }
    public static void questCheck(QuestProcess process) {
        try {
            Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();
            ArrayList<AbstractCard> completeList = new ArrayList<AbstractCard>();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                if (c instanceof QuestCard) {
                    if(((QuestCard) c).onQuestCheck(process)) {
                        completeList.add(c);
                    }
                }
            }
            if(!removeList.isEmpty()) {
                for(AbstractCard c : removeList) {
                    AbstractDungeon.player.masterDeck.removeCard(c);
                }
                removeList.clear();
            }
            if(!completeList.isEmpty()) {
                for(AbstractCard c : completeList) {
                    ((QuestCard) c).onQuestComplete();
                }
            }

        } catch(Exception e) {
            logger.warn("error occurs:" + e.getMessage());
        }
    }
    public static boolean hasCardSkipQuest() {

        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c instanceof Diet) {
                return true;
            }
        }
        return false;
    }



    public abstract void makeDescription();
    public abstract void questInit();
}
