package BlueArchive_Hoshino.monsters.act3.boss;

import BlueArchive_Hoshino.DefaultMod;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.*;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class Hieronymus extends CustomMonster {
    public static final String ID = DefaultMod.makeID(Hieronymus.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String BGM ="Hieronymus.ogg";
    private int dmg_double;
    private int dmg_light;
    private int dmg_final;
    private boolean thresholdReached = false;

    public Hieronymus() {
        this(0.0F, 0.0F);
    }

    public Hieronymus(float x, float y) {
        super(NAME, ID, 400, -5.0F, 0.0F, 400.0F, 500.0F, makeMonstersPath("Hieronymus.png"), x, y);
        this.flipHorizontal = false;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(470);
        } else {
            this.setHp(450);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg_double = 8;
            this.dmg_light = 27;
            this.dmg_final = 8;

        } else {
            this.dmg_double = 7;
            this.dmg_light = 25;
            this.dmg_final = 7;
        }

        this.damage.add(new DamageInfo(this, this.dmg_double, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_light, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_final, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new LightningEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.75F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ScreenOnFireEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.FIRE));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (this.currentHealth < this.maxHealth / 2 && !this.thresholdReached) {
            this.thresholdReached = true;
            this.setMove(MOVES[3],(byte)4, Intent.ATTACK, ((DamageInfo)this.damage.get(2)).base, 4, true);
        }
        else if (num < 40) {
            if (!this.lastMove((byte)2) && !this.lastMoveBefore((byte)2)) {
                this.setMove(MOVES[1], (byte)2, Intent.DEBUFF);
            } else {
                this.getMove(AbstractDungeon.aiRng.random(40, 99));
                return;
            }
        } else if (num < 70) {
            if (!this.lastMove((byte)1)) {
                this.setMove(MOVES[0], (byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 3, true);
            } else {
                this.setMove(MOVES[2], (byte)3, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
            }
        } else {
            if (!this.lastTwoMoves((byte)3)) {
                this.setMove(MOVES[2], (byte)3, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
            } else {
                this.setMove(MOVES[0], (byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 3, true);
            }
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
    public static Hieronymus getHieronymus() {

        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if (m instanceof Hieronymus) {
                return (Hieronymus)m;
            }
        }
        return null;
    }

    public void die() {
        super.die();
        this.onBossVictoryLogic();
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if(!m.isDying && !m.isDead && m != this){
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(m, false));
            }
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Hieronymus");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
