package BlueArchive_Hoshino.monsters.act1.boss;

import BlueArchive_Hoshino.actions.RemoveAllPowerAction;
import BlueArchive_Hoshino.powers.KaitengerPower;
import BlueArchive_Hoshino.powers.ReflectablePower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hoshino.powers.KaitengerPower.remain_kaitenger;

public abstract class KaitengerCommon extends CustomMonster {

    public int kaitenger_position = 1;
    public static ArrayList<KaitengerCommon> remainKaitenger = new ArrayList<KaitengerCommon>();

    public KaitengerCommon(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        if(offsetX<0)
            kaitenger_position = 0;
        if(offsetX>0)
            kaitenger_position = 2;

    }


    public abstract KaitengerCommon copyKaitenger(float x, float y);


    public boolean isAllDead(){
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if(!m.isDead && !m.isDying && !m.halfDead) {
                return false;
            }
        }
        return true;
    }
}
