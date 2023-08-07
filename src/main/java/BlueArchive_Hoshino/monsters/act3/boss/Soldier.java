package BlueArchive_Hoshino.monsters.act3.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.monsters.act2.boss.Perorodzilla;
import BlueArchive_Hoshino.powers.DelayedPower;
import BlueArchive_Hoshino.powers.ExplosivePower2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;
import static BlueArchive_Hoshino.patches.EnumPatch.HOSHINO_SHOTGUN;

public class Soldier extends AbstractMonster {
    public static final String ID = DefaultMod.makeID(Soldier.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String ATLAS = makeMonstersPath("Soldier.atlas");
    private static final String SKEL = makeMonstersPath("Soldier.json");
    private int attackDmg = 8;
    private int turnCount = 0;
    private int bombAmt = 30;

    public Soldier() {
        this(0.0F, 0.0F);
    }
    public Soldier(float x, float y) {
        super(NAME, ID, 50, 0.0F, 0.0F, 100.0F, 250.0F, (String)null, x, y);

        this.loadAnimation(ATLAS, SKEL, 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(72, 82);
        } else {
            this.setHp(70, 80);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDmg = 9;
        } else {
            this.attackDmg = 7;
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.bombAmt = 40;
        } else {
            this.bombAmt = 35;
        }

        this.damage.add(new DamageInfo(this, attackDmg));
    }

    public void takeTurn() {
        ++this.turnCount;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), HOSHINO_SHOTGUN));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2)));
            case 2:
            default:
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        }
    }
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ExplosivePower2(this, 4, bombAmt)));
    }

    public void init() {
        super.init();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ExplosivePower2(this, 4, bombAmt)));
    }

    public void update() {
        super.update();
    }

    protected void getMove(int num) {
        if (this.turnCount < 3) {
            this.setMove((byte)1, Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(0)).base);
        } else {
            this.setMove((byte)2, Intent.UNKNOWN);
        }
    }
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        Chesed chesed = Chesed.getChesed();
        if(chesed != null) {
            chesed.summonCheck();
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Soldier");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
