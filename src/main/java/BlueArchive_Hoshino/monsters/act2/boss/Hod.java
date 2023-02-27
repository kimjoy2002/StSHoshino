package BlueArchive_Hoshino.monsters.act2.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.HodGloryPower;
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
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class Hod extends CustomMonster {
    public static final String ID = DefaultMod.makeID(Hod.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String ATLAS = makeMonstersPath("Hod.atlas");
    private static final String SKEL = makeMonstersPath("Hod.json");
    private static final String BGM ="Hod.ogg";
    private int dmg_shock;
    private int dmg_arm;
    private int dmg_laser;
    private int strAmt;
    private int blockAmt;
    private boolean initialSpawn = true;
    private boolean thresholdReached = false;
    private int numTurns = 0;

    private HashMap<Integer, AbstractMonster> enemySlots = new HashMap();
    public Hod() {
        this(0.0F, 0.0F);
    }

    public Hod(float x, float y) {
        super(NAME, ID, 360, -5.0F, 0.0F, 600.0F, 500.0F, (String)null, x, y);
        this.loadAnimation(ATLAS, SKEL, 1.5F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.flipHorizontal = false;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(390);
        } else {
            this.setHp(360);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg_shock = 15;
            this.dmg_arm = 10;
            this.dmg_laser = 6;
            this.strAmt = 3;
            blockAmt = 11;
        } else {
            this.dmg_shock = 13;
            this.dmg_arm = 9;
            this.dmg_laser = 6;
            this.strAmt = 2;
            blockAmt = 10;
        }

        this.damage.add(new DamageInfo(this, this.dmg_shock, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_arm, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_laser, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new LightningEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 3:
            {
                int minion_num = 0;
                if(enemySlots.get(0) != null && !enemySlots.get(0).isDying) {
                    minion_num++;
                }
                HodPillar newMonster = new HodPillar(-400.0F + minion_num * -181.0F, 0.0F, 1);

                AbstractDungeon.actionManager.addToBottom(new SFXAction("AUTOMATON_ORB_SPAWN", MathUtils.random(-0.1F, 0.1F)));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(newMonster, true));
                this.enemySlots.put(minion_num, newMonster);
                break;
            }
            case 4:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.LIGHT_YELLOW_COLOR, ShockWaveEffect.ShockWaveType.NORMAL), 1.5F));

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, (AbstractDungeon.ascensionLevel >= 19)?3:2)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strAmt), this.strAmt));
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                break;
            case 6:
                Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

                while(var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster)var1.next();
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, blockAmt));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new ArtifactPower(this, 1)));
                }
                break;

        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (this.currentHealth < this.maxHealth / 2 && !this.thresholdReached) {
            this.thresholdReached = true;
            this.setMove(MOVES[3], (byte)4, Intent.BUFF);
            numTurns = 0;
        } else {
            if (this.numTurns == 0) {
                if(thresholdReached) {
                    this.setMove(MOVES[4], (byte)5, Intent.ATTACK, ((DamageInfo)this.damage.get(2)).base, 4, true);

                } else if(initialSpawn || getMinionNumber() < 2) {
                    this.setMove(MOVES[2],(byte)3, Intent.UNKNOWN);
                    initialSpawn = false;
                } else {
                    this.setMove((byte)6, Intent.DEFEND_BUFF);
                }

            } else if (this.numTurns == 1) {
                if (MathUtils.randomBoolean()) {
                    this.setMove(MOVES[0], (byte)1, Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(0)).base);
                } else {
                    this.setMove(MOVES[1], (byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, 2, true);
                }
            } else {
                if (this.lastMove((byte)2)) {
                    this.setMove(MOVES[0], (byte)1, Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(0)).base);
                } else {
                    this.setMove(MOVES[1], (byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, 2, true);
                }
            }
            numTurns++;
            if(numTurns > 2)
                numTurns = 0;
        }
        this.createIntent();
    }
    private int getMinionNumber() {
        Iterator<AbstractMonster> var1 = this.enemySlots.values().iterator();
        int num = 0;
        while(var1.hasNext()) {
            AbstractMonster m = var1.next();
            if(!m.isDying) {
                num++;
            }
        }
        return num;
    }




    public void usePreBattleAction() {
        super.usePreBattleAction();
        UnlockTracker.markBossAsSeen(ID);
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, (AbstractDungeon.ascensionLevel >= 19)?3:2)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HodGloryPower(this,this)));
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly(BGM);
    }

    public void die() {
        super.die();
        this.onBossVictoryLogic();
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Hod");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
