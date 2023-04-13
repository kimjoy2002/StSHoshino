package BlueArchive_Aris.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class CursedWeaponEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vX;
    private float vY;
    private TextureAtlas.AtlasRegion img;
    public CursedWeaponEffect(float x, float y) {
        this.duration = MathUtils.random(2.0F, 3.0F);
        this.startingDuration = this.duration;
        this.img = ImageMaster.EXHAUST_L;
        this.scale = MathUtils.random(0.8F, 1.2F) * Settings.scale;
        this.color = new Color(MathUtils.random(0.6F, 0.7F), MathUtils.random(0.0F, 0.1F), MathUtils.random(0.1F, 0.2F), 0.0F);

        this.x = x - (float)(this.img.packedWidth / 2);
        this.y = y - (float)(this.img.packedHeight / 2);
        this.scale = Settings.scale * MathUtils.random(0.5F, 1.0F);
        this.vX = MathUtils.random(-30.0F, 30.0F) * Settings.scale;
        this.vY = MathUtils.random(20.0F, 100.0F) * Settings.scale;
        this.rotation = MathUtils.random(0.0F, 360.0F);

        this.renderBehind = true;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.color.a = this.duration / 2.0F;
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);

        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);

        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
