package BlueArchive_Hoshino.monsters.act3.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.ExplosivePower2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ExplosivePower;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;
import static BlueArchive_Hoshino.patches.EnumPatch.HOSHINO_SHOTGUN_LIGHT;

public class Drone extends AbstractMonster {
    public static final String ID = DefaultMod.makeID(Drone.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private int attackDmg = 5;
    private int bombAmt = 20;
    private int turnCount = 0;

    public Drone() {
        this(0.0F, 0.0F);
    }
    public Drone(float x, float y) {
        super(NAME, ID, 25, 0.0F, 0.0F, 100.0F, 180.0F, makeMonstersPath("Drone.png"), x, y);

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(27, 35);
        } else {
            this.setHp(25, 33);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDmg = 6;
        } else {
            this.attackDmg = 5;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.bombAmt = 25;
        } else {
            this.bombAmt = 20;
        }

        this.damage.add(new DamageInfo(this, attackDmg));
    }

    public void takeTurn() {
        ++this.turnCount;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), HOSHINO_SHOTGUN_LIGHT));
            case 2:
            default:
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        }
    }


    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ExplosivePower2(this, 3, bombAmt)));
    }

    public void init() {
        super.init();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ExplosivePower2(this, 3, bombAmt)));
    }

    public void update() {
        super.update();
    }

    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        Chesed chesed = Chesed.getChesed();
        if(chesed != null) {
            chesed.summonCheck();
        }
    }

    protected void getMove(int num) {
        if (this.turnCount < 2) {
            this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        } else {
            this.setMove((byte)2, Intent.UNKNOWN);
        }


    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Drone");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
