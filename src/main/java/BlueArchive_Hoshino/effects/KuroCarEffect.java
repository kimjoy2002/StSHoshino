package BlueArchive_Hoshino.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class KuroCarEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float speed;
    private static Texture texture = new Texture("BlueArchive_HoshinoResources/images/effects/kuro_car.png");

    public static float alpha = 1.0F;
    private float color_ = 1.0F;
    public KuroCarEffect(float x, float y, boolean back) {
        this.x = x;
        this.y = y;
        this.speed = 1.5f;
        this.duration = 2.0F;
        if(back) {
            speed *= -1;
        }

    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.x -= Gdx.graphics.getDeltaTime() * 1000.0f * this.speed  * Settings.scale;
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }
    public void render(SpriteBatch sb) {
        this.color = new Color(color_, color_, color_, alpha);
        sb.setColor(this.color);
        int w = texture.getWidth();
        int h = texture.getHeight();
        sb.draw(texture, this.x, this.y, (float) w / 2.0F, (float) h / 2.0F, (float) w, (float) h, this.scale, this.scale, this.rotation, 0, 0, w, h, speed<0?true:false, false);
    }

    @Override
    public void dispose() {

    }
}
