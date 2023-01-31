package BlueArchive_Hoshino.subscriber;

import BlueArchive_Hoshino.cards.BlackMarket;
import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.powers.BulletVigorPower;
import BlueArchive_Hoshino.powers.ShieldPower;
import BlueArchive_Hoshino.relics.*;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Iterator;

public class BulletSubscriber implements PostDrawSubscriber, OnStartBattleSubscriber, PostEnergyRechargeSubscriber, OnCardUseSubscriber, PostBattleSubscriber, CustomSavable<Integer>, PreStartGameSubscriber, PostDeathSubscriber, PostCreateStartingRelicsSubscriber {
    private static int bullet = 4;
    private static int maxBullet = 4;
    private static int maxBulletLimit = 10;
    private static int defaultBullet = 4;
    public static int reloadedThisCombat = 0;

    public BulletSubscriber() {
    }

    public static int getBullet() {
        return bullet;
    }

    public static int getMaxBullet() {
        return maxBullet;
    }
    public static int getMaxBulletLimit() {
        return maxBulletLimit;
    }

    public static boolean getUpToMaxBullet() {
        return maxBullet < maxBulletLimit;
    }


    public static boolean reload() {
        return reload(0);
    }
    public static boolean reload(int number) {
        if((number == -1 && getMaxBullet() > getBullet()) || number >= 0) {
            setBullet(number>0?number:getMaxBullet());
            int blockAmt = 0;
            int shieldAmt = 0;


            for(int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                AbstractRelic relic_ = AbstractDungeon.player.relics.get(i);
                if (relic_ instanceof ReloadRelic) {
                    ((ReloadRelic) relic_).onReload();
                }
            }

            if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:ExpertPower")) {
                AbstractPower skPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:ExpertPower");
                skPower.flash();
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, skPower.amount));
            }

            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:HoshinoBaseRelicPlus")) {
                shieldAmt += HoshinoBaseRelicPlus.MAGIC;
                AbstractDungeon.player.getRelic("BlueArchive_Hoshino:HoshinoBaseRelicPlus").flash();
            }

            if(shieldAmt > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new ShieldPower(AbstractDungeon.player, AbstractDungeon.player, shieldAmt), shieldAmt));
            }

            if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:TacticalReloadPower")) {
                AbstractPower trPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:TacticalReloadPower");
                trPower.flash();
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction((AbstractCreature)null, DamageInfo.createDamageMatrix(trPower.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
            }

            if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:BlackMarketPower")) {
                AbstractPower bmPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:BlackMarketPower");
                bmPower.flash();
                AbstractCard c = BlackMarket.returnRandomBlackMarketCardInCombat().makeCopy();
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c, true));
            }


            String[] bullets_ = {"BlueArchive_Hoshino:BulletVigor", "BlueArchive_Hoshino:BulletVune", "BlueArchive_Hoshino:BulletIgni"};

            for(String bullet_ : bullets_) {
                Iterator var2 = AbstractDungeon.player.powers.iterator();
                while(var2.hasNext())  {
                    AbstractPower p = (AbstractPower)var2.next();
                    if(/*p.ID.equals("BlueArchive_Hoshino:ReloadLosePower") ||*/
                            p.ID.equals(bullet_)
                    ) {
                        p.flash();
                        RemoveSpecificPowerAction ac_  = new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, p.ID);
                        ac_.update();
                        break;
                        //AbstractDungeon.actionManager.addToTop();
                    }
                }
            }


            if (AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:EyeOfHorusRelic")) {
                AbstractRelic relic = AbstractDungeon.player.getRelic("BlueArchive_Hoshino:EyeOfHorusRelic");
                if(relic.counter == 1) {
                    relic.flash();
                    relic.counter = 0;
                    relic.stopPulse();
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BulletVigorPower(AbstractDungeon.player, EyeOfHorusRelic.AMOUNT), EyeOfHorusRelic.AMOUNT));
                }
            }

            AbstractDungeon.actionManager.addToBottom(new SFXAction("BlueArchive_Hoshino:Reload"));

            if(AbstractDungeon.player instanceof Hoshino) {
                if (shieldAmt > 0 && !AbstractDungeon.player.hasPower("BlueArchive_Hoshino:ShieldPower")) {
                    AnimationState.TrackEntry e = AbstractDungeon.player.state.setAnimation(0, "fastShieldUpAnimation", false);
                    e = AbstractDungeon.player.state.addAnimation(0, Hoshino.SHIELD_RELOAD_ANIMATION, false, e.getEndTime());
                    AbstractDungeon.player.state.addAnimation(0, Hoshino.SHIELD_BASE_ANIMATION, true, e.getEndTime());
                } else {
                    AnimationState.TrackEntry e = AbstractDungeon.player.state.setAnimation(0, Hoshino.getReloadTextureName(), false);
                    AbstractDungeon.player.state.addAnimation(0, Hoshino.getNutralTextureName(), true, e.getEndTime());
                }
            }
            BulletSubscriber.reloadedThisCombat++;
            return true;
        }
        return false;
    }

    public static void addBullet(int amount) {
        if (amount >= 0) {
            setBullet(bullet + amount);
        }
    }

    public static void removeBullet(int amount) {
        if (amount >= 0) {
            setBullet(Math.max(bullet - amount, 0));
        }
    }

    public static void setBullet(int value) {
        int afterBullet = Math.max(Math.min(value, maxBullet), 0);

        if(afterBullet < bullet) {
            if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:SupressionVeteranPower")) {
                AbstractPower svPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:SupressionVeteranPower");
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, svPower.amount * (bullet - afterBullet)));
                svPower.flash();
            }
            if(AbstractDungeon.player.hasPower("BlueArchive_Hoshino:BulletIgni")) {
                AbstractPower trPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:BulletIgni");
                trPower.flash();
                for(int i = 0; i < (bullet - afterBullet); i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction((AbstractCreature) null, DamageInfo.createDamageMatrix(trPower.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
                }
            }
            for(int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                AbstractRelic relic_ = AbstractDungeon.player.relics.get(i);
                if (relic_ instanceof ShotRelic) {
                    ((ShotRelic) relic_).onBulletUse(bullet - afterBullet);
                }
            }
        }

        bullet = afterBullet;
    }

    public static void addMaxBullet(int amount) {
        if (amount >= 0) {
            setMaxBullet(maxBullet + amount);
        }
    }

    public static void removeMaxBullet(int amount) {
        if (amount >= 0) {
            setMaxBullet(Math.max(maxBullet - amount, 0));
        }
    }

    public static void setMaxBullet(int value) {
        if (value >= 0) {
            maxBullet = Math.min(Math.min(value, maxBulletLimit), maxBulletLimit);
        }
        defaultBullet = maxBullet;
    }

    public Integer onSave() {
        System.out.println("@@Saving MaxBullet...");
        return maxBullet;
    }

    public void onLoad(Integer savedMaxBullet) {
        System.out.println("@@Loading MaxBullet...");
        if (savedMaxBullet == null) {
            maxBullet = 4;
        } else {
            maxBullet = savedMaxBullet;
        }

    }

    public void receivePostDraw(AbstractCard abstractCard) {
    }

    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        setBullet(defaultBullet);
        System.out.println("Current bullet: " + bullet);
    }

    public void receivePostBattle(AbstractRoom abstractRoom) {
        System.out.println("Saving...");
        bullet = 4;
        defaultBullet = 4;
        maxBullet = 4;
        System.out.println("Saved.");
    }

    public void receiveCardUsed(AbstractCard abstractCard) {

        System.out.println("receiveCardUsed Current SP: " + bullet);
    }


    public void receivePreStartGame() {
        defaultBullet = 4;
        maxBullet = 4;
    }

    public void receivePostDeath() {
        maxBullet = 4;
    }

    public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass playerClass, ArrayList<String> arrayList) {
        System.out.println("Saving...");
        bullet = 4;
        maxBullet = 4;
        System.out.println("Saved.");
    }

    @Override
    public void receivePostEnergyRecharge() {

    }
}
