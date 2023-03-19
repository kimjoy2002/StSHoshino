package BlueArchive_Aris.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

import static BlueArchive_Aris.cards.OverloadCard.overloadThisCombat;
import static BlueArchive_Aris.powers.ChargePower.chargeThisCombat;

public class ElecWhirlwindAction extends AbstractGameAction {
    public int[] multiDamage;
    private DamageInfo.DamageType damageType;
    private AbstractPlayer p;

    public ElecWhirlwindAction(AbstractPlayer p, int[] multiDamage, DamageInfo.DamageType damageType) {
        this.multiDamage = multiDamage;
        this.damageType = damageType;
        this.p = p;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
    }

    public void update() {
        int effect = overloadThisCombat;

        if (effect > 0) {
            for(int i = 0; i < effect; ++i) {
                if (i == 0) {
                    this.addToBot(new SFXAction("ATTACK_WHIRLWIND"));
                    this.addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));
                }

                this.addToBot(new SFXAction("ATTACK_HEAVY"));
                this.addToBot(new VFXAction(this.p, new CleaveEffect(), 0.0F));
                this.addToBot(new DamageAllEnemiesAction(this.p, this.multiDamage, this.damageType, AttackEffect.NONE, true));
            }
        }

        this.isDone = true;
    }
}
