package BlueArchive_Hoshino.ui;

import BlueArchive_Hoshino.characters.Hoshino;
import BlueArchive_Hoshino.subscriber.BulletSubscriber;
import BlueArchive_Hoshino.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;

import java.util.ArrayList;
import java.util.Iterator;

public class BulletUI extends AbstractPanel {
    public static final String EMPTY_BULLET_IMAGE = "BlueArchive_HoshinoResources/images/ui/bullet_empty.png";
    public static final String FULL_BULLET_IMAGE = "BlueArchive_HoshinoResources/images/ui/bullet_full.png";
    private static final UIStrings uiStrings;
    private static final Color ENERGY_TEXT_COLOR;
    public static float fontScale;
    private Hitbox tipHitbox;
    private float energyVfxAngle;
    private float energyVfxScale;
    private Color energyVfxColor;
    public static String image;

    public static Texture fullBulletTex;
    public static Texture emptyBulletTex;

    private static ArrayList<PowerTip> tips;
    private static PowerTip tooltip;

    public static boolean useBulletBoolean = false;

    public BulletUI() {
        super(96.0F * Settings.scale, 320.0F * Settings.scale, (float)Settings.WIDTH + 480.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F, 12.0F * Settings.scale, -12.0F * Settings.scale, emptyBulletTex, false);
        this.tipHitbox = new Hitbox(0.0F, 0.0F, 120.0F * Settings.scale, 120.0F * Settings.scale);
        this.energyVfxAngle = 0.0F;
        this.energyVfxScale = Settings.scale;
        this.energyVfxColor = Color.WHITE.cpy();
        tips = new ArrayList();
        tooltip = new PowerTip(uiStrings.TEXT[0], uiStrings.TEXT[1]);
        tips.add(tooltip);
        updateTooltip();
        useBulletBoolean = false;
    }

    public static Texture FullBulletVfx() { return TextureLoader.getTexture(FULL_BULLET_IMAGE);}
    public static Texture EmptyBulletVfx() { return TextureLoader.getTexture(EMPTY_BULLET_IMAGE);}

    public void update() {
        this.updateVfx();
        this.current_x = AbstractDungeon.player.drawX;
        this.current_y = AbstractDungeon.player.drawY + 300.0F * Settings.scale;
        if (fontScale != 1.0F) {
            fontScale = MathHelper.scaleLerpSnap(fontScale, 2.0F);
        }

        this.tipHitbox.update();
        if (this.tipHitbox.hovered && !AbstractDungeon.isScreenUp) {
            AbstractDungeon.overlayMenu.hoveredTip = true;
        }

    }


    private void updateVfx() {
        fullBulletTex = FullBulletVfx();
        emptyBulletTex = EmptyBulletVfx();
    }

    public void render(SpriteBatch sb) {
        if (!this.isHidden && CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && useBullet()) {
            this.tipHitbox.move(this.current_x, this.current_y);
            this.renderVfx(sb);
            this.tipHitbox.height = emptyBulletTex.getHeight() * Settings.scale;
            this.tipHitbox.width = 200.0F * Settings.scale;
            this.tipHitbox.render(sb);
            if (this.tipHitbox.hovered && !AbstractDungeon.isScreenUp) {
                TipHelper.renderGenericTip(this.current_x , this.current_y + ((float)emptyBulletTex.getHeight() / 2.0F + 10.0F) * Settings.scale, uiStrings.TEXT[0], uiStrings.TEXT[1]);
            }
        }

    }

    private void renderVfx(SpriteBatch sb) {
        sb.setColor(this.energyVfxColor);
        float dis = 200.0F / (BulletSubscriber.getMaxBulletLimit()-1) * Settings.scale;
        for (int i = 0; i < BulletSubscriber.getMaxBullet();i++) {
            sb.draw(BulletSubscriber.getBullet() > i?fullBulletTex:emptyBulletTex, this.current_x - 100.0F * Settings.scale + i * dis, this.current_y-(float)emptyBulletTex.getHeight() / 2.0F, 32.0F, 32.0F, 32.0F, 32.0F, this.energyVfxScale, this.energyVfxScale, this.energyVfxAngle, 0, 0, 32, 32, false, false);
        }

    }

    public static void updateTooltip() {
        if (AbstractDungeon.player != null) {
            tooltip.body = uiStrings.TEXT[1];
            tips.clear();
            tips.add(tooltip);
        }

    }

    public boolean useBullet() {
        return useBulletBoolean || (AbstractDungeon.player instanceof Hoshino);
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hoshino:BulletToolTips");
        ENERGY_TEXT_COLOR = new Color(1.0F, 1.0F, 0.86F, 1.0F);
        fontScale = 1.0F;
        fullBulletTex = FullBulletVfx();
        emptyBulletTex = EmptyBulletVfx();
    }
}
