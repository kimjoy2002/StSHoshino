package BlueArchive_Aris.cards;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface AttackedCard {

    public int onAttacked(DamageInfo info, int damageAmount);
}
