package BlueArchive_Hoshino.monsters.act2.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.HodGloryPower;
import BlueArchive_Hoshino.powers.KaitenMagicCirclePower;
import BlueArchive_Hoshino.powers.MackerelSlashPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.*;

import java.util.ArrayList;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class KaitenFXMK0 extends CustomMonster {
    public static final String ID = DefaultMod.makeID(KaitenFXMK0.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    protected Color color =  new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static final String BGM ="KaitenFXMK0.ogg";
    private static Texture CIRCLE = new Texture(makeMonstersPath("Circle.png"));
    private static Texture RED_CIRCLE = new Texture(makeMonstersPath("RedMagicCircle.png"));
    private static Texture GREEN_CIRCLE = new Texture(makeMonstersPath("GreenMagicCircle.png"));
    private int dmg_gatling;
    private int dmg_missle;
    private int dmg_slash;
    private int slashUP;
    private int blockAmt;
    private int plateAmt;
    private int numTurns = 0;
    public static boolean isSlash = false;
    public static boolean isFirstTurn = true;
    private int dmgThreshold;
    private int dmgTaken;
    private float rotation=0.0f;

    public KaitenFXMK0() {
        this(0.0F, 0.0F);
    }

    public KaitenFXMK0(float x, float y) {
        super(NAME, ID, 480, -5.0F, 0.0F, 460.0F, 600.0F, makeMonstersPath("KaitenFXMK0.png"), x, y);

        this.flipHorizontal = false;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(490);
        } else {
            this.setHp(480);
        }

        dmgThreshold = 40;
        slashUP = 10;
        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg_gatling = 4;
            this.dmg_missle = 18;
            this.dmg_slash = 35;
            this.blockAmt = 16;
            this.plateAmt = 6;
        } else {
            this.dmg_gatling = 4;
            this.dmg_missle = 17;
            this.dmg_slash = 32;
            this.blockAmt = 15;
            this.plateAmt = 5;
        }

        this.damage.add(new DamageInfo(this, this.dmg_gatling, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_missle, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dmg_slash, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                if(getColor()==1) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2));
                }

                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_WHIRLWIND"));

                for(int i = 0; i < 1; ++i) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_HEAVY"));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new CleaveEffect(true), 0.15F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.NONE, true));
                }
                if(getColor()==1) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2));
                }
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.blockAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this, this.plateAmt), this.plateAmt));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new DieDieDieEffect(), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MackerelSlashPower(this, this, slashUP), slashUP));
                break;
            case 5:
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        ArrayList<Integer> list = new ArrayList<Integer>();

        if (!this.lastMove((byte)1)){
            list.add(1);
        }
        if (!this.lastMove((byte)2)){
            list.add(2);
        }
        if (!this.lastMove((byte)3) && !lastMoveBefore((byte)3) && !isFirstTurn){
            list.add(3);
        }
        isFirstTurn = false;

        int nextMove = (Integer)list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));


        if (this.numTurns == 4) {
            isSlash = true;
            this.setMove(MOVES[3], (byte) 4, Intent.ATTACK_BUFF, ((DamageInfo) this.damage.get(2)).base);
        } else if (nextMove == 1) {
            isSlash = false;
            this.setMove(MOVES[0], (byte) 1, Intent.ATTACK, ((DamageInfo) this.damage.get(0)).base, 3, true);
        } else if (nextMove == 2) {
            isSlash = false;
            this.setMove(MOVES[1], (byte) 2, Intent.ATTACK, ((DamageInfo) this.damage.get(1)).base);
        } else if (nextMove == 3) {
            isSlash = false;
            this.setMove(MOVES[2], (byte) 3, Intent.DEFEND_BUFF);
        } else {
            isSlash = false;
            this.setMove(MOVES[0], (byte) 1, Intent.ATTACK, ((DamageInfo) this.damage.get(0)).base, 3, true);
        }

        numTurns++;
        if(numTurns > 4) {
            numTurns = 0;
        }
        this.createIntent();
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
        UnlockTracker.markBossAsSeen(ID);
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new KaitenMagicCirclePower(this, this.dmgThreshold)));
        AbstractDungeon.getCurrRoom().playBgmInstantly(BGM);
    }


    public void damage(DamageInfo info) {
        int tmpHealth = this.currentHealth;
        super.damage(info);
        if (tmpHealth > this.currentHealth && !this.isDying) {
            this.dmgTaken += tmpHealth - this.currentHealth;
            if (this.getPower("BlueArchive_Hoshino:KaitenMagicCirclePower") != null) {
                AbstractPower var = this.getPower("BlueArchive_Hoshino:KaitenMagicCirclePower");
                var.amount -= tmpHealth - this.currentHealth;
                while(var.amount <= 0) {
                    var.amount+=dmgThreshold;
                    ((KaitenMagicCirclePower)var).circle_color++;
                    if(((KaitenMagicCirclePower)var).circle_color > 2){
                        ((KaitenMagicCirclePower)var).circle_color = 0;
                    }
                    var.flash();
                    AbstractDungeon.onModifyPower();
                }
                this.getPower("BlueArchive_Hoshino:KaitenMagicCirclePower").updateDescription();
            }

            while (this.dmgTaken >= this.dmgThreshold) {
                this.dmgTaken -= dmgThreshold;
            }
        }

    }

    public void update() {
        super.update();
        //this.rotation += Gdx.graphics.getDeltaTime()*100;
    }
    public int getColor() {

        if (this.getPower("BlueArchive_Hoshino:KaitenMagicCirclePower") != null) {
            AbstractPower var = this.getPower("BlueArchive_Hoshino:KaitenMagicCirclePower");
            return ((KaitenMagicCirclePower) var).circle_color;
        }
        return -1;
    }


    @Override
    public void render(SpriteBatch sb) {
        if (this.getPower("BlueArchive_Hoshino:KaitenMagicCirclePower") != null) {
            AbstractPower var = this.getPower("BlueArchive_Hoshino:KaitenMagicCirclePower");

            if (((KaitenMagicCirclePower) var).circle_color == 0) {
                color = CardHelper.getColor(255.0f, 208.0f, 220.0f);
            } else if (((KaitenMagicCirclePower) var).circle_color == 1) {
                color = CardHelper.getColor(255.0f, 50.0f, 50.0f);
            } else {
                color = CardHelper.getColor(50.0f, 255.0f, 50.0f);
            }

            sb.setColor(this.color);

            int w = CIRCLE.getWidth();
            int h = CIRCLE.getHeight();
            sb.draw(CIRCLE, drawX- (150.0f+(float)w) * Settings.scale / 2.0F + this.animX, 0.0f, (float) w / 2.0F, h / 2.0F, w, h, 1.0f, 0.4f, rotation, 0, 0, w, h, false, false);
        }
        //sb.setBlendFunction(770, 1);

        super.render(sb);
    }

    public void die() {
        super.die();
        this.onBossVictoryLogic();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:KaitenFXMK0");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
