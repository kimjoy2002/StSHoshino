package BlueArchive_Hoshino.monsters.act3.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.GozBomb;
import BlueArchive_Hoshino.effects.KuroCarEffect;
import BlueArchive_Hoshino.monsters.act2.boss.Peroro;
import BlueArchive_Hoshino.powers.GozeBushinPower;
import BlueArchive_Hoshino.powers.GozeThresholdPower;
import BlueArchive_Hoshino.powers.HodGloryPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;
import static BlueArchive_Hoshino.monsters.act3.boss.Goz.BUNSHIN_HP;
import static BlueArchive_Hoshino.monsters.act3.boss.Goz.BUNSHIN_THREASHOLD;


public class GozBunsin extends CustomMonster {
    public static final String ID = DefaultMod.makeID(GozBunsin.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private int dmg;
    private int dmg_threelight;
    private int numTurns = 0;
    public boolean isReal = false;
    public int remain_hp = 0;

    public GozBunsin() {
        this(0.0F, 0.0F, false, 100);
    }

    public GozBunsin(float x, float y, boolean isReal, int remain_hp) {
        super(NAME, ID, BUNSHIN_HP, -5.0F, 0.0F, 300.0F, 500.0F, makeMonstersPath("Goz.png"), x, y);
        this.flipHorizontal = false;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.isReal = isReal;


        this.setHp(BUNSHIN_HP);

        this.remain_hp = remain_hp;
        if(remain_hp < BUNSHIN_HP) {
            this.setHp(remain_hp);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg = 10;
            this.dmg_threelight = 14;
        } else {
            this.dmg = 9;
            this.dmg_threelight = 13;
        }

        this.damage.add(new DamageInfo(this, this.dmg, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_threelight, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));

                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 2:
                break;
            case 3:
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new GrandFinalEffect(), 0.7F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new GrandFinalEffect(), 1.0F));
                }
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, isReal?3:1), isReal?3:1));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if(numTurns == 0) {
            this.setMove((byte)4, Intent.BUFF);
        } else if(numTurns != 3) {
            if (this.getPower("BlueArchive_Hoshino:GozeBushinPower") != null) {
                GozeBushinPower var = (GozeBushinPower)this.getPower("BlueArchive_Hoshino:GozeBushinPower");
                var.revealed = true;
                var.updateDescription();
            }
            this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        } else {
            if(!isReal) {
                this.setMove((byte)2, Intent.SLEEP);
            } else {
                this.setMove(MOVES[1], (byte)3, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, 3, true);
            }
        }

        numTurns++;
        if(numTurns == 4) {
            numTurns = 0;
        }
        this.createIntent();
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
    }


    public void damage2(DamageInfo info) {
        super.damage(info);

        if (this.currentHealth <= 0 && !this.halfDead && AbstractDungeon.getCurrRoom().cannotLose) {
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

            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
        }
    }

    public void init() {
        super.init();
        if(remain_hp > BUNSHIN_HP) {
            AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GozeBushinPower(this)));
    }

    public void die() {
        if(isReal && remain_hp > BUNSHIN_HP) {
            Goz newGoz = new Goz(0.0F, 0.0F, remain_hp-BUNSHIN_HP);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(newGoz, false));
            if(remain_hp-BUNSHIN_HP > BUNSHIN_THREASHOLD) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newGoz, this, new GozeThresholdPower(newGoz, BUNSHIN_THREASHOLD)));
            }

            Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster m = (AbstractMonster) var1.next();
                if(!m.isDying && !m.isDead && !m.halfDead && m != this){
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(m, false));
                }
            }
            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
        }
        else if (isReal) {
            Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster m = (AbstractMonster) var1.next();
                if(!m.isDying && !m.isDead && !m.halfDead && m != this){
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(m, false));
                }
            }
            this.onBossVictoryLogic();
            super.die();
        }
        else {
            super.die();
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Goz");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
