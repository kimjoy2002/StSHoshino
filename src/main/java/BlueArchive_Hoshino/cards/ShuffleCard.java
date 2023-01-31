package BlueArchive_Hoshino.cards;

import java.util.concurrent.atomic.AtomicInteger;

public interface ShuffleCard {
    public static AtomicInteger totalShuffledThisTurn = new AtomicInteger();
    public void onShuffle();
}
