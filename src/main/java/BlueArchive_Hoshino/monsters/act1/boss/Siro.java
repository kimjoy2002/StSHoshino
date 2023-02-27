package BlueArchive_Hoshino.monsters.act1.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.SiroBomb;
import BlueArchive_Hoshino.effects.SiroBallEffect;
import BlueArchive_Hoshino.powers.HodGloryPower;
import BlueArchive_Hoshino.powers.ReflectablePower;
import BlueArchive_Hoshino.powers.SiroKuroPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class Siro extends CustomMonster {
    public static final String ID = DefaultMod.makeID(Siro.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String ATLAS = makeMonstersPath("Siro.atlas");
    private static final String SKEL = makeMonstersPath("Siro.json");
    private static final String BGM ="SiroKuro.ogg";
    private int dmg_ball;

    public Siro() {
        this(0.0F, 0.0F);
    }

    public Siro(float x, float y) {
        super(NAME, ID, 90, -5.0F, 0.0F, 250.0F, 500.0F, (String)null, x, y);
        this.loadAnimation(ATLAS, SKEL, 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.flipHorizontal = false;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(90);
        } else {
            this.setHp(80);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg_ball = 18;
        } else {
            this.dmg_ball = 16;
        }

        this.damage.add(new DamageInfo(this, this.dmg_ball, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new SiroBomb(), 2));
                    SiroBomb c = new SiroBomb();
                    c.upgrade();
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(c, 1));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new SiroBomb(), 3));
                }
                break;
            case 2:
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new SiroBallEffect(this.hb.cX, this.hb.cY- (hb_h+100.f)* Settings.scale), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            case 4: {
                AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
                this.halfDead = false;
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
                Kuro kuro = new Kuro(0.0F, 0.0F);
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(kuro, false));

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(kuro, kuro, new ReflectablePower(kuro,kuro, true)));
                AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                break;
            }
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
         if (this.lastMove((byte)1)) {
            this.setMove((byte)2, Intent.SLEEP);
        } else if (this.lastMove((byte)2)) {
            this.setMove(MOVES[1], (byte)3, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        } else {
            this.setMove(MOVES[0], (byte)1, Intent.STRONG_DEBUFF);
        }

        this.createIntent();
    }
    public void damage(DamageInfo info) {
        super.damage(info);

        if (this.currentHealth <= 0 && !this.halfDead) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                this.halfDead = true;
            }

            Iterator s = this.powers.iterator();

            AbstractPower p;
            while(s.hasNext()) {
                p = (AbstractPower)s.next();
                p.onDeath();
            }

            s = AbstractDungeon.player.relics.iterator();

            while(s.hasNext()) {
                AbstractRelic r = (AbstractRelic)s.next();
                r.onMonsterDeath(this);
            }

            this.addToTop(new ClearCardQueueAction());


            this.setMove((byte)4, Intent.UNKNOWN);
            this.createIntent();
        }
    }


    public void usePreBattleAction() {
        super.usePreBattleAction();
        UnlockTracker.markBossAsSeen(ID);
        AbstractDungeon.getCurrRoom().cannotLose = true;
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SiroKuroPower(this,this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ReflectablePower(this,this, false)));
        AbstractDungeon.getCurrRoom().playBgmInstantly(BGM);
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.onBossVictoryLogic();
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Siro");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
