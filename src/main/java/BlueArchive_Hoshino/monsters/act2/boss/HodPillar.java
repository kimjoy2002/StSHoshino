package BlueArchive_Hoshino.monsters.act2.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.EnergyLosePower;
import BlueArchive_Hoshino.powers.HodGloryPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.ApplyStasisAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;

public class HodPillar extends AbstractMonster {
    public static final String ID = DefaultMod.makeID(HodPillar.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private static final String ATLAS = makeMonstersPath("Hod_Pillar.atlas");
    private static final String SKEL = makeMonstersPath("Hod_Pillar.json");
    private static final int BEAM_DMG = 8;
    private boolean usedStasis = false;
    private int count;

    public HodPillar() {
        this(0.0F, 0.0F, 0);
    }
    public HodPillar(float x, float y, int count) {
        super(NAME, ID, 54, 0.0F, 0.0F, 150.0F, 250.0F, (String)null, x, y);
        this.loadAnimation(ATLAS, SKEL, 1.5F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "start_animation", false);
        this.state.addAnimation(0, "base_animation", true, e.getEndTime());
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(56);
        } else {
            this.setHp(54);
        }

        this.count = count;
        this.damage.add(new DamageInfo(this, BEAM_DMG));
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY+ 80.0F * Settings.scale), 0.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new EnergyLosePower(AbstractDungeon.player, this, 1), 1));
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HodGloryPower(this,this)));
    }

    public void init() {
        super.init();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HodGloryPower(this,this)));
    }

    public void update() {
        super.update();
    }

    protected void getMove(int num) {
        if (this.lastMove((byte)1)) {
            this.setMove((byte)2, Intent.DEBUFF);
        } else {
            this.setMove((byte)1, Intent.ATTACK, BEAM_DMG);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:HodPillar");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
