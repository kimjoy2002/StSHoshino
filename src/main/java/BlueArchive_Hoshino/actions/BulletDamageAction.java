package BlueArchive_Hoshino.actions;

import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BulletDamageAction extends DamageAction {
    private int goldAmount_;
    private boolean skipWait_;
    private boolean muteSfx_;
    private AbstractCard c;

    public BulletDamageAction(AbstractCreature target, AbstractCard c, AbstractGameAction.AttackEffect effect) {
        super(target, new DamageInfo(AbstractDungeon.player, c.damage, c.damageTypeForTurn), effect);
        goldAmount_ = 0;
        skipWait_ = false;
        muteSfx_ = false;
        this.c  = c;
    }

    @Override
    public void update() {
        if (this.shouldCancelAction() && c.damageTypeForTurn != DamageInfo.DamageType.THORNS) {
            this.isDone = true;
        } else {
            if (this.duration == 0.1F) {
                if (c.damageTypeForTurn != DamageInfo.DamageType.THORNS && (target.isDying || target.halfDead)) {
                    this.isDone = true;
                    return;
                }

                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, this.muteSfx_));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.attackEffect == AttackEffect.POISON) {
                    this.target.tint.color.set(Color.CHARTREUSE.cpy());
                    this.target.tint.changeColor(Color.WHITE.cpy());
                } else if (this.attackEffect == AttackEffect.FIRE) {
                    this.target.tint.color.set(Color.RED);
                    this.target.tint.changeColor(Color.WHITE.cpy());
                }
                if(BulletSubscriber.getBullet() == 0) {
                    BulletSubscriber.reload(-1);
                }
                BulletSubscriber.removeBullet(1);
                c.applyPowers();
                this.target.damage(new DamageInfo(AbstractDungeon.player, c.damage, c.damageTypeForTurn));
                if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:BulletVune")) {
                    AbstractPower igPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:BulletVune");
                    igPower.flash();
                    this.addToTop(new ApplyPowerAction(this.target, AbstractDungeon.player, new VulnerablePower(this.target, igPower.amount, false), igPower.amount));
                }
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                if (!this.skipWait_ && !Settings.FAST_MODE) {
                    this.addToTop(new WaitAction(0.1F));
                }
            }

        }
    }
}
