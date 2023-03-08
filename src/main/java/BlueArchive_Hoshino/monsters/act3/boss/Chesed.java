package BlueArchive_Hoshino.monsters.act3.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.monsters.act2.boss.HodPillar;
import BlueArchive_Hoshino.monsters.act2.boss.Peroro;
import BlueArchive_Hoshino.monsters.act4.boss.PeroroHifumi;
import BlueArchive_Hoshino.powers.*;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class Chesed extends CustomMonster {
    public static final String ID = DefaultMod.makeID(Chesed.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String ATLAS = makeMonstersPath("Chesed.atlas");
    private static final String SKEL = makeMonstersPath("Chesed.json");
    private static final String BGM ="Chesed.ogg";
    private int dmg;
    private int summon = 0;
    private boolean isopen = false;
    private boolean isFirstTurn = true;
    private int blockAmt;

    private HashMap<Integer, AbstractMonster> enemySlots = new HashMap();
    public Chesed() {
        this(0.0F, 0.0F);
    }

    public Chesed(float x, float y) {
        super(NAME, ID, 105, -5.0F, 0.0F, 300.0F, 450.0F, (String)null, x, y);
        this.loadAnimation(ATLAS, SKEL, 0.7F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(110);
        } else {
            this.setHp(105);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg = 45;
            blockAmt = 18;
        } else {
            this.dmg = 40;
            blockAmt = 15;
        }

        this.damage.add(new DamageInfo(this, this.dmg, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

                while(var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster)var1.next();
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, blockAmt));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChesedStrPower(this, 3), 3));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.75F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 10), 10));
                break;
            case 3:
            {
                if(summon%4 == 0) {
                    Drone d1 = new Drone(-450.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d1, true));
                    Soldier s1 = new Soldier(-250.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(s1, true));
                    Turret t1 = new Turret(-50.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(t1, true));
                }
                else if(summon%4 == 1) {
                    AbstractMonster d1 = (AbstractDungeon.ascensionLevel >= 19) ?new Soldier(-450.0F,0.0f) : new Drone(-450.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d1, true));
                    Goliat g1 = new Goliat(-150.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(g1, true));
                } else if(summon%4 == 2) {
                    Soldier s1 = new Soldier(-450.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(s1, true));
                    Soldier s2 = new Soldier(-250.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(s2, true));
                    Soldier s3 = new Soldier(-50.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(s3, true));
                } else {
                    AbstractMonster d1 = new Drone(-500.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d1, true));
                    AbstractMonster d2 = new Drone(-350.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d2, true));
                    AbstractMonster d3 = new Drone(-200.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d3, true));
                    AbstractMonster d4 = (AbstractDungeon.ascensionLevel >= 19) ?new Soldier(-50.0F,0.0f) : new Drone(-50.0F,0.0f);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d4, true));
                }
                summon++;
                break;
            }
            case 4:
            {
                break;
            }
            case 5:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), 2));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    public void damage(DamageInfo info) {
        if (info.type == DamageInfo.DamageType.THORNS && info.output > 0 && this.hasPower("BlueArchive_Hoshino:ChesedMinionPower")) {
            info.output = (int) (info.output*0.1f);
            if(info.output <= 0) {
                info.output = 1;
            }
        }
        super.damage(info);
    }

    protected void getMove(int num) {
        if (isFirstTurn){
            if (AbstractDungeon.aiRng.randomBoolean()) {
                this.setMove((byte) 5, Intent.DEBUFF);
            } else {
                this.setMove(MOVES[0], (byte)1, Intent.BUFF);
            }
            isFirstTurn = false;
        } else if (isopen) {
            this.setMove(MOVES[2], (byte)2, Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(0)).base);
        }
        else {
            Iterator<AbstractMonster> var1 = AbstractDungeon.getMonsters().monsters.iterator();
            int nmonum = 0;
            while(var1.hasNext()) {
                AbstractMonster m = var1.next();
                if(!m.isDying && !m.halfDead && !m.isDead && m != this) {
                    nmonum++;
                }
            }
            if(nmonum == 0) {
                //몹이 없을때 소환
                this.setMove(MOVES[1], (byte)3, Intent.UNKNOWN);
            } else {
                if(this.lastMove((byte)5))
                    this.setMove(MOVES[0], (byte)1, Intent.BUFF);
                else
                    this.setMove((byte)5, Intent.DEBUFF);
            }
        }
        this.createIntent();
    }

    public void summonCheck() {
        Iterator<AbstractMonster> var1 = AbstractDungeon.getMonsters().monsters.iterator();
        int num = 0;
        while(var1.hasNext()) {
            AbstractMonster m = var1.next();
            if(!m.isDying && !m.halfDead && !m.isDead && m != this) {
                num++;
            }
        }
        if(num == 0) {
            if (this.getPower("BlueArchive_Hoshino:ChesedMinionPower") != null) {
                AbstractPower minionPower = this.getPower("BlueArchive_Hoshino:ChesedMinionPower");
                if(minionPower.amount > 1) {
                    this.addToBot(new ReducePowerAction(this, this, "BlueArchive_Hoshino:ChesedMinionPower", 1));
                    this.setMove(MOVES[1], (byte)3, Intent.UNKNOWN);

                } else {
                    this.addToBot(new RemoveSpecificPowerAction(this, this, "BlueArchive_Hoshino:ChesedMinionPower"));
                    this.setMove(MOVES[3], (byte)4, Intent.STUN);
                    AnimationState.TrackEntry e = this.state.setAnimation(0, "open_animation", false);
                    this.state.addAnimation(0, "after_animation", true, e.getEndTime());
                    isopen = true;
                }
                this.createIntent();
            }
        }
    }
    public static Chesed getChesed() {

        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if (m instanceof Chesed) {
                return (Chesed)m;
            }
        }
        return null;
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
        UnlockTracker.markBossAsSeen(ID);
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();

        AbstractMonster d1 = new Drone(-500.0F,0.0f);
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d1, true));
        AbstractMonster d2 = new Drone(-350.0F,0.0f);
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d2, true));
        AbstractMonster d3 = new Drone(-200.0F,0.0f);
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d3, true));
        AbstractMonster d4 = (AbstractDungeon.ascensionLevel >= 19) ?new Soldier(-50.0F,0.0f) : new Drone(-50.0F,0.0f);
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(d4, true));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChesedMinionPower(this, 3)));
        AbstractDungeon.getCurrRoom().playBgmInstantly(BGM);
    }

    public void die() {
        super.die();
        this.onBossVictoryLogic();
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if(!m.isDying && !m.isDead && !m.halfDead && m != this){
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(m, false));
            }
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Chesed");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
