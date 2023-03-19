package BlueArchive_Aris.cards;

import BlueArchive_Aris.powers.BalanceBrokenPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public abstract class OverloadCard extends AbstractDynamicCard {

    public static int overloadThisCombat = 0;
    public OverloadCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (EnergyPanel.totalCount <= this.costForTurn) {
            onOverload(p, m);
            overloadThisCombat++;
            if(AbstractDungeon.player.hasPower(BalanceBrokenPower.POWER_ID)) {
                AbstractPower power = AbstractDungeon.player.getPower(BalanceBrokenPower.POWER_ID);
                for(int i = 0; i < power.amount; i++) {
                    onOverload(p, m);
                    overloadThisCombat++;
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
