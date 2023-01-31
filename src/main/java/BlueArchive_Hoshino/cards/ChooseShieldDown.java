package BlueArchive_Hoshino.cards;

import BlueArchive_Hoshino.DefaultMod;
import BlueArchive_Hoshino.powers.ShieldPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hoshino.DefaultMod.makeCardPath;

public class ChooseShieldDown extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ChooseShieldDown.class.getSimpleName());
    public static final String IMG = makeCardPath("ChooseShieldDown.png");
    private static final CardStrings cardStrings;

    private static final int BLOCK = 0;
    public ChooseShieldDown() {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        baseBlock = BLOCK;
    }
    public ChooseShieldDown(int amount) {
        super(ID, IMG, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        baseBlock = amount;
        this.applyPowersToBlock();
        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void onChoseThisOption() {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.block));
    }

    public void upgrade() {
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("BlueArchive_Hoshino:ChooseShieldDown");
    }

}
