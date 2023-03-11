package BlueArchive_Hoshino.monsters.act4.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.monsters.act2.boss.Perorodzilla;
import BlueArchive_Hoshino.powers.DelayedPower;
import BlueArchive_Hoshino.powers.PeroroRevivePower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;
import static BlueArchive_Hoshino.monsters.act4.boss.Hifumi.isHifumiReact;

public class PeroroHifumi extends AbstractMonster {
    public static final String ID = DefaultMod.makeID(PeroroHifumi.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private static final String ATLAS = makeMonstersPath("Peroro.atlas");
    private static final String SKEL = makeMonstersPath("Peroro.json");
    private static final int BEAM_DMG = 15;
    private static final int BEAM2_DMG = 1;
    public int beam_power = 6;
    private static boolean is_first = true;
    private static boolean after_revive = false;
    public float size = 1.0f;

    public float pwidth = 1.0f;
    public float pheight = 1.0f;

    public PeroroHifumi() {
        this(0.0F, 0.0F);
    }
    public PeroroHifumi(float x, float y) {
        super(NAME, ID, 40, 0.0F, 0.0F, 300.0F, 300.0F, (String)null, x, y);
        this.loadAnimation(ATLAS, SKEL, 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(45);
        } else {
            this.setHp(40);
        }
        this.pwidth = this.skeleton.getData().getWidth();
        this.pheight = this.skeleton.getData().getWidth();

        this.currentHealth = 0;
        halfDead = true;

        this.damage.add(new DamageInfo(this, BEAM_DMG));
        this.damage.add(new DamageInfo(this, BEAM2_DMG));
    }

    protected boolean isHifumiLive() {

        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if (m instanceof Hifumi) {
                if(!m.isDying && !m.isDead && !m.halfDead)
                    return true;
            }
        }
        return false;
    }



    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY+ 40.0F * Settings.scale), 0.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY+ 40.0F * Settings.scale), 0.3F));
                for(int i = 0; i < beam_power; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                }
                break;
            case 3:
            {
                if (MathUtils.randomBoolean()) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("DARKLING_REGROW_2", MathUtils.random(-0.1F, 0.1F)));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("DARKLING_REGROW_1", MathUtils.random(-0.1F, 0.1F)));
                }

                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MinionPower(this)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PeroroRevivePower(this, this)));
                after_revive = true;
                this.halfDead = false;
                Iterator var1 = AbstractDungeon.player.relics.iterator();

                while(var1.hasNext()) {
                    AbstractRelic r = (AbstractRelic)var1.next();
                    r.onSpawnMonster(this);
                }
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }



    public void damage(DamageInfo info) {
        super.damage(info);

        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;

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
            this.powers.clear();
            this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "BlueArchive_Hoshino:TauntPower"));
            isHifumiReact();
            this.setMove((byte)3, Intent.UNKNOWN);
            this.createIntent();
        }
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();


    }

    public void init() {
        super.init();
    }

    public void update() {
        super.update();
    }

    public void sizeUp() {
        size += 0.2f;
        this.loadAnimation(ATLAS, SKEL, 1.0F/size);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }
    public void updateMove() {
        if (this.lastMove((byte)1))
            this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        else if(this.lastMove((byte)2))
            this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, beam_power, true);
    }

    protected void getMove(int num) {
        if(halfDead) {
            this.setMove((byte)3, Intent.UNKNOWN);
        } else if(after_revive) {
            this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            after_revive = false;
        } else if(is_first) {
            if (AbstractDungeon.aiRng.randomBoolean()) {
                this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, beam_power, true);
            } else {
                this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            }
            is_first = false;
        }
        else if (this.lastMove((byte)1)) {
            this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, beam_power, true);
        } else {
            this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        }
    }

    public void die() {
        if(!isHifumiLive()){
            super.die();
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Peroro");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
