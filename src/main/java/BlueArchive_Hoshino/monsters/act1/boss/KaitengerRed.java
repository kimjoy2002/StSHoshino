package BlueArchive_Hoshino.monsters.act1.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.CallingKaitengerAction;
import BlueArchive_Hoshino.powers.DelayedPower;
import BlueArchive_Hoshino.powers.KaitengerPower;
import BlueArchive_Hoshino.powers.TauntPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.Collections;
import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;
import static BlueArchive_Hoshino.powers.KaitengerPower.remain_kaitenger;


public class KaitengerRed extends KaitengerCommon {
    public static final String ID = DefaultMod.makeID(KaitengerRed.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String BGM ="Kaitenger.ogg";
    private int dmg;

    private int turn = 0;
    private boolean is_first_turn = true;

    public KaitengerRed() {
        this(0.0F, 0.0F);
    }

    public KaitengerRed(float x, float y) {
        super(NAME, ID, 75, -5.0F, 0.0F, 150.0F, 250.0F, makeMonstersPath("Kaitenger_red.png"), x, y);
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(80);
        } else {
            this.setHp(75);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg = 7;
        } else {
            this.dmg = 6;
        }
        turn = 0;

        kaitenger_position = 1;
        this.damage.add(new DamageInfo(this, this.dmg, DamageInfo.DamageType.NORMAL));
    }

    public KaitengerCommon copyKaitenger(float x, float y) {

        return new KaitengerRed(x,y);
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

            this.setMove((byte)4, Intent.UNKNOWN);
            this.createIntent();
        }
    }



    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new TauntPower(AbstractDungeon.player,this, 2)));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new CallingKaitengerAction(2));
                break;
            case 4:
                this.halfDead = false;
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
                AbstractDungeon.actionManager.addToBottom(new CallingKaitengerAction(1));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if(halfDead) {
            this.setMove((byte)4, Intent.UNKNOWN);
        }
        else if(is_first_turn) {
            this.setMove((byte)3, Intent.UNKNOWN);
            is_first_turn = false;
        } else {
            if (this.lastMove((byte)2)){
                this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            } else {
                this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
            }
        }
        this.createIntent();
    }


    public void usePreBattleAction() {
        super.usePreBattleAction();
        UnlockTracker.markBossAsSeen(ID);
        AbstractDungeon.getCurrRoom().cannotLose = true;
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        remain_kaitenger = 4;
        remainKaitenger.clear();
        remainKaitenger.add(new KaitengerGreen());
        remainKaitenger.add(new KaitengerBlack());
        remainKaitenger.add(new KaitengerYellow());
        remainKaitenger.add(new KaitengerPink());
        Collections.shuffle(remainKaitenger, new java.util.Random(AbstractDungeon.shuffleRng.randomLong()));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new KaitengerPower(this,this)));
        AbstractDungeon.getCurrRoom().playBgmInstantly(BGM);
    }

    public void init() {
        super.init();
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            if(isAllDead()) {
                this.onBossVictoryLogic();
            }
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Kaitenger_red");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
