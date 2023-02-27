package BlueArchive_Hoshino.monsters.act1.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.CallingKaitengerAction;
import BlueArchive_Hoshino.powers.DelayedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class KaitengerPink extends KaitengerCommon {
    public static final String ID = DefaultMod.makeID(KaitengerPink.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private int dmg;
    private boolean first_attck = true;


    public KaitengerPink() {
        this(0.0F, 0.0F);
    }

    public KaitengerPink(float x, float y) {
        super(NAME, ID, 35, -5.0F, 0.0F, 150.0F, 250.0F, makeMonstersPath("Kaitenger_pink.png"), x, y);
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(38);
        } else {
            this.setHp(35);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg = 7;
        } else {
            this.dmg = 6;
        }

        this.damage.add(new DamageInfo(this, this.dmg, DamageInfo.DamageType.NORMAL));
    }

    public KaitengerCommon copyKaitenger(float x, float y) {

        return new KaitengerPink(x,y);
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

            this.setMove((byte)3, Intent.UNKNOWN);
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
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));

                Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

                while(var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster)var1.next();
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new DelayedPower(m, this, new StrengthPower(this, 2))));
                }
                break;
            case 3:
                this.halfDead = false;
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
                AbstractDungeon.actionManager.addToBottom(new CallingKaitengerAction(1));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        {
            if(halfDead) {
                this.setMove((byte)3, Intent.UNKNOWN);
            }
            else if (this.lastMove((byte)1) || (first_attck && kaitenger_position == 2)){
                this.setMove(MOVES[0], (byte)2, Intent.BUFF);
            } else {
                this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            }
            if(first_attck) {
                first_attck = false;
            }
        }
        this.createIntent();
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
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Kaitenger_pink");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
