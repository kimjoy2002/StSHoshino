package BlueArchive_Hoshino.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class ShotDamageRandonEnemyAction extends AbstractGameAction {
    private AbstractCard card;
    private AbstractGameAction.AttackEffect effect;

    public ShotDamageRandonEnemyAction(AbstractCard card, AbstractGameAction.AttackEffect effect) {
        this.card = card;
        this.effect = effect;
    }

    public ShotDamageRandonEnemyAction(AbstractCard card) {
        this(card, AttackEffect.NONE);
    }

    public void update() {
        this.target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null) {
            this.card.calculateCardDamage((AbstractMonster)this.target);
            if (AttackEffect.LIGHTNING == this.effect) {
                this.addToTop(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player, this.card.damage, this.card.damageTypeForTurn), AttackEffect.NONE));
                this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                this.addToTop(new VFXAction(new LightningEffect(this.target.hb.cX, this.target.hb.cY)));

                if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:BulletVune")) {
                    AbstractPower igPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:BulletVune");
                    igPower.flash();
                    this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, igPower.amount, false), igPower.amount));
                }
            } else {
                this.addToTop(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player, this.card.damage, this.card.damageTypeForTurn), this.effect));
                if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:BulletVune")) {
                    AbstractPower igPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:BulletVune");
                    igPower.flash();
                    this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, igPower.amount, false), igPower.amount));
                }
            }
        }

        this.isDone = true;
    }
}

