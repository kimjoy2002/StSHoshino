package BlueArchive_Hoshino.monsters.act2.boss;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.DelayedPower;
import BlueArchive_Hoshino.powers.EnergyLosePower;
import BlueArchive_Hoshino.powers.HodGloryPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;

public class Peroro extends AbstractMonster {
    public static final String ID = DefaultMod.makeID(Peroro.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private static final String ATLAS = makeMonstersPath("Peroro.atlas");
    private static final String SKEL = makeMonstersPath("Peroro.json");
    private static final int BEAM_DMG = 6;

    public Peroro() {
        this(0.0F, 0.0F);
    }
    public Peroro(float x, float y) {
        super(NAME, ID, 60, 0.0F, 0.0F, 150.0F, 200.0F, (String)null, x, y);
        this.loadAnimation(ATLAS, SKEL, 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "base_animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(70);
        } else {
            this.setHp(60);
        }

        this.damage.add(new DamageInfo(this, BEAM_DMG));
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
            {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, "BlueArchive_Hoshino:PeroroPower"));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
                Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

                while(var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster)var1.next();
                    if(m instanceof Perorodzilla) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new DelayedPower(m, this, new StrengthPower(this, (AbstractDungeon.ascensionLevel >= 19)?3:2))));
                    }
                }
                break;
            }
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
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

    protected void getMove(int num) {
        if (this.lastTwoMoves((byte)1)) {
            this.setMove((byte)2, Intent.UNKNOWN);
        } else {
            this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Peroro");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
