package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.actions.DrowsyAction;
import BlueArchive_Hoshino.powers.ShieldPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class ChooseShieldUP extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ChooseShieldUP.class.getSimpleName());
    public static final String IMG = makeCardPath("ChooseShieldUP.png");
    private static final CardStrings cardStrings;

    public ChooseShieldUP() {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        magicNumber = baseMagicNumber = 0;
    }

    public ChooseShieldUP(int amount) {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        magicNumber = baseMagicNumber = amount;
        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void onChoseThisOption() {
        if(AbstractDungeon.player.currentBlock > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new ShieldPower(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.currentBlock), this.magicNumber));
        }
    }

    public void upgrade() {
    }



    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("BlueArchive_Hoshino:ChooseShieldUP");
    }

}
