package BlueArchive_Hoshino.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SiroBallEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float speed;
    private float rad_speed;
    private static Texture texture = new Texture("BlueArchive_HoshinoResources/images/effects/siro_ball.png");

    public static float alpha = 1.0F;
    private float color_ = 1.0F;
    public SiroBallEffect(float x, float y) {
        this.x = x;
        this.y = y;
        this.speed = 1.0f;
        this.rad_speed = 100.0f;
        this.duration = 2.5F;


    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.x -= Gdx.graphics.getDeltaTime() * 1000.0f * this.speed  * Settings.scale;
        this.rotation += Gdx.graphics.getDeltaTime()*rad_speed;
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }
    public void render(SpriteBatch sb) {
        this.color = new Color(color_, color_, color_, alpha);
        sb.setColor(this.color);
        int w = texture.getWidth();
        int h = texture.getHeight();
        sb.draw(texture, this.x, this.y, (float) w / 2.0F, (float) h / 2.0F, (float) w, (float) h, this.scale, this.scale, this.rotation, 0, 0, w, h, false, false);
    }

    @Override
    public void dispose() {

    }
}
