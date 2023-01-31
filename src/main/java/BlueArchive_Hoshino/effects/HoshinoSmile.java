package BlueArchive_Hoshino.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class HoshinoSmile extends AbstractGameEffect {

    private float duration = 1.0F;
    private float x;
    private float y;
    private static Texture texture = new Texture("BlueArchive_HoshinoResources/images/ending/hoshinoSmile.png");
    public static float alpha = 0.0F;
    private float color_ = 1.0F;

    public HoshinoSmile() {
        this.startingDuration = this.duration;
        this.renderBehind = true;
        this.y = (float) Settings.HEIGHT/ 2;
        this.x = (float) Settings.WIDTH / 2;
        alpha = 0.0F;
        this.scale = 1.0F * Settings.scale*3/2;
    }

    public void render(SpriteBatch sb) {
        this.color = new Color(color_, color_, color_, alpha);
        sb.setColor(this.color);
        int w = texture.getWidth();
        int h = texture.getHeight();
        sb.draw(texture, this.x - (float) w / 2.0F, this.y - (float) h / 2.0F, (float) w / 2.0F, (float) h / 2.0F, (float) w, (float) h, this.scale , this.scale, this.rotation, 0, 0, w, h, false, false);
    }

    public void dispose() {
    }

    public void update() {
        float dt = Gdx.graphics.getDeltaTime();
        alpha += dt / 4.0F;
        if (alpha > 1.0F) {
            alpha = 1.0F;
        }
    }

    private float clamp(float a, float min, float max) {
        return Math.max(Math.min(a, max), min);
    }
}