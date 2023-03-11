package BlueArchive_Hoshino.monsters.act2.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.monsters.act4.boss.PeroroHifumi;
import BlueArchive_Hoshino.powers.HodGloryPower;
import BlueArchive_Hoshino.powers.PeroroPower;
import BlueArchive_Hoshino.powers.ReflectablePower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.TorchHead;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class Perorodzilla extends CustomMonster {
    public static final String ID = DefaultMod.makeID(Perorodzilla.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String BGM ="Perorodzilla.ogg";
    private float spawnX = -200.0F;
    private int dmg_attack;
    private int dmg_laser;
    private int dmg_big_laser;
    private int numTurns = 0;
    private int readyBeam = 0;


    private HashMap<Integer, AbstractMonster> enemySlots = new HashMap();
    public Perorodzilla() {
        this(0.0F, 0.0F);
    }

    public Perorodzilla(float x, float y) {
        super(NAME, ID, 530, -5.0F, 0.0F, 500.0F, 550.0F, makeMonstersPath("Perorodzilla.png"), x, y);
        this.flipHorizontal = false;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(550);
        } else {
            this.setHp(530);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg_attack = 10;
            this.dmg_laser = 15;
            this.dmg_big_laser = 12;
        } else {
            this.dmg_attack = 9;
            this.dmg_laser = 14;
            this.dmg_big_laser = 11;
        }

        this.damage.add(new DamageInfo(this, this.dmg_attack, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_laser, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_big_laser, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));

                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX- 100.0F * Settings.scale, this.hb.cY+ 100.0F * Settings.scale), 0.0F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX+ 100.0F * Settings.scale, this.hb.cY+ 100.0F * Settings.scale), 0.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, (AbstractDungeon.ascensionLevel >= 19)?3:2, true), (AbstractDungeon.ascensionLevel >= 19)?3:2));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX - 100.0F * Settings.scale, this.hb.cY + 100.0F * Settings.scale), 0.0F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX + 100.0F * Settings.scale, this.hb.cY + 100.0F * Settings.scale), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                //fall_through
            case 3:
            {
                int key;
                Iterator var3 = this.enemySlots.entrySet().iterator();

                while(true) {
                    if (!var3.hasNext()) {
                        break;
                    }

                    Map.Entry<Integer, AbstractMonster> m = (Map.Entry)var3.next();
                    if (((AbstractMonster)m.getValue()).isDying) {
                        Peroro newMonster = new Peroro(spawnX + -185.0F * (float)(Integer)m.getKey(),0.0f);
                        key = (Integer)m.getKey();
                        this.enemySlots.put(key, newMonster);
                        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(newMonster, true));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newMonster, newMonster, new PeroroPower(newMonster,newMonster, this)));
                    }
                }
                break;
            }
            case 5:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_SLIMEBOSS_1A"));
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ShakeScreenAction(0.3F, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.LOW));
                break;

        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (this.numTurns == 0) {
            this.setMove(MOVES[0], (byte) 1, Intent.ATTACK, ((DamageInfo) this.damage.get(0)).base);
        } else if (this.numTurns == 1) {
            this.setMove(MOVES[1], (byte) 2, Intent.ATTACK_DEBUFF, ((DamageInfo) this.damage.get(1)).base);
        } else if (this.numTurns == 2) {
            if(readyBeam == 1)
                this.setMove((byte) 5, Intent.SLEEP);
            else
                this.setMove(MOVES[2], (byte) 3, Intent.UNKNOWN);
        } else if (this.numTurns == 3) {
            this.setMove(MOVES[3], (byte) 4, Intent.ATTACK, ((DamageInfo) this.damage.get(2)).base, 3, true);
        }

        numTurns++;
        if(numTurns>2 && readyBeam== 0) {
            numTurns = 0;
            readyBeam = 1;
        }
        if(numTurns > 3) {
            numTurns = 0;
            readyBeam = 0;
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
        AbstractDungeon.scene.fadeOutAmbiance();

        for(int key = 1; key < 3; ++key) {
            Peroro newMonster = new Peroro(spawnX + -185.0F * (float)key, 0.0f);
            AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_COLLECTOR_SUMMON"));
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(newMonster, true));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newMonster, newMonster, new PeroroPower(newMonster,newMonster, this)));
            this.enemySlots.put(key, newMonster);
        }
        AbstractDungeon.getCurrRoom().playBgmInstantly(BGM);
    }

    public void die() {
        super.die();
        this.onBossVictoryLogic();

        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if (m instanceof Peroro) {
                if(!m.isDying && !m.isDead && !m.halfDead){
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(m, false));
                }
            }
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Perorodzilla");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
