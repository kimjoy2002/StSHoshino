package BlueArchive_Hoshino.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SiroBallReturnEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float start_x;
    private float start_y;
    private float target_x;
    private float target_y;
    private float air_resist;
    private float g = 3000.0f;
    private static Texture texture = new Texture("BlueArchive_HoshinoResources/images/effects/siro_ball.png");

    public static float alpha = 1.0F;
    private float color_ = 1.0F;
    public SiroBallReturnEffect(float start_x, float start_y, float target_x, float target_y) {
        start_x -= texture.getWidth()/2 * Settings.scale;
        target_x -= texture.getWidth()/2 * Settings.scale;
        start_y -= texture.getHeight()/2 * Settings.scale;
        target_y -= texture.getHeight()/2 * Settings.scale;

        target_y = start_y; //생각해보니 같은 높이일 필요가 없음

        this.x = start_x;
        this.y = start_y;
        this.start_x = start_x;
        this.start_y = start_y;
        this.target_x = target_x;
        this.target_y = target_y;
        this.duration = 0.5F;



        air_resist = 2000;
        vx = (target_x - start_x) / duration + ((0.5f * air_resist) * duration);
        this.vy= ((target_y - start_y) / duration) + ((0.5f * g) * duration);
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        float dt = Gdx.graphics.getDeltaTime();
        x += vx * dt;
        y += vy * dt;
        vx -= air_resist * dt;
        vy -= g * dt;
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
