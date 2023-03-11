package BlueArchive_Hoshino.monsters.act4.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.PeroroRising;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.monsters.act2.boss.HodPillar;
import BlueArchive_Hoshino.powers.ExplodePower;
import BlueArchive_Hoshino.powers.HodGloryPower;
import BlueArchive_Hoshino.powers.TauntPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.*;

import java.util.HashMap;
import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class Hifumi extends CustomMonster {
    public static final String ID = DefaultMod.makeID(Hifumi.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String ATLAS = makeMonstersPath("Hifumi.atlas");
    private static final String SKEL = makeMonstersPath("Hifumi.json");
    private boolean isFirstMove = true;
    private boolean firstPeroroDead = false;
    private boolean firstPeroroBuff = false;
    private boolean firstPeroroBigger = false;
    private int moveCount = 0;
    private int blockAmt;
    private int explodeAmt = 30;

    private HashMap<Integer, AbstractMonster> enemySlots = new HashMap();
    public Hifumi() {
        this(0.0F, 0.0F);
    }

    public Hifumi(float x, float y) {
        super(NAME, ID, 750, -5.0F, 0.0F, 200.0F, 300.0F, (String)null, x, y);
        this.loadAnimation(ATLAS, SKEL, 0.9F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(0);
        this.flipHorizontal = true;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(800);
        } else {
            this.setHp(750);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            blockAmt = 45;
            explodeAmt = 35;
        } else {
            blockAmt = 40;
            explodeAmt = 30;
        }

    }

    public void takeTurn() {
        if(firstPeroroBigger == false && getPeroro() != null && getPeroro().maxHealth >= 100) {
            firstPeroroBigger = true;
            AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[4], 1.0F, 2.0F));
        }
        else if(this.nextMove == 4) {
            if(firstPeroroBuff == false) {
                firstPeroroBuff = true;
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[3], 1.0F, 2.0F));
            }
        }

        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster peroro = getPeroro();
        switch (this.nextMove) {
            case 1:
                if(peroro != null)
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new TauntPower(AbstractDungeon.player,peroro, 2)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                break;
            case 2:
                if(peroro != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(peroro, this, new ExplodePower(peroro , this, explodeAmt), explodeAmt));
                }
                explodeAmt+=10;
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, (AbstractDungeon.player instanceof  Hoshino)? DIALOG[1]:DIALOG[0], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartMegaDebuffEffect()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                PeroroRising p1 = new PeroroRising();
                p1.upgrade();
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(p1, 1, true, false, false, (float)Settings.WIDTH * 0.2F, (float)Settings.HEIGHT / 2.0F));
                PeroroRising p2 = new PeroroRising();
                if (AbstractDungeon.ascensionLevel >= 19) {
                    p2.upgrade();
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(p2, 1, true, false, false, (float)Settings.WIDTH * 0.4F, (float)Settings.HEIGHT / 2.0F));
                PeroroRising p3 = new PeroroRising();
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(p3, 1, true, false, false, (float)Settings.WIDTH * 0.6F, (float)Settings.HEIGHT / 2.0F));
                PeroroRising p4 = new PeroroRising();
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(p4, 1, true, false, false, (float)Settings.WIDTH * 0.8F, (float)Settings.HEIGHT / 2.0F));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, blockAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new PeroroRising(), 1, true, false, false, (float)Settings.WIDTH * 0.4F, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new PeroroRising(), 1, true, false, false, (float)Settings.WIDTH * 0.6F, (float)Settings.HEIGHT / 2.0F));
                break;

        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected  AbstractMonster getPeroro() {

        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if (m instanceof PeroroHifumi) {
                return m;
            }
        }
        return null;
    }





    protected void getMove(int num) {
        if (this.isFirstMove) {
            this.setMove(MOVES[2], (byte)3, Intent.STRONG_DEBUFF);
            this.isFirstMove = false;
        } else {
            switch (this.moveCount % 3) {
                case 0:
                    if (AbstractDungeon.aiRng.randomBoolean()) {
                        this.setMove(MOVES[0], (byte)1, Intent.DEBUFF);
                    } else {
                        this.setMove(MOVES[1], (byte)2, Intent.BUFF);
                    }
                    break;
                case 1:
                    if (!this.lastMove((byte)2)) {
                        this.setMove(MOVES[1], (byte)2, Intent.BUFF);
                    } else {
                        this.setMove(MOVES[0], (byte)1, Intent.DEBUFF);
                    }
                    break;
                default:
                    this.setMove(MOVES[3], (byte)4, Intent.DEFEND_DEBUFF);
            }

            ++this.moveCount;
        }
        this.createIntent();
    }


    public static void isHifumiReact() {
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if (m instanceof Hifumi) {
                if(((Hifumi)m).firstPeroroDead == false) {
                    ((Hifumi)m).firstPeroroDead = true;
                    AbstractDungeon.actionManager.addToBottom(new ShoutAction(m, DIALOG[2], 1.0F, 2.0F));
                }
            }
        }
    }

    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");
        int invincibleAmt = 300;
        if (AbstractDungeon.ascensionLevel >= 19) {
            invincibleAmt -= 100;
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, invincibleAmt), invincibleAmt));
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            AbstractMonster peroro = getPeroro();
            if(peroro != null) {
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(peroro, false));
            }
            super.die();
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Hifumi");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
