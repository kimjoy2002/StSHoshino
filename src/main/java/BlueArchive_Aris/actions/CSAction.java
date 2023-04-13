package BlueArchive_Aris.actions;


import BlueArchive_Aris.cards.LevelUp;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class CSAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractCard theCard = null;
    private boolean upgrade = false;

    public CSAction(AbstractCreature target, DamageInfo info, boolean upgrade) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_MED;
        this.upgrade = upgrade;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            this.target.damage(this.info);
            AbstractCard card = new LevelUp();
            card.upgrade();
            if (upgrade || (((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0)) {
                this.addToBot(new MakeTempCardInHandAction(card, 1));
            } else {
                this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
