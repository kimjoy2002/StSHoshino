package BlueArchive_Hoshino.effects;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.util.Iterator;

import static java.lang.Math.max;

public class GachaCardEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 2.5F;
    private static Texture r = new Texture("BlueArchive_HoshinoResources/images/ui/R.png");
    private static Texture sr = new Texture("BlueArchive_HoshinoResources/images/ui/SR.png");
    private static Texture ssr = new Texture("BlueArchive_HoshinoResources/images/ui/SSR.png");
    private float drawScale;
    private final float targetDrawScale;
    private final float current_x;
    private final float current_y;

    private float color_ = 1.0F;
    public float alpha = 1.0F;

    private Texture texture;

    public GachaCardEffect(int rare, float x, float y) {
        this.texture = rare==3?ssr:(rare==2?sr:r);
        this.duration = 3.5F;
        this.startingDuration = 3.5F;
        this.drawScale = 0.01F;
        this.targetDrawScale = 0.9F;
        this.current_x = x;
        this.current_y = y;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale);

        if (this.duration < 0.6F) {
            alpha= max(0, alpha-Gdx.graphics.getDeltaTime()/0.6F);
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            this.color = new Color(color_, color_, color_, alpha);
            sb.setColor(this.color);
            int w = texture.getWidth();
            int h = texture.getHeight();
            sb.draw(texture, this.current_x - (float) w / 2.0F, this.current_y - (float) h / 2.0F, (float) w / 2.0F, (float) h / 2.0F, (float) w, (float) h, this.drawScale , this.drawScale, this.rotation, 0, 0, w, h, false, false);
        }
    }

    public void dispose() {
    }

}
