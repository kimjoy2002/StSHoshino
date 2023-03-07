package BlueArchive_Hoshino.monsters.act1.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.BankRobbery;
import BlueArchive_Hoshino.cards.SandStorm;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import static BlueArchive_Hoshino.DefaultMod.makeBgmPath;
import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class Binah extends CustomMonster {
    public static final String ID = DefaultMod.makeID(Binah.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String ATLAS = makeMonstersPath("Binah.atlas");
    private static final String SKEL = makeMonstersPath("Binah.json");
    private static final String BGM ="Binah.ogg";
    private int dmg_missle;
    private int dmg_laser;
    private int dmg_sandwind;
    private boolean thresholdReached = false;

    public Binah() {
        this(0.0F, 0.0F);
    }

    public Binah(float x, float y) {
        super(NAME, ID, 180, -5.0F, 0.0F, 460.0F, 430.0F, (String)null, x, y);
        this.loadAnimation(ATLAS, SKEL, 0.6F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.flipHorizontal = false;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(190);
        } else {
            this.setHp(180);
        }

        this.dmg_missle = 3;
        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg_laser = 13;
            this.dmg_sandwind = 39;
        } else {
            this.dmg_laser = 12;
            this.dmg_sandwind = 36;
        }

        this.damage.add(new DamageInfo(this, this.dmg_laser, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_missle, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_sandwind, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX - 80.0F * Settings.scale, this.hb.cY + 10.0F * Settings.scale), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_WHIRLWIND"));

                for(int i = 0; i < 4; ++i) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_HEAVY"));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new CleaveEffect(true), 0.15F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.NONE, true));
                }
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ScreenOnFireEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 3, true), 3));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_SLIME_ATTACK"));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new SandStorm(), 3));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new SandStorm(), 2));
                }
                break;
            case 5:
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (this.currentHealth < this.maxHealth / 2 && !this.thresholdReached) {
            this.thresholdReached = true;
            this.setMove((byte)5, Intent.UNKNOWN);
        }
        else if (this.lastMove((byte)5)) {
            this.setMove(MOVES[2], (byte)3, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(2)).base);
        }
        else if (this.lastMove((byte)4)) {
            if(thresholdReached) {
                this.setMove(MOVES[2], (byte)3, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(2)).base);
            } else {
                this.setMove(MOVES[0], (byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            }
        } else if (this.lastMove((byte)1) || this.lastMove((byte)3)) {
            this.setMove(MOVES[1], (byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, 4, true);
        } else if (this.lastMove((byte)2)) {
            this.setMove(MOVES[3], (byte)4, Intent.STRONG_DEBUFF);
        } else {
            this.setMove(MOVES[3], (byte)4, Intent.STRONG_DEBUFF);
        }

        this.createIntent();
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
        UnlockTracker.markBossAsSeen(ID);
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly(BGM);
    }

    public void die() {
        super.die();
        this.onBossVictoryLogic();
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Binah");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
