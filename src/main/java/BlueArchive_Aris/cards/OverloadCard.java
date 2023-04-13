package BlueArchive_Aris.cards;

import BlueArchive_Aris.powers.BalanceBrokenPower;
import BlueArchive_Aris.powers.SystemOverloadPower;
import BlueArchive_Aris.relics.OverloadRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.Iterator;

public abstract class OverloadCard extends AbstractDynamicCard {

    public static int overloadThisCombat = 0;
    public OverloadCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

    public static void overloadCheck() {

        overloadThisCombat++;
        Iterator iter = AbstractDungeon.player.relics.iterator();
        while(iter.hasNext()) {
            AbstractRelic relic = (AbstractRelic)iter.next();
            if(relic instanceof OverloadRelic) {
                ((OverloadRelic)relic).onOverload();
            }
        }
    }



    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (EnergyPanel.totalCount <= this.costForTurn) {
            onOverload(p, m);

            overloadCheck();
            if(AbstractDungeon.player.hasPower(BalanceBrokenPower.POWER_ID)) {
                AbstractPower power = AbstractDungeon.player.getPower(BalanceBrokenPower.POWER_ID);
                for(int i = 0; i < power.amount; i++) {
                    onOverload(p, m);
                    overloadCheck();
                }
            }
        }
    }

    public void onOverload(AbstractPlayer p, AbstractMonster m) {

    }

    public void triggerOnGlowCheck() {
        this.glowColor = BlueArchive_Hoshino.cards.AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (EnergyPanel.totalCount <= this.costForTurn) {
            this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
