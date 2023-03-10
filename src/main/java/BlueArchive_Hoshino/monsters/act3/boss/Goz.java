package BlueArchive_Hoshino.monsters.act3.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.ForceWaitAction;
import BlueArchive_Hoshino.cards.GozBomb;
import BlueArchive_Hoshino.cards.PeroroRising;
import BlueArchive_Hoshino.cards.SiroBomb;
import BlueArchive_Hoshino.effects.KuroCarEffect;
import BlueArchive_Hoshino.powers.GozeThresholdPower;
import BlueArchive_Hoshino.powers.KaitenMagicCirclePower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class Goz extends CustomMonster {
    public static final String ID = DefaultMod.makeID(Goz.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String ATLAS = makeMonstersPath("Goz.atlas");
    private static final String SKEL = makeMonstersPath("Goz.json");
    private static final String BGM ="Goz.ogg";
    private int dmg;
    private int dmg_debuf;
    private int dmg_streamrider;
    private int bomb_num = 3;
    private boolean isFirstTurn = true;
    public static int BUNSHIN_THREASHOLD = 250;
    public static int BUNSHIN_HP = 150;
    public static boolean use_bomb = false;
    private int dmgThreshold;
    private int dmgTaken;
    private int numTurns = 0;

    public Goz() {
        this(0.0F, 0.0F, -1);
    }

    public Goz(float x, float y, int remain_hp) {
        super(NAME, ID, 540, -5.0F, 0.0F, 300.0F, 300.0F, (String)null, x, y);

        this.loadAnimation(ATLAS, SKEL, 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(0);
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 19) {
            bomb_num = 4;
        } else {
            bomb_num = 3;
        }

        this.dmgThreshold = BUNSHIN_THREASHOLD;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(560);
        } else {
            this.setHp(540);
        }
        if(remain_hp>0) {
            this.currentHealth = remain_hp;
            if(use_bomb) {
                isFirstTurn = false;
                numTurns = 3;
            }
        } else {
            use_bomb = false;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg = 26;
            this.dmg_debuf = 15;
            this.dmg_streamrider = 49;
        } else {
            this.dmg = 24;
            this.dmg_debuf = 14;
            this.dmg_streamrider = 46;
        }

        this.damage.add(new DamageInfo(this, this.dmg, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_debuf, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_streamrider, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));

                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new GozBomb(), bomb_num, true, true));
                //for(int i = 0; i< bomb_num; i++) {
                //    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new GozBomb(), 1, true, false, false, (float)Settings.WIDTH * 0.2F+(float)Settings.WIDTH *distant*i, (float)Settings.HEIGHT / 2.0F));
                //}
                //AbstractDungeon.actionManager.addToBottom(new ForceWaitAction(2.0f));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 1, true), 1));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true), 1));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new KuroCarEffect(this.hb.cX, 100 * Settings.scale, false), 0.6F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new GozBomb(), 1, true, false, false, (float)Settings.WIDTH * 0.5F, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new GozBomb(), 1, true, false, false, (float)Settings.WIDTH * 0.5F, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (isFirstTurn) {
            this.setMove(MOVES[3], (byte)1, Intent.STRONG_DEBUFF);
            isFirstTurn = false;
            use_bomb = true;
        }
        else {
            if(numTurns == 0) {
                this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            }
            else if(numTurns == 1) {
                this.setMove((byte)3, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
            }
            else if(numTurns == 2) {
                this.setMove(MOVES[4], (byte)4, Intent.ATTACK, ((DamageInfo)this.damage.get(2)).base);
            }
            else if(numTurns == 3) {
                this.setMove((byte)5, Intent.DEBUFF);
            }

            numTurns++;

            if(numTurns >= 4) {
                numTurns = 0;
            }
        }

        this.createIntent();
    }

    public void damage(DamageInfo info) {
        int tmpHealth = this.currentHealth;
        super.damage(info);
        if (tmpHealth > this.currentHealth && !this.isDying ) {
            this.dmgTaken += tmpHealth - this.currentHealth;
            if (this.getPower("BlueArchive_Hoshino:GozeThresholdPower") != null) {
                AbstractPower var = this.getPower("BlueArchive_Hoshino:GozeThresholdPower");
                if(var.amount > 0) {
                    var.amount -= tmpHealth - this.currentHealth;
                    if(var.amount <= 0) {
                        AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
                        AbstractDungeon.actionManager.addToBottom(new HideHealthBarAction(this));
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));
                        int rand_ = AbstractDungeon.monsterRng.random(2);
                        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new GozBunsin(-350.F, 0.0F, rand_==0?true:false, currentHealth), false));
                        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new GozBunsin(0.0F, 0.0F, rand_==1?true:false, currentHealth), false));
                        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new GozBunsin(350.0F, 0.0F, rand_==2?true:false, currentHealth), false));
                        AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
                        AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                        AbstractDungeon.onModifyPower();
                        this.addToTop(new ClearCardQueueAction());
                    } else {
                        this.getPower("BlueArchive_Hoshino:GozeThresholdPower").updateDescription();
                    }
                }
            }
        }

    }
    public void usePreBattleAction() {
        super.usePreBattleAction();
        UnlockTracker.markBossAsSeen(ID);
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GozeThresholdPower(this, this.dmgThreshold)));
        AbstractDungeon.getCurrRoom().playBgmInstantly(BGM);
    }

    public void die() {
        super.die();
        if(!AbstractDungeon.getCurrRoom().cannotLose){
            this.onBossVictoryLogic();
        }
        /*Iterator var1 = AbstractDungeon.actionManager.actions.iterator();

        AbstractGameAction a;
        do {
            if (!var1.hasNext()) {
                if (this.currentHealth <= 0) {
                    this.onBossVictoryLogic();
                }
                return;
            }

            a = (AbstractGameAction)var1.next();
        } while(!(a instanceof SpawnMonsterAction));*/
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Goz");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
