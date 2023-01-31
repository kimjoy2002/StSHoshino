package BlueArchive_Hoshino.patches.power;

import BlueArchive_Hoshino.cards.ShuffleCard;
import BlueArchive_Hoshino.powers.RefillAmmoPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.Iterator;

@SpirePatch(
        clz = EmptyDeckShuffleAction.class,
        method = SpirePatch.CONSTRUCTOR
)
public class EmptyDeckShufflePatch {
    public static void Postfix(EmptyDeckShuffleAction __instance)
    {
        if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
        {
            ShuffleCard.totalShuffledThisTurn.getAndIncrement();
            Iterator var1 = AbstractDungeon.player.hand.group.iterator();

            while(var1.hasNext()) {
                AbstractCard c = (AbstractCard)var1.next();
                if (c instanceof ShuffleCard) {
                    ((ShuffleCard) c).onShuffle();
                }
            }
        }
        {
            if (AbstractDungeon.player.hasPower("BlueArchive_Hoshino:RefillAmmoPower")) {
                AbstractPower sdPower = AbstractDungeon.player.getPower("BlueArchive_Hoshino:RefillAmmoPower");
                ((RefillAmmoPower) sdPower).onShuffle();
            }
        }
    }
}
