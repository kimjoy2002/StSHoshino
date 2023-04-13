package BlueArchive_Aris.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.Locale;

public abstract  class EquipmentCard extends AbstractDynamicCard {
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(EnhancementScroll.ID);
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    public int str = 0;
    public int dex = 0;
    public static String temp_str = new String();

    public EquipmentCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }
    public void initializeDescription() {
        if(temp_str.isEmpty()) {
            temp_str = rawDescription;
            if(str != 0 && dex == 0) {
                rawDescription = EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[1] + (str>0?"+":"") + str + EXTENDED_DESCRIPTION[4] + rawDescription;
            }
            else if(str == 0 && dex != 0) {
                rawDescription = EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[3] + (dex>0?"+":"") + dex + EXTENDED_DESCRIPTION[4] + rawDescription;
            }
            else if(str != 0 && dex != 0) {
                rawDescription = EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[1] + (str>0?"+":"") + str +EXTENDED_DESCRIPTION[2] + EXTENDED_DESCRIPTION[3] + (dex>0?"+":"") + dex + EXTENDED_DESCRIPTION[4] + rawDescription;
            }
        }
        super.initializeDescription();
        if(!temp_str.isEmpty()) {
            rawDescription = temp_str;
            temp_str = "";
        }
    }
}
