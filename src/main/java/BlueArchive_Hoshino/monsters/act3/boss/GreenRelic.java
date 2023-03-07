package BlueArchive_Hoshino.monsters.act3.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.HalfDeadAction;
import BlueArchive_Hoshino.actions.SetHpAction;
import BlueArchive_Hoshino.effects.RelicAura;
import BlueArchive_Hoshino.powers.ExplosivePower2;
import BlueArchive_Hoshino.powers.GreenRelicPower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;
import static BlueArchive_Hoshino.monsters.act3.boss.Hieronymus.getHieronymus;
import static BlueArchive_Hoshino.patches.EnumPatch.HOSHINO_SHOTGUN;

public class GreenRelic extends AbstractMonster {
    public static final String ID = DefaultMod.makeID(GreenRelic.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String ATLAS = makeMonstersPath("Relic.atlas");
    private static final String SKEL = makeMonstersPath("Relic.json");

    private boolean effect = false;
    protected float particleTimer;
    public GreenRelic() {
        this(0.0F, 0.0F);
    }
    public GreenRelic(float x, float y) {
        super(NAME, ID, 10, 0.0F, 0.0F, 150.0F, 150.0F, (String)null, x, y);

        this.loadAnimation(ATLAS, SKEL, 0.75F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "green_relic", false);
        float relic_endtimes = e.getEndTime();

        if (AbstractDungeon.ascensionLevel >= 19) {
            setHp(6);
        } else {
            setHp(8);
        }
        currentHealth = 1;
        e.setTime(relic_endtimes/maxHealth);
        e.setEndTime(relic_endtimes/maxHealth);
    }

    public void takeTurn() {
        switch (this.nextMove) {
            default:
            case 1:
                break;
            case 2:
            {
                AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
                Hieronymus hieronymus = getHieronymus();
                if(hieronymus != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(hieronymus, this, new StrengthPower(hieronymus, 2)));
                }
                AbstractDungeon.actionManager.addToBottom( new SetHpAction(this, 1));
                effect = false;
            }
            break;

        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GreenRelicPower(this)));
        AbstractDungeon.actionManager.addToBottom(new HalfDeadAction(this));
    }

    public void damage(DamageInfo info) {
    }
    public void init() {
        super.init();
    }

    public void update() {
        super.update();
        if(effect) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = MathUtils.random(0.3F, 0.4F);
                AbstractDungeon.effectsQueue.add(new RelicAura(this, "Green"));
            }
        }
    }

    public void heal(int amount) {
        super.heal(amount);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "green_relic", false);
        float relic_endtimes = e.getEndTime();
        e.setTime(relic_endtimes*currentHealth/maxHealth);
        e.setEndTime(relic_endtimes*currentHealth/maxHealth);

        if(!effect && currentHealth == maxHealth){
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NoDrawPower(AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToBottom(new HalfDeadAction(this));
            this.setMove((byte)2, Intent.BUFF);
            this.createIntent();
            effect = true;
        }
    }
    protected void getMove(int num) {
        this.setMove((byte)1, Intent.NONE);
        this.createIntent();
    }
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:GreenRelic");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
