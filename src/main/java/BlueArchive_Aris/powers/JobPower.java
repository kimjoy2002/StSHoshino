package BlueArchive_Aris.powers;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class JobPower extends AbstractPower {
    public AbstractCard equip;

    public abstract void onJobChange();
}
