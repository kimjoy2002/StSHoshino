package BlueArchive_Hoshino.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class GozeFuzeParticalEffect  extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private TextureAtlas.AtlasRegion img;
    private static Texture texture = new Texture("BlueArchive_HoshinoResources/images/effects/goze_fuze_light.png");
    public static boolean renderGreen = false;

    public GozeFuzeParticalEffect(float x, float y) {
        this.duration = MathUtils.random(0.1F, 0.15F);
        this.startingDuration = this.duration;
        this.img = this.getImg();
        this.x = x - (float)(this.img.packedWidth / 2) + MathUtils.random(-3.0F, 3.0F) * Settings.scale;
        this.y = y - (float)(this.img.packedHeight / 2);
        this.scale = Settings.scale * MathUtils.random(0.5F, 1.0F);
        this.vY = MathUtils.random(0.5F, 1.0F);
        this.vY *= this.vY * Settings.scale;
        this.rotation = MathUtils.random(0.0F, 360.0F);
        this.color = new Color(MathUtils.random(0.1F, 0.3F), MathUtils.random(0.1F, 0.3F), MathUtils.random(0.5F, 0.9F), 1.0F);


        this.renderBehind = true;
    }

    private TextureAtlas.AtlasRegion getImg() {
        return ImageMaster.TORCH_FIRE_2;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        this.y += this.vY * Gdx.graphics.getDeltaTime();
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);

        int w = texture.getWidth();
        int h = texture.getHeight();
        sb.draw(texture, this.x, this.y, (float) w / 2.0F, (float) h / 2.0F, (float) w, (float) h, this.scale, this.scale, this.rotation, 0, 0, w, h, false, false);

        //sb.draw(texture, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}

