package BlueArchive_Hoshino.monsters.act1.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.cards.SiroBomb;
import BlueArchive_Hoshino.effects.KuroCarEffect;
import BlueArchive_Hoshino.effects.SiroBallEffect;
import BlueArchive_Hoshino.powers.HodGloryPower;
import BlueArchive_Hoshino.powers.ReflectablePower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;


public class Kuro extends CustomMonster {
    public static final String ID = DefaultMod.makeID(Kuro.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String ATLAS = makeMonstersPath("Kuro.atlas");
    private static final String SKEL = makeMonstersPath("Kuro.json");
    private static final String BGM ="SiroKuro.ogg";
    private int dmg_car;
    private int turn = 0;

    public Kuro() {
        this(0.0F, 0.0F);
    }

    public Kuro(float x, float y) {
        super(NAME, ID, 160, -5.0F, 0.0F, 250.0F, 250.0F, (String)null, x, y);
        this.loadAnimation(ATLAS, SKEL, 0.6F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.flipHorizontal = false;
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(180);
        } else {
            this.setHp(170);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.dmg_car = 12;
        } else {
            this.dmg_car = 11;
        }
        turn = 0;

        this.damage.add(new DamageInfo(this, this.dmg_car, DamageInfo.DamageType.NORMAL));
    }

    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 4, true), 4));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new KuroCarEffect(this.hb.cX, 100 * Settings.scale, false), 0.6F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (turn > 0){
            this.setMove(MOVES[1], (byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        } else {
            this.setMove(MOVES[0], (byte)1, Intent.DEBUFF);
        }
        turn++;
        if(turn == 4)
            turn = 0;
        this.createIntent();
    }


    public void usePreBattleAction() {
        super.usePreBattleAction();
        UnlockTracker.markBossAsSeen(ID);
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ReflectablePower(this,this, true)));
        AbstractDungeon.getCurrRoom().playBgmInstantly(BGM);
    }

    public void init() {
        super.init();
        AbstractDungeon.getCurrRoom().cannotLose = false;
    }

    public void die() {
        super.die();
        this.onBossVictoryLogic();
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Kuro");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
