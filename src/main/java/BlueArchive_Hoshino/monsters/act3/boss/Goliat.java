package BlueArchive_Hoshino.monsters.act3.boss;

import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static BlueArchive_Hoshino.DefaultMod.makeMonstersPath;
import static BlueArchive_Hoshino.patches.EnumPatch.HOSHINO_SHOTGUN;

public class Goliat extends AbstractMonster {
    public static final String ID = DefaultMod.makeID(Goliat.class.getSimpleName());
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private int bomb = 24;
    private int gatling = 6;
    private int nuke = 45;
    private int turn = 0;

    public Goliat() {
        this(0.0F, 0.0F);
    }
    public Goliat(float x, float y) {
        super(NAME, ID, 200, 0.0F, 0.0F, 250.0F, 250.0F, makeMonstersPath("Goliat.png"), x, y);

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(205);
        } else {
            this.setHp(200);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.bomb = 26;
            this.nuke = 48;
            this.gatling = 7;
        } else {
            this.bomb = 24;
            this.nuke = 45;
            this.gatling = 6;
        }


        this.damage.add(new DamageInfo(this, bomb));
        this.damage.add(new DamageInfo(this, gatling));
        this.damage.add(new DamageInfo(this, nuke));
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), HOSHINO_SHOTGUN));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), HOSHINO_SHOTGUN));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), HOSHINO_SHOTGUN));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                this.addToBot(new VFXAction(new ExplosionSmallEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.FIRE));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
    }

    public void init() {
        super.init();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
    }

    public void update() {
        super.update();
    }

    protected void getMove(int num) {
        if(turn ==0) {
            if (AbstractDungeon.aiRng.randomBoolean()) {
                this.setMove((byte) 1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            } else {
                this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, 3, true);
            }
        } else if(turn < 3) {
            if (this.lastMove((byte)2)) {
                this.setMove((byte) 1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            } else {
                this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, 3, true);
            }
        } else {
            this.setMove((byte)3, Intent.ATTACK, ((DamageInfo)this.damage.get(2)).base);
        }

        turn++;
        this.createIntent();
    }
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        Chesed chesed = Chesed.getChesed();
        if(chesed != null) {
            chesed.summonCheck();
        }
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueArchive_Hoshino:Goliat");
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
