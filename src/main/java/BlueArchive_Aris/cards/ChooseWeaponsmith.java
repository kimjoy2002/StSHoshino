package BlueArchive_Aris.cards;

import BlueArchive_Aris.actions.DoubleChargeAction;
import BlueArchive_Aris.actions.EquipsmithAction;
import BlueArchive_Hoshino.DefaultMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeArisCardPath;
import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class ChooseWeaponsmith extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeArisID(ChooseWeaponsmith.class.getSimpleName());
    public static final String IMG = makeArisCardPath("Weaponsmith.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public ChooseWeaponsmith(int amount) {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        magicNumber = baseMagicNumber = amount;
    }
    public ChooseWeaponsmith() {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        magicNumber = baseMagicNumber = 2;
    }



    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void onChoseThisOption() {
        this.addToBot(new EquipsmithAction(magicNumber, true));
    }

    public void upgrade() {
    }

    public AbstractCard makeCopy() {
        return new ChooseWeaponsmith(magicNumber);
    }

}
