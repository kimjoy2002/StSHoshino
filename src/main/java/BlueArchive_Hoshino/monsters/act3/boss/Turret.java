package BlueArchive_Hoshino.monsters.act3.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.DelayedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;

public class Turret extends AbstractMonster {
    public static final String ID = DefaultMod.makeID(Turret.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final int BEAM = 12;

    public Turret() {
        this(0.0F, 0.0F);
    }
    public Turret(float x, float y) {
        super(NAME, ID, 120, 0.0F, 0.0F, 150.0F, 320.0F, makeMonstersPath("Turret.png"), x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(125);
        } else {
            this.setHp(120);
        }

        this.damage.add(new DamageInfo(this, BEAM));
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true)));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
    }

    public void init() {
        super.init();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
    }

    public void update() {
        super.update();
    }

    protected void getMove(int num) {
        this.setMove((byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);

    }
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        Chesed chesed = Chesed.getChesed();
        if(chesed != null) {
            chesed.summonCheck();
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Turret");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
